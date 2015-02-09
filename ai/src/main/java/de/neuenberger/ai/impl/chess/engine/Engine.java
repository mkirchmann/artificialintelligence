package de.neuenberger.ai.impl.chess.engine;

import java.util.LinkedList;
import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class Engine {
	ChessScore chessScore = new ChessScore();

	public PlyResult getBestMove(final ChessBoard board, final Color color, final int recursions) {
		PlyResult result = null;

		final List<ChessPly> plies = board.getPossiblePlies(color);

		final int maxScore = 0;

		for (final ChessPly chessPly : plies) {
			if (result == null) {
				result = getScore(board, color, recursions, chessPly);
			} else {
				final PlyResult currentScore = getScore(board, color, recursions, chessPly);
				if (currentScore.getScore() > maxScore) {
					result = currentScore;
				}
			}
		}
		return result;
	}

	public PlyResult getScore(final ChessBoard board, final Color color, final int recursions, final ChessPly chessPly) {
		final PlyResult result;
		if (recursions <= 0) {
			final int score = chessScore.getBoardScore(color, board);
			result = new PlyResult(score);
		} else {
			final ChessBoard apply = board.apply(chessPly);

			result = getBestMove(apply, color.getOtherColor(), recursions - 1);
		}
		return result;
	}

	static class PlyResult {
		final int score;

		public PlyResult(final int score) {
			this.score = score;
		}

		List<ChessPly> plies = new LinkedList<>();

		/**
		 * @return the score
		 */
		public int getScore() {
			return score;
		}

		public void insertPly(final ChessPly ply) {
			plies.add(0, ply);
		}
	}
}
