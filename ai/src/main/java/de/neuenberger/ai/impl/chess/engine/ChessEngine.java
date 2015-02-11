package de.neuenberger.ai.impl.chess.engine;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4jFactory;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessEngine {
	ChessScore chessScore = new ChessScore();
	private final Color seekBestMoveFor;
	private final int recursions;
	private final ChessBoard board;

	Log log = Log4jFactory.getLog(getClass());

	public ChessEngine(final ChessBoard board, final Color seekBestMoveFor, final int recursions) {
		this.board = board;
		this.seekBestMoveFor = seekBestMoveFor;
		this.recursions = recursions;
	}

	public PlyResult getBestMove() {
		final PlyResult result = getBestMove(board, seekBestMoveFor, recursions, true, null, null);
		return result;
	}

	protected PlyResult getBestMove(final ChessBoard board, final Color color, final int recursions,
			final boolean putLog, PlyResult alpha, PlyResult beta) {
		PlyResult result = null;

		final List<ChessPly> plies = board.getPossiblePlies(color);

		if (plies.isEmpty()) {
			if (board.isCheck()) {
				if (color == seekBestMoveFor) {
					result = new TerminationScorePlyResult(TerminationScore.MATED, board);
				} else {
					result = new TerminationScorePlyResult(TerminationScore.MATE, board);
				}
			} else {
				result = new TerminationScorePlyResult(TerminationScore.STALEMATE, board);
			}
		} else if (recursions <= 0) {
			final int score = chessScore.getBoardScore(board);
			result = new IntegerScorePlyResult(score, board);
		} else {
			for (final ChessPly chessPly : plies) {
				if (putLog) {
					log.info("about to check " + chessPly);
				}
				final PlyResult currentPlyResult;

				final ChessBoard apply = board.apply(chessPly);

				currentPlyResult = getBestMove(apply, color.getOtherColor(), recursions - 1, false, alpha, beta);
				currentPlyResult.insertPly(chessPly);
				final PlyResult currentScore = currentPlyResult;
				if (putLog) {
					log.info("checking move for " + color + " with " + recursions + " recursions resulted in "
							+ currentScore);
				}
				final boolean use;
				if (result == null) {
					result = currentScore;
				} else if (color == seekBestMoveFor) { // maximizing
					if (result.compareTo(currentScore) < 0) {
						result = currentScore;
					}
					if (alpha == null) {
						alpha = currentScore;
					} else if (alpha.compareTo(currentScore) < 0) {
						alpha = currentScore;
					}
				} else if (color != seekBestMoveFor) {
					if (result.compareTo(currentScore) > 0) { // minimizing
						result = currentScore;
					}
					if (beta == null) {
						beta = currentScore;
					} else {
						if (beta.compareTo(currentScore) > 0) {
							beta = currentScore;
						}
					}
				}
				if (beta != null && alpha != null && beta.compareTo(alpha) < 0) {
					break; // cut off.
				}
			}
		}

		return result;
	}

}
