package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessScore {
	public int getBoardScore(final ChessBoard board) {
		int ownScore = 0;
		int opponentScore = 0;
		for (int x = board.getMinX(); x <= board.getMaxX(); x++) {
			for (int y = board.getMinY(); y <= board.getMaxY(); y++) {
				final Piece piece = board.getPieceAt(x, y);
				if (piece != null) {
					if (piece.getColor() == Color.WHITE) {
						ownScore += piece.getSimpleScore();
					} else {
						opponentScore += piece.getSimpleScore();
					}
				}
			}
		}
		return ownScore - opponentScore;
	}
}
