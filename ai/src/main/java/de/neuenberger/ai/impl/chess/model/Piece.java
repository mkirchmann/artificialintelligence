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

	/**
	 * @return the simpleScore
	 */
	public int getSimpleScore() {
		return simpleScore;
	}

	public boolean isWhite() {
		return getColor() == Color.WHITE;
	}

	public abstract String getUnicode();
}
