package de.neuenberger.ai.impl.chess.engine;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessEngine {
	ChessScore chessScore = new ChessScore();
	private final Color seekBestMoveFor;
	private final int recursions;
	private final ChessBoard board;

	public ChessEngine(final ChessBoard board, final Color seekBestMoveFor, final int recursions) {
		this.board = board;
		this.seekBestMoveFor = seekBestMoveFor;
		this.recursions = recursions;
	}

	public PlyResult getBestMove() {
		return getBestMove(board, seekBestMoveFor, recursions);
	}

	protected PlyResult getBestMove(final ChessBoard board, final Color color, final int recursions) {
		PlyResult result = null;

		final List<ChessPly> plies = board.getPossiblePlies(color);

		if (plies.isEmpty()) {
			if (board.isCheck()) {
				result = new SpecialScorePlyResult(SpecialScore.MATE);
			} else {
				result = new SpecialScorePlyResult(SpecialScore.STALEMATE);
			}
			if (color == seekBestMoveFor) {
				result.negate();
			}
		} else {
			for (final ChessPly chessPly : plies) {
				if (result == null) {
					result = getScore(board, color, recursions, chessPly);
				} else {
					final PlyResult currentScore = getScore(board, color, recursions, chessPly);
					if (result.compareTo(currentScore) > 0) {
						result = currentScore;
					}
				}
			}
		}

		return result;
	}

	protected PlyResult getScore(final ChessBoard board, final Color color, final int recursions,
			final ChessPly chessPly) {
		final PlyResult result;
		if (recursions <= 0) {
			final int score = chessScore.getBoardScore(color, board);
			result = new IntegerScorePlyResult(score);
			if (color != seekBestMoveFor) {
				result.negate();
			}
		} else {
			final ChessBoard apply = board.apply(chessPly);

			result = getBestMove(apply, color.getOtherColor(), recursions - 1);
			result.negate();
		}
		result.insertPly(chessPly);
		return result;
	}

	public enum SpecialScore {
		STALEMATE(0, "Stalement"), MATE(Integer.MAX_VALUE, "#"), MATED(Integer.MIN_VALUE, "-#");

		private final Integer internalScore;
		private final String addition;

		SpecialScore(final Integer internalScore, final String addition) {
			this.internalScore = internalScore;
			this.addition = addition;
		}

		public SpecialScore negate() {
			if (this == MATE) {
				return MATED;
			} else if (this == MATED) {
				return MATE;
			}

			return this;
		}

		public int compare(final Object o) {
			int result;
			if (o instanceof Integer) {
				result = internalScore.compareTo((Integer) o);
			} else if (o instanceof SpecialScore) {
				result = internalScore.compareTo(((SpecialScore) o).internalScore);
			} else {
				throw new IllegalArgumentException();
			}
			return result;
		}

		@Override
		public String toString() {
			return addition;
		}

		public int getScoreEquivalent() {
			return internalScore;
		}
	}

}
