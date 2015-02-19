package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public class ChessScore {
	public static int getBoardScore(final ChessBoard board) {
		return board.getScore();
	}
}
