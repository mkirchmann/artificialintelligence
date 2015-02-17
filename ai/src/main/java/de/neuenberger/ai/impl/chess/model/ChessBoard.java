package de.neuenberger.ai.impl.chess.model;

import java.util.LinkedList;
import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.BitBoard.Position;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessBoard implements Board<Piece, Color, ChessPly> {

	private final Piece[] boardContents;
	private ChessPly lastPly;
	private boolean check;
	private Color whosToMove;
	private final BitBoard instance;

	public ChessBoard() {
		boardContents = new Piece[64];
		instance = BitBoard.getInstance();
	}

	@Override
	public List<ChessPly> getPossiblePlies(final Color color) {
		final List<ChessPly> result = new LinkedList<>();
		for (int i = 0; i < 64; i++) {
			final Piece piece = boardContents[i];
			if (piece != null && piece.getColor() == color) {
				piece.addPossiblePlies(result, this, instance.getPosition(i), true);
			}
		}
		return result;
	}

	@Override
	public ChessBoard clone() {
		final ChessBoard result = new ChessBoard();

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
		return new BoardChangerImpl(this);
	}

	public boolean isAttackedByOpponent(final Position target, final Color color) {
		// TODO improve by checking only rook, bishop , knight and king moves,
		// i.e. whether these can capture the target coordinates ...
		final List<ChessPly> tempList = new LinkedList<>();
		for (int i = 0; i < 64; i++) {
			final Piece piece = boardContents[i];
			if (piece != null && piece.getColor() != color) {
				piece.addPossiblePlies(tempList, this, instance.getPosition(i), false);
				for (final ChessPly ply : tempList) {
					if (ply.getTarget() == target) {
						return true;
					}
				}
				tempList.clear();
			}
		}
		return false;
	}

	@Override
	public boolean checkCoordinatesValid(final int newX, final int newY) {
		return getMinX() <= newX && newX <= getMaxX() && getMinY() <= newY && newY <= getMaxY();
	}

	@Override
	public boolean isInCheck(final Color color) {
		for (int i = 0; i < 64; i++) {
			final Piece piece = boardContents[i];
			if (piece != null && piece.getRepresentation() == 'K' && piece.getColor() == color) {
				// found king.
				return isAttackedByOpponent(instance.getPosition(i), color);
			}
		}
		return false;
	}

	@Override
	public ChessPly getLastPly() {
		return lastPly;
	}

	private void movePiece(final BitBoard.Position source, final BitBoard.Position target) {
		final Piece piece = boardContents[source.getIdx()];
		boardContents[source.getIdx()] = null;
		boardContents[target.getIdx()] = piece;
	}

	private void setPieceAt(final BitBoard.Position target, final Piece piece) {
		boardContents[target.getIdx()] = piece;
	}

	public static class BoardChangerImpl implements BoardChanger {
		boolean valid = true;

		final ChessBoard chessBoard;

		public void invalidate() {
			valid = false;
		}

		public BoardChangerImpl(final ChessBoard chessBoard) {
			this.chessBoard = chessBoard;
		}

		@Override
		public void movePiece(final BitBoard.Position source, final BitBoard.Position target) {
			checkValid();
			chessBoard.movePiece(source, target);
		}

		@Override
		public void setPieceAt(final BitBoard.Position target, final Piece piece) {
			checkValid();
			chessBoard.setPieceAt(target, piece);
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
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getUpperLeftDiagonal(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getUpperLeftDiagonal(final Position position) {
		return instance.getUpperLeftDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getUpperRightDiagonal(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getUpperRightDiagonal(final Position position) {
		return instance.getUpperRightDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getLowerLeftDiagonal(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getLowerLeftDiagonal(final Position position) {
		return instance.getLowerLeftDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getLowerRightDiagonal(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getLowerRightDiagonal(final Position position) {
		return instance.getLowerRightDiagonal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getTopVertical(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getTopVertical(final Position position) {
		return instance.getTopVertical(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getBottomVertical(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getBottomVertical(final Position position) {
		return instance.getBottomVertical(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getLeftHorizontal(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getLeftHorizontal(final Position position) {
		return instance.getLeftHorizontal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getRightHorizontal(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getRightHorizontal(final Position position) {
		return instance.getRightHorizontal(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getKingNormalFields(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getKingNormalFields(final Position position) {
		return instance.getKingNormalFields(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getKnightMoves(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getKnightMoves(final Position position) {
		return instance.getKnightMoves(position);
	}

	public Position fromPosition(final int x, final int y) {
		return instance.fromZeroBasedCoordinates(x, y);
	}

	public Piece getPieceAt(final int i) {
		return boardContents[i];
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getBlackPawnNormalMoves(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getBlackPawnNormalMoves(final Position position) {
		return instance.getBlackPawnNormalMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getWhitePawnNormalMoves(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getWhitePawnNormalMoves(final Position position) {
		return instance.getWhitePawnNormalMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getBlackPawnCaptureMoves(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getBlackPawnCaptureMoves(final Position position) {
		return instance.getBlackPawnCaptureMoves(position);
	}

	/**
	 * @param position
	 * @return
	 * @see de.neuenberger.ai.impl.chess.model.BitBoard#getWhitePawnCaptureMoves(de.neuenberger.ai.impl.chess.model.BitBoard.Position)
	 */
	public List<Position> getWhitePawnCaptureMoves(final Position position) {
		return instance.getWhitePawnCaptureMoves(position);
	}

}
