package de.neuenberger.ai.impl.chess.engine;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessScore {
	public static int getBoardScore(final ChessBoard board) {
		int ownScore = 0;
		int opponentScore = 0;
		for (int i = 0; i < 64; i++) {
			final Piece piece = board.getPieceAt(i);
			if (piece != null) {
				if (piece.getColor() == Color.WHITE) {
					ownScore += piece.getSimpleScore();
				} else {
					opponentScore += piece.getSimpleScore();
				}
			}
		}
		return ownScore - opponentScore;
	}
}
