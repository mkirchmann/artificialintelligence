package de.neuenberger.ai.impl.chess.engine;

import java.util.LinkedList;
import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessPly;

public abstract class PlyResult<ScoreType> implements Comparable<PlyResult> {
	ScoreType score;

	// TODO split to two classes for each score.
	public PlyResult(final ScoreType score) {
		this.score = score;
	}

	public abstract void negate();

	List<ChessPly> plies = new LinkedList<>();

	/**
	 * @return the score
	 */
	public ScoreType getScore() {
		return score;
	}

	public void insertPly(final ChessPly ply) {
		plies.add(0, ply);
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("(" + score + ")");
		for (final ChessPly ply : plies) {
			builder.append(" ");
			builder.append(ply);
		}
		return builder.toString();
	}

	public abstract int getScoreEquivalent();

}