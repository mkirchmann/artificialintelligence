package de.neuenberger.ai.impl.chess.engine.scoring;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public interface ScoreStrategy {
	public int getScore(ChessBoard chessBoard);
}
