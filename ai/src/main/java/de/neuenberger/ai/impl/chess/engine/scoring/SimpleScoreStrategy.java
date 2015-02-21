package de.neuenberger.ai.impl.chess.engine.scoring;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public class SimpleScoreStrategy implements ScoreStrategy {

	@Override
	public int getScore(final ChessBoard chessBoard) {
		return chessBoard.getScore();
	}

}
