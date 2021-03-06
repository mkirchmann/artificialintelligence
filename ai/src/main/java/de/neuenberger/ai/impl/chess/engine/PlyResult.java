package de.neuenberger.ai.impl.chess.engine;

import java.util.ArrayList;
import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;

public abstract class PlyResult<ScoreType> implements Comparable<PlyResult> {
	ScoreType score;
	private final ChessBoard targetBoard;

	public PlyResult(final ScoreType score, final ChessBoard targetBoard) {
		this.score = score;
		this.targetBoard = targetBoard;
	}

	List<ChessPly> plies = new ArrayList<>(50);

	/**
	 * @return the score
	 */
	public ScoreType getScore() {
		return score;
	}

	/**
	 * Inserts the given ply to this result at the first position
	 * 
	 * @param ply
	 *            given ply
	 */
	public void insertPly(final ChessPly ply) {
		plies.add(0, ply);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(" + score + ")");
		appendPlies(builder);
		return builder.toString();
	}

	public abstract int getScoreEquivalent();

	/**
	 * @return the targetBoard
	 */
	public ChessBoard getTargetBoard() {
		return targetBoard;
	}

	/**
	 * 
	 * @return Return number of plies.
	 */
	public int getPlyCount() {
		return plies.size();
	}

	public String toMovesString() {
		final StringBuilder builder = new StringBuilder();
		appendPlies(builder);
		return builder.toString();
	}

	public void appendPlies(final StringBuilder builder) {
		for (final ChessPly ply : plies) {
			if (builder.length() > 0) {
				builder.append(" ");
			}
			builder.append(ply);
		}
	}

	public ChessPly getPly(final int i) {
		return plies.get(i);
	}

}