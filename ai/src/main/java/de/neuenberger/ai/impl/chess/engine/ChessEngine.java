package de.neuenberger.ai.impl.chess.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4jFactory;

import de.neuenberger.ai.impl.chess.engine.movesorting.FifoStrategy;
import de.neuenberger.ai.impl.chess.engine.movesorting.MoveSortingStrategy;
import de.neuenberger.ai.impl.chess.engine.movesorting.NullMoveSortingStrategy;
import de.neuenberger.ai.impl.chess.engine.scoring.ScoreStrategy;
import de.neuenberger.ai.impl.chess.engine.scoring.SimpleScoreStrategy;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessEngine {
	private final Color seekBestMoveFor;
	private final int recursions;
	private final ChessBoard board;

	Log log = Log4jFactory.getLog(getClass());

	MoveSortingStrategy moveSortingStrategy;
	private final ScoreStrategy scoreStrategy;

	public ChessEngine(final ChessBoard board, final int recursions) {
		this(buildMoveSortingStrategy(), selectScoringStrategy(board), board, board.getWhosToMove(), recursions);
	}

	public ChessEngine(final ChessBoard board, final Color seekBestMoveFor, final int recursions) {
		this(buildMoveSortingStrategy(), selectScoringStrategy(board), board, seekBestMoveFor, recursions);
	}

	private static ScoreStrategy selectScoringStrategy(final ChessBoard board2) {
		return new SimpleScoreStrategy();
	}

	private static MoveSortingStrategy buildMoveSortingStrategy() {
		final NullMoveSortingStrategy strat1 = new NullMoveSortingStrategy();
		final NullMoveSortingStrategy stratOpp = new NullMoveSortingStrategy();
		strat1.setNextMoveSortingStrategy(stratOpp);

		final FifoStrategy fifoStrategy = new FifoStrategy();
		stratOpp.setNextMoveSortingStrategy(fifoStrategy);
		fifoStrategy.setNextMoveSortingStrategy(fifoStrategy);

		return strat1;
	}

	public ChessEngine(final MoveSortingStrategy moveSortingStrategy, final ScoreStrategy scoreStrategy,
			final ChessBoard board, final Color seekBestMoveFor, final int recursions) {
		this.moveSortingStrategy = moveSortingStrategy;
		this.scoreStrategy = scoreStrategy;
		this.board = board;
		this.seekBestMoveFor = seekBestMoveFor;
		this.recursions = recursions;
	}

	public PlyResult getBestMove() {
		final List<PlyResult> recordMoves = new ArrayList<>(50);
		final PlyResult result = getBestMove(board, seekBestMoveFor, recursions, recordMoves, moveSortingStrategy,
				null, null);
		return result;
	}

	public PlyResult getBestMoveIterativeDeepening(final int deepeningRecursions) {
		final List<PlyResult> plies = getBestMoveIterativeDeepening(deepeningRecursions, board);
		Collections.sort(plies, Collections.reverseOrder());
		if (plies.size() > 0) {
			return plies.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Iterative deepening
	 * 
	 * @param deepeningRecursions
	 * @param board
	 * @return Returns a list of the rated moves.
	 */
	protected List<PlyResult> getBestMoveIterativeDeepening(final int deepeningRecursions, final ChessBoard board) {
		final List<PlyResult> recordMoves = new ArrayList<>(50);
		PlyResult bestMove = getBestMove(board, seekBestMoveFor, recursions, recordMoves, moveSortingStrategy, null,
				null);
		int idx = 0;
		final List<PlyResult> result = new ArrayList<>(50);
		if (deepeningRecursions <= 0) {
			result.addAll(recordMoves);
		} else {
			final int checkMoves;
			if (recordMoves.size() < 4) {
				checkMoves = recordMoves.size();
			} else {
				checkMoves = recordMoves.size() / 4;
			}
			// sort moves.
			Collections.sort(recordMoves, Collections.reverseOrder());

			for (final PlyResult plyResult : recordMoves) {
				// only the best moves are checked
				if (idx > checkMoves) {
					// just add the move to the result.
					result.add(plyResult);
				} else {
					if (plyResult.getPlyCount() < 2) {
						// cant go any deeper.
						result.add(plyResult);
					} else {
						final ChessPly move0 = plyResult.getPly(0);
						final ChessPly move1 = plyResult.getPly(1);
						final ChessBoard newBoard = board.apply(move0).apply(move1);

						final List<PlyResult> iterativeDeepening = getBestMoveIterativeDeepening(
								deepeningRecursions - 1, newBoard);
						bestMove = null;
						for (final PlyResult plyResult2 : iterativeDeepening) {
							plyResult2.insertPly(move1);
							plyResult2.insertPly(move0);
							if (bestMove == null) {
								bestMove = plyResult2;
							} else if (bestMove.compareTo(plyResult2) < 0) {
								bestMove = plyResult2;
							}
						}
						if (bestMove == null) {
							result.add(plyResult);
						} else {
							result.add(bestMove);
						}
					}
				}
				idx++;
			}
		}
		return result;
	}

	protected PlyResult getBestMove(final ChessBoard board, final Color color, final int recursions,
			final List<PlyResult> recordMoves, final MoveSortingStrategy moveSortingStrategy, PlyResult alpha,
			PlyResult beta) {
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
		} else if (!board.hasEnoughMatingMaterial()) {
			result = new TerminationScorePlyResult(TerminationScore.NOMATINGMATERIAL, board);
		} else if (recursions <= 0) {
			final int score = board.getScore();
			result = new IntegerScorePlyResult(score, board);
		} else {

			final List<ChessPly> sortedMoves = moveSortingStrategy.sortMoves(board, color, plies);

			for (final ChessPly chessPly : sortedMoves) {
				final PlyResult currentPlyResult;

				final ChessBoard apply = board.apply(chessPly);

				currentPlyResult = getBestMove(apply, color.getOtherColor(), recursions - 1, null,
						moveSortingStrategy.getNextMoveSortingStrategy(), alpha, beta);
				currentPlyResult.insertPly(chessPly);
				final PlyResult currentScore = currentPlyResult;
				if (recordMoves != null) {
					recordMoves.add(currentScore);
					// record top level move calculation results.
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
