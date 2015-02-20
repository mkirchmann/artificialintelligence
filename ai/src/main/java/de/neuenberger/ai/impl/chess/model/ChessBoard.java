package de.neuenberger.ai.impl.chess.model;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardInstance;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.delegate.IsInCheckBruteForceAllFieldsStrategy;
import de.neuenberger.ai.impl.chess.model.delegate.IsInCheckCalculationStrategy;

public class ChessBoard implements Board<Piece, Color, ChessPly> {

	private final Piece[] boardContents;
	private ChessPly lastPly;
	private boolean check;
	private Color whosToMove = Color.WHITE;
	private final BitBoardPreCalculations bitBoard;
	private final BitBoardInstance bitBoardInstance;
	// static IsInCheckCalculationStrategy isInCheckUseBitBoardStrategy = new
	// IsInCheckUseBitBoard();
	static IsInCheckCalculationStrategy isInCheckUseBitBoardStrategy = new IsInCheckBruteForceAllFieldsStrategy();

	Logger log = LoggerFactory.getLogger(getClass());

	public ChessBoard() {
		this(new BitBoardInstance(BitBoardPreCalculations.getInstance()));
	}

	public ChessBoard(final BitBoardInstance bitBoardInstance) {
		this.bitBoardInstance = bitBoardInstance;
		boardContents = new Piece[64];
		bitBoard = BitBoardPreCalculations.getInstance();
	}

	@Override
	public List<ChessPly> getPossiblePlies(final Color color) {
		final PlyList plyList = new BasePlyList();
		for (int i = 0; i < 64; i++) {
			final Piece piece = boardContents[i];
			if (piece != null && piece.getColor() == color) {
				piece.addPossiblePlies(plyList, this, bitBoard.getPosition(i), true);
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
		return bitBoard.getUpperLeftDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getUpperRightDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getUpperRightDiagonal(final Position position) {
		return bitBoard.getUpperRightDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getLowerLeftDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getLowerLeftDiagonal(final Position position) {
		return bitBoard.getLowerLeftDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getLowerRightDiagonal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getLowerRightDiagonal(final Position position) {
		return bitBoard.getLowerRightDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getTopVertical(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getTopVertical(final Position position) {
		return bitBoard.getTopVertical(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getBottomVertical(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getBottomVertical(final Position position) {
		return bitBoard.getBottomVertical(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getLeftHorizontal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getLeftHorizontal(final Position position) {
		return bitBoard.getLeftHorizontal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getRightHorizontal(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getRightHorizontal(final Position position) {
		return bitBoard.getRightHorizontal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getKingNormalFields(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getKingNormalFields(final Position position) {
		return bitBoard.getKingNormalFields(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getKnightMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getKnightMoves(final Position position) {
		return bitBoard.getKnightMoves(position);
	}

	public Position fromPosition(final int x, final int y) {
		return bitBoard.fromZeroBasedCoordinates(x, y);
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
		return bitBoard.getBlackPawnNormalMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getWhitePawnNormalMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getWhitePawnNormalMoves(final Position position) {
		return bitBoard.getWhitePawnNormalMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getBlackPawnCaptureMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getBlackPawnCaptureMoves(final Position position) {
		return bitBoard.getBlackPawnCaptureMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations#getWhitePawnCaptureMoves(de.neuenberger.ai.impl.chess.model.BitBoardPreCalculations.Position)
	 */
	public List<Position> getWhitePawnCaptureMoves(final Position position) {
		return bitBoard.getWhitePawnCaptureMoves(position);
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

}
