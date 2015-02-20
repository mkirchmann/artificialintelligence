package de.neuenberger.ai.impl.chess.model;

import java.io.Serializable;

import de.neuenberger.ai.impl.chess.model.bitboard.Position;

/**
 * A piece is immutable and can generate moves.
 * 
 * @author Michael
 * 
 */
public abstract class Piece implements Serializable {

	private final Color color;

	public enum Color {
		WHITE, BLACK;

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

	public enum PieceType {
		KING('K', 100000), QUEEN('Q', 83), ROOK('R', 50), BISHOP('B', 30), KNIGHT('N', 30), PAWN('P', 10);
		private final char representation;
		private final int simpleScore;

		PieceType(final char representation, final int simpleScore) {
			this.representation = representation;
			this.simpleScore = simpleScore;
		}

		/**
		 * @return the representation
		 */
		public char getRepresentation() {
			return representation;
		}

		/**
		 * @return the simpleScore
		 */
		public int getSimpleScore() {
			return simpleScore;
		}
	}

	private final PieceType pieceType;

	public Piece(final PieceType pieceType, final Color color) {
		this.pieceType = pieceType;
		this.color = color;
	}

	public abstract void addPossiblePlies(PlyList list, ChessBoard board, Position position, boolean checkSaveness);

	@Override
	public String toString() {
		String result;
		if (color == Color.BLACK) {
			result = ("" + getRepresentation()).toLowerCase();
		} else {
			result = "" + getRepresentation();
		}
		return result;
	}

	/**
	 * @return the representation
	 */
	public char getRepresentation() {
		return pieceType.getRepresentation();
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
		return pieceType.getSimpleScore();
	}

	public boolean isWhite() {
		return getColor() == Color.WHITE;
	}

	public abstract String getUnicode();

	public PieceType getPieceType() {
		return pieceType;
	}

}
