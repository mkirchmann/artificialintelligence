package de.neuenberger.ai.impl.chess.engine;

public class IntegerScorePlyResult extends PlyResult<Integer> {

	public IntegerScorePlyResult(final Integer score) {
		super(score);
	}

	@Override
	public void negate() {
		score = -((Integer) score);
	}

	@Override
	public int getScoreEquivalent() {
		return getScore();
	}

	@Override
	public int compareTo(final PlyResult result) {
		return getScore().compareTo(result.getScoreEquivalent());
	}

}
