package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public class TerminationScorePlyResult extends PlyResult<TerminationScore> {

	public TerminationScorePlyResult(final TerminationScore terminationScore, final ChessBoard board) {
		super(terminationScore, board);
	}

	@Override
	public int compareTo(final PlyResult o) {
		int result = score.compare(o.getScore());
		if (result == 0) {
			if (getScore() == TerminationScore.MATED) {
				result = Integer.valueOf(getPlyCount()).compareTo(o.getPlyCount());
			} else if (getScore() == TerminationScore.MATE) {
				result = Integer.valueOf(o.getPlyCount()).compareTo(getPlyCount());
			} else {
				result = 0;
			}
		}
		return result;
	}

	@Override
	public int getScoreEquivalent() {
		return getScore().getScoreEquivalent();
	}

}
