package de.neuenberger.ai.impl.chess.engine.movesorting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.ChessPlyMScoreComparator;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.plies.RatedPly;

public class NullMoveSortingStrategy extends AbstractMoveSortingStrategy {

	Comparator<ChessPly> chessPlyComparator = new ChessPlyMScoreComparator();
	Comparator<RatedPly> ratedPlyComparator = new RatedPlyComparator();

	public NullMoveSortingStrategy() {

	}

	@Override
	public List<ChessPly> sortMoves(final ChessBoard chessBoard, final Color colorToMove, final List<ChessPly> moves) {
		final List<ChessPly> result;
		result = new LinkedList<>();
		final List<ChessPly> movesForNullMovePruning = new LinkedList<>();
		final List<ChessPly> simpleScoredMoves = new ArrayList<>(moves.size());
		for (final ChessPly chessPly : moves) {
			if (chessPly.getMoveDeltaScore() > 0) {
				simpleScoredMoves.add(chessPly);
			} else {
				movesForNullMovePruning.add(chessPly);
			}
		}
		Collections.sort(simpleScoredMoves, chessPlyComparator);
		result.addAll(simpleScoredMoves);

		final List<RatedPly> scoredMoves = new ArrayList<>(movesForNullMovePruning.size());
		for (final ChessPly chessPly : movesForNullMovePruning) {
			final ChessBoard board = chessBoard.apply(chessPly);
			final List<ChessPly> possiblePlies = board.getPossiblePlies(colorToMove);
			Integer moveScore = 0;
			for (final ChessPly chessPly2 : possiblePlies) {
				moveScore += chessPly2.getMoveDeltaScore();
			}
			scoredMoves.add(new RatedPly(chessPly, moveScore));
		}
		Collections.sort(scoredMoves, ratedPlyComparator);
		addScoreMoves(result, scoredMoves);
		return result;
	}

	private static void addScoreMoves(final List<ChessPly> result, final Collection<RatedPly> scoredMoves) {
		for (final RatedPly ratedPly : scoredMoves) {
			result.add(ratedPly.getChessPly());
		}
	}
}
