package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public class IntegerScorePlyResult extends PlyResult<Integer> {

	public IntegerScorePlyResult(final Integer score, final ChessBoard board) {
		super(score, board);
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
