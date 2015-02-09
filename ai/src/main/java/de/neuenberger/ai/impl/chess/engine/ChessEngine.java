package de.neuenberger.ai.impl.chess.engine;

import java.util.LinkedList;
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
				result = new PlyResult(SpecialScore.MATED);
			} else {
				result = new PlyResult(SpecialScore.STALEMATE);
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
		if (color != seekBestMoveFor) {
			result.negate();
		}
		return result;
	}

	protected PlyResult getScore(final ChessBoard board, final Color color, final int recursions,
			final ChessPly chessPly) {
		final PlyResult result;
		if (recursions <= 0) {
			final int score = chessScore.getBoardScore(color, board);
			result = new PlyResult(score);
		} else {
			final ChessBoard apply = board.apply(chessPly);

			result = getBestMove(apply, color.getOtherColor(), recursions - 1);
		}
		result.insertPly(chessPly);
		return result;
	}

	static class PlyResult implements Comparable<PlyResult> {
		private Object score;

		// TODO split to two classes for each score.
		public PlyResult(final Integer score) {
			this.score = score;
		}

		public void negate() {
			if (score instanceof Integer) {
				score = -((Integer) score);
			} else if (score instanceof SpecialScore) {
				score = ((SpecialScore) score).negate();
			}
		}

		public PlyResult(final SpecialScore specialScore) {
			this.score = specialScore;
		}

		List<ChessPly> plies = new LinkedList<>();

		/**
		 * @return the score
		 */
		public Object getScore() {
			return score;
		}

		public void insertPly(final ChessPly ply) {
			plies.add(0, ply);
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			builder.append("(" + score + ")");
			for (final ChessPly ply : plies) {
				builder.append(" ");
				builder.append(ply);
			}
			return builder.toString();
		}

		@Override
		public int compareTo(final PlyResult o) {
			return compare(score, o.score);
		}

		public int compare(final Object o1, final Object o2) {
			if (o1 == null) {
				throw new IllegalArgumentException("o1=null.");
			}
			int result;

			if (o1 instanceof Integer) {
				if (o2 instanceof Integer) {
					result = ((Integer) o1).compareTo((Integer) o2);
				} else if (o2 instanceof SpecialScore) {
					result = -((SpecialScore) o2).compare(o1);
				} else {
					throw new IllegalArgumentException("Wrong type for compare: o2=" + o2);
				}
			} else if (o1 instanceof SpecialScore) {
				result = ((SpecialScore) o1).compare(o2);
			} else {
				throw new IllegalArgumentException("Wrong type for compare: o1=" + o1 + ", o2=" + o2);
			}

			return result;
		}
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
	}

}
