package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.engine.ChessEngine.SpecialScore;

public class SpecialScorePlyResult extends PlyResult<SpecialScore> {

	public SpecialScorePlyResult(final SpecialScore specialScore) {
		super(specialScore);
	}

	@Override
	public void negate() {
		score = score.negate();
	}

	@Override
	public int compareTo(final PlyResult o) {
		return score.compare(o.getScore());
	}

	@Override
	public int getScoreEquivalent() {
		return getScore().getScoreEquivalent();
	}

}
