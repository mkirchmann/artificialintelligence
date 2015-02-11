package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public class SpecialScorePlyResult extends PlyResult<TerminationScore> {

	public SpecialScorePlyResult(final TerminationScore specialScore, final ChessBoard board) {
		super(specialScore, board);
	}

	@Override
	public int compareTo(final PlyResult o) {
		int result = score.compare(o.getScore());
		if (result == 0) {
			result = Integer.valueOf(getPlyCount()).compareTo(o.getPlyCount());
		}
		return result;
	}

	@Override
	public int getScoreEquivalent() {
		return getScore().getScoreEquivalent();
	}

}
