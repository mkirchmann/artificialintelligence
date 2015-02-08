package de.neuenberger.ai.impl.chess.model;

import java.io.Serializable;
import java.util.List;

import de.neuenberger.ai.base.model.Board;

/**
 * A piece is immutable and can generate moves.
 * 
 * @author Michael
 * 
 */
public abstract class Piece implements Serializable {

	private final char representation;
	private final Color color;

	public enum Color {
		BLACK, WHITE;
	}

	public Piece(final char representation, final Color color) {
		this.representation = representation;
		this.color = color;
	}

	public abstract void addPossiblePlies(List<ChessPly> plies, Board<Piece, Color, ChessPly> board, int x, int y,
			boolean checkSaveness);

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

	protected boolean checkPieceAndAddPly(final List<ChessPly> plies, final Board<Piece, Color, ChessPly> board,
			final int sourceX, final int sourceY, final int newX, final int newY, final boolean checkOwnKingsafeness) {
		final boolean doBreak;
		if (board.checkCoordinatesValid(newX, newY)) {
			final Piece pieceAt = board.getPieceAt(newX, newY);
			if (pieceAt != null) {
				if (pieceAt.getColor() != getColor()) { // different color, it
														// is a
														// capture move.
					final ChessPly ply = new ChessPly(this, sourceX, sourceY, newX, newY, true, false);
					final Board<Piece, Color, ChessPly> targetBoard = board.apply(ply);
					if (!targetBoard.isInCheck(getColor())) {
						plies.add(ply);
					}
				}
				doBreak = true;
			} else {
				final ChessPly ply = new ChessPly(this, sourceX, sourceY, newX, newY, false, false);
				final boolean valid;
				if (checkOwnKingsafeness) {
					final Board<Piece, Color, ChessPly> targetBoard = board.apply(ply);
					valid = !targetBoard.isInCheck(getColor());
				} else {
					valid = true;
				}
				if (valid) {
					plies.add(ply);
				}
				doBreak = false;
			}
		} else {
			doBreak = true;
		}
		return doBreak;
	}
}
