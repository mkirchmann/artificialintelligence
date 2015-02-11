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
		final PlyResult result = getBestMove(board, seekBestMoveFor, recursions, true);
		return result;
	}

	protected PlyResult getBestMove(final ChessBoard board, final Color color, final int recursions,
			final boolean putLog) {
		PlyResult result = null;

		final List<ChessPly> plies = board.getPossiblePlies(color);

		if (plies.isEmpty()) {
			if (board.isCheck()) {
				result = new SpecialScorePlyResult(TerminationScore.MATE, board);
			} else {
				result = new SpecialScorePlyResult(TerminationScore.STALEMATE, board);
			}
		} else {
			for (final ChessPly chessPly : plies) {
				if (putLog) {
					log.info("about to check " + chessPly);
				}
				final PlyResult currentScore = getScore(board, color, recursions, chessPly);
				if (putLog) {
					log.info("checking move for " + color + " with " + recursions + " recursions resulted in "
							+ currentScore);
				}
				boolean use;
				if (result == null) {
					use = true;
				} else if (color == seekBestMoveFor && result.compareTo(currentScore) < 0) {
					use = true;
				} else if (color != seekBestMoveFor && result.compareTo(currentScore) > 0) {
					use = true;
				} else {
					use = false;
				}
				if (use) {
					result = currentScore;
				}
			}
		}

		return result;
	}

	protected PlyResult getScore(final ChessBoard board, final Color color, final int recursions,
			final ChessPly chessPly) {
		final PlyResult result;
		if (recursions <= 0) {
			final int score = chessScore.getBoardScore(board);
			result = new IntegerScorePlyResult(score, board);

		} else {
			final ChessBoard apply = board.apply(chessPly);

			result = getBestMove(apply, color.getOtherColor(), recursions - 1, false);
		}
		result.insertPly(chessPly);
		return result;
	}

}
