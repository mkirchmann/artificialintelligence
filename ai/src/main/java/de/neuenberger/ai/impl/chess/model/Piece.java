package de.neuenberger.ai.impl.chess.model;

import java.io.Serializable;
import java.util.List;

/**
 * A piece is immutable and can generate moves.
 * 
 * @author Michael
 * 
 */
public abstract class Piece implements Serializable {

	private final char representation;
	private final Color color;
	private final int simpleScore;

	public enum Color {
		BLACK, WHITE;

		/**
		 * @return the nextColor
		 */
		public Color getOtherColor() {
			if (this == BLACK) {
				return WHITE;
			} else {
				return BLACK;
			}
		}
	}

	public Piece(final char representation, final Color color, final int simpleScore) {
		this.representation = representation;
		this.color = color;
		this.simpleScore = simpleScore;
	}

	public abstract void addPossiblePlies(List<ChessPly> plies, ChessBoard board, int x, int y, boolean checkSaveness);

	@Override
	public String toString() {
		String result;
		if (color == Color.BLACK) {
			result = ("" + representation).toLowerCase();
		} else {
			result = "" + representation;
		}
		return result;
	}

	/**
	 * @return the representation
	 */
	public char getRepresentation() {
		return representation;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	protected boolean checkPieceAndAddPly(final List<ChessPly> plies, final ChessBoard board, final int sourceX,
			final int sourceY, final int newX, final int newY, final boolean checkOwnKingsafeness) {
		final boolean doBreak;
		if (board.checkCoordinatesValid(newX, newY)) {
			final Piece pieceAt = board.getPieceAt(newX, newY);
			if (pieceAt != null) {
				if (pieceAt.getColor() != getColor()) { // different color, it
														// is a
														// capture move.
					final ChessPly ply = new ChessPly(this, sourceX, sourceY, newX, newY, true, false);
					checkValidityOfPlyAndAdd(plies, board, checkOwnKingsafeness, ply);
				}
				doBreak = true;
			} else {
				final ChessPly ply = new ChessPly(this, sourceX, sourceY, newX, newY, false, false);
				checkValidityOfPlyAndAdd(plies, board, checkOwnKingsafeness, ply);
				doBreak = false;
			}
		} else {
			doBreak = true;
		}
		return doBreak;
	}

	private void checkValidityOfPlyAndAdd(final List<ChessPly> plies, final ChessBoard board,
			final boolean checkOwnKingsafeness, final ChessPly ply) {
		final boolean add;
		if (checkOwnKingsafeness) {
			final ChessBoard targetBoard = board.apply(ply);
			add = !targetBoard.isInCheck(getColor());
			ply.setCheck(targetBoard.isInCheck(getColor().getOtherColor()));
		} else {
			add = true;
		}
		if (add) {
			plies.add(ply);
		}
	}

	/**
	 * @return the simpleScore
	 */
	public int getSimpleScore() {
		return simpleScore;
	}
}
