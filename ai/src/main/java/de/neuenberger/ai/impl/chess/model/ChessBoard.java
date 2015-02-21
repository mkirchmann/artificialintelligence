package de.neuenberger.ai.impl.chess.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.Piece.PieceType;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardInstance;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.delegate.IsInCheckCalculationStrategy;
import de.neuenberger.ai.impl.chess.model.delegate.IsInCheckUseBitBoard;

public class ChessBoard implements Board<Piece, Color, ChessPly> {

	private final Piece[] boardContents;
	private ChessPly lastPly;
	private boolean check;
	private Color whosToMove = Color.WHITE;
	private static final BitBoardPreCalculations bitBoardPreCalculations = BitBoardPreCalculations.getInstance();
	private final BitBoardInstance bitBoardInstance;
	static IsInCheckCalculationStrategy isInCheckUseBitBoardStrategy = new IsInCheckUseBitBoard();
	// static IsInCheckCalculationStrategy isInCheckUseBitBoardStrategy = new
	// IsInCheckBruteForceAllFieldsStrategy();

	Logger log = LoggerFactory.getLogger(getClass());

	public ChessBoard() {
		this(new BitBoardInstance(bitBoardPreCalculations));
	}

	public ChessBoard(final BitBoardInstance bitBoardInstance) {
		this.bitBoardInstance = bitBoardInstance;
		boardContents = new Piece[64];
	}

	@Override
	public List<ChessPly> getPossiblePlies(final Color color) {
		final PlyList plyList = new BasePlyList();
		for (int i = 0; i < 64; i++) {
			final Piece piece = boardContents[i];
			if (piece != null && piece.getColor() == color) {
				piece.addPossiblePlies(plyList, this, bitBoardPreCalculations.getPosition(i), true);
			}
		}
		return plyList.getCollection();
	}

	@Override
	public ChessBoard clone() {
		final ChessBoard result = new ChessBoard(bitBoardInstance.clone());

		for (int i = 0; i < 64; i++) {
			final Piece piece = boardContents[i];
			if (piece != null) {
				result.boardContents[i] = piece;
			}
		}

		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				final Position position = fromPosition(x, y);
				final Piece piece = boardContents[position.getIdx()];

				if (piece != null) {
					builder.append(piece);
				} else {
					builder.append("-");
				}
			}
			builder.append("\n");
		}
		if (lastPly != null) {
			builder.append(lastPly).append(" ");
		}
		builder.append(whosToMove).append(" to move");
		return builder.toString();
	}

	@Override
	public int getMinX() {
		return 0;
	}

	@Override
	public int getMaxX() {
		return 7;
	}

	@Override
	public int getMaxY() {
		return 7;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	public ChessBoard apply(final ChessBoardModifier p) {
		final ChessBoard clone = clone();
		if (p instanceof ChessPly) {
			clone.lastPly = (ChessPly) p;
		}
		final BoardChangerImpl boardChanger = clone.createBoardChanger();
		p.applyTo(boardChanger);
		p.applyWhosToMove(boardChanger);
		boardChanger.invalidate();

		return clone;
	}

	private BoardChangerImpl createBoardChanger() {
		return new BoardChangerImpl(this, bitBoardInstance);
	}

	@Override
	public boolean isInCheck(final Color color) {
		return isInCheckUseBitBoardStrategy.isInCheck(color, this, bitBoardInstance);
	}

	@Override
	public boolean checkCoordinatesValid(final int newX, final int newY) {
		return getMinX() <= newX && newX <= getMaxX() && getMinY() <= newY && newY <= getMaxY();
	}

	@Override
	public ChessPly getLastPly() {
		return lastPly;
	}

	private Piece movePiece(final Position source, final Position target) {
		final Piece piece = boardContents[source.getIdx()];
		boardContents[source.getIdx()] = null;
		boardContents[target.getIdx()] = piece;
		return piece;
	}

	private void setPieceAt(final Position target, final Piece piece) {
		boardContents[target.getIdx()] = piece;
	}

	public static class BoardChangerImpl implements BoardChanger {
		boolean valid = true;

		final ChessBoard chessBoard;

		private final BitBoardInstance bitBoardInstance;

		public void invalidate() {
			valid = false;
		}

		public BoardChangerImpl(final ChessBoard chessBoard, final BitBoardInstance bitBoardInstance) {
			this.chessBoard = chessBoard;
			this.bitBoardInstance = bitBoardInstance;
		}

		@Override
		public void movePiece(final Position source, final Position target, final Piece capturedPiece) {
			checkValid();
			final Piece movedPiece = chessBoard.movePiece(source, target);
			bitBoardInstance.movePiece(movedPiece, capturedPiece, source, target);
		}

		@Override
		public void setPieceAt(final Position target, final Piece piece) {
			checkValid();
			chessBoard.setPieceAt(target, piece);
			bitBoardInstance.setPiece(piece, target);

		}

		private void checkValid() {
			if (!valid) {
				throw new IllegalStateException("Board Changer has been invalidated.");
			}
		}

		@Override
		public void setWhosToMove(final Color whosToMove) {
			chessBoard.setWhosToMove(whosToMove);

			chessBoard.check = chessBoard.isInCheck(whosToMove);

			invalidate();
		}

	}

	/**
	 * @return the check
	 */
	public boolean isCheck() {
		return check;
	}

	/**
	 * @return the whosToMove
	 */
	public Color getWhosToMove() {
		return whosToMove;
	}

	/**
	 * @param whosToMove
	 *            the whosToMove to set
	 */
	private void setWhosToMove(final Color whosToMove) {
		this.whosToMove = whosToMove;
	}

	public Piece getPieceAt(final Position target) {
		return boardContents[target.getIdx()];
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getUpperLeftDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getUpperLeftDiagonal(final Position position) {
		return bitBoardPreCalculations.getUpperLeftDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getUpperRightDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getUpperRightDiagonal(final Position position) {
		return bitBoardPreCalculations.getUpperRightDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getLowerLeftDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getLowerLeftDiagonal(final Position position) {
		return bitBoardPreCalculations.getLowerLeftDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getLowerRightDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getLowerRightDiagonal(final Position position) {
		return bitBoardPreCalculations.getLowerRightDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getTopVertical(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getTopVertical(final Position position) {
		return bitBoardPreCalculations.getTopVertical(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getBottomVertical(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getBottomVertical(final Position position) {
		return bitBoardPreCalculations.getBottomVertical(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getLeftHorizontal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getLeftHorizontal(final Position position) {
		return bitBoardPreCalculations.getLeftHorizontal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getRightHorizontal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getRightHorizontal(final Position position) {
		return bitBoardPreCalculations.getRightHorizontal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getKingNormalFields(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getKingNormalFields(final Position position) {
		return bitBoardPreCalculations.getKingNormalFields(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getKnightMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getKnightMoves(final Position position) {
		return bitBoardPreCalculations.getKnightMoves(position);
	}

	public Position fromPosition(final int x, final int y) {
		return bitBoardPreCalculations.fromZeroBasedCoordinates(x, y);
	}

	public Piece getPieceAt(final int i) {
		return boardContents[i];
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getBlackPawnNormalMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getBlackPawnNormalMoves(final Position position) {
		return bitBoardPreCalculations.getBlackPawnNormalMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getWhitePawnNormalMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getWhitePawnNormalMoves(final Position position) {
		return bitBoardPreCalculations.getWhitePawnNormalMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getBlackPawnCaptureMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getBlackPawnCaptureMoves(final Position position) {
		return bitBoardPreCalculations.getBlackPawnCaptureMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getWhitePawnCaptureMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getWhitePawnCaptureMoves(final Position position) {
		return bitBoardPreCalculations.getWhitePawnCaptureMoves(position);
	}

	public int getScore() {
		return bitBoardInstance.getPieceScore();
	}

	/**
	 * @return the bitBoardInstance
	 */
	public BitBoardInstance getBitBoardInstance() {
		return bitBoardInstance;
	}

	public boolean hasEnoughMatingMaterial() {
		return hasEnoughMatingMaterial(Color.WHITE) || hasEnoughMatingMaterial(Color.BLACK);
	}

	public boolean hasEnoughMatingMaterial(final Color color) {
		boolean result;
		final long pawns = bitBoardInstance.getPieceBitBoard(color, PieceType.PAWN);
		if (pawns == 0) {
			// no more pawns.

			if (bitBoardInstance.getPieceBitBoard(color, PieceType.QUEEN) == 0
					&& bitBoardInstance.getPieceBitBoard(color, PieceType.ROOK) == 0) {
				// no more rook and queen.
				final int numberOfKnights = Long.bitCount(bitBoardInstance.getPieceBitBoard(color, PieceType.KNIGHT));
				if (numberOfKnights > 1) {
					// two knights - cant force mate but are still
					// sufficient.
					result = true;
				} else {
					final long bishops = bitBoardInstance.getPieceBitBoard(color, PieceType.BISHOP);
					final int whiteBishops = Long.bitCount(bishops & bitBoardPreCalculations.getWhiteFields());
					final int blackBishops = Long.bitCount(bishops & bitBoardPreCalculations.getBlackFields());
					if (whiteBishops == 0 && blackBishops == 0 && numberOfKnights == 0) {
						// nothing but the bare king.
						result = false;
					} else if (whiteBishops > 0 && blackBishops > 0) {
						result = true;
					} else if ((whiteBishops > 0 || blackBishops > 0) && numberOfKnights == 1) {
						// mate with knight and bishop can be forced.
						result = true;
					} else {
						// there is some material left
						final int otherSide = Long.bitCount(bitBoardInstance.getColorBitBoard(color.getOtherColor()));
						if (otherSide > 1) {
							// the other side has material that could block
							// the
							// flight squares.
							// TODO maybe implement an approach that does
							// the
							// worst moves for one side ;)
							result = true;
						} else {
							// the other side has no material that could
							// block
							// the flight squares - draw.
							result = false;
						}
					}
				}
			} else {
				result = true;
			}
		} else {
			result = true;
		}
		return result;
	}

}
