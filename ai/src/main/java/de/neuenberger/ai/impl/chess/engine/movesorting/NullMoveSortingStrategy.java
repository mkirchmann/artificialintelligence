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
import de.neuenberger.ai.impl.chess.model.Piece.PieceType;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardInstance;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations;
import de.neuenberger.ai.impl.chess.model.plies.RatedPly;

public class NullMoveSortingStrategy extends AbstractMoveSortingStrategy {

	Comparator<ChessPly> chessPlyComparator = new ChessPlyMScoreComparator();
	Comparator<RatedPly> ratedPlyComparator = new RatedPlyComparator();
	private final BitBoardPreCalculations instance;

	public NullMoveSortingStrategy() {
		this(BitBoardPreCalculations.getInstance());
	}

	public NullMoveSortingStrategy(final BitBoardPreCalculations instance) {
		this.instance = instance;
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

		if (movesForNullMovePruning.size() > 1) {
			final Color opponentColor = colorToMove.getOtherColor();
			final BitBoardInstance currentBoardBitBoard = chessBoard.getBitBoardInstance();
			final long pawnDefendedFields = currentBoardBitBoard.getPawnDefendedFields(opponentColor);
			final List<RatedPly> scoredMoves = new ArrayList<>(movesForNullMovePruning.size());
			final long opponentKnights = currentBoardBitBoard.getPieceBitBoard(opponentColor, PieceType.KNIGHT);
			final Boolean attackedByRorB[] = new Boolean[64];
			final Boolean aboutToBeAttackedByRorB[] = new Boolean[64];
			for (final ChessPly chessPly : movesForNullMovePruning) {
				final ChessBoard board = chessBoard.apply(chessPly);
				final List<ChessPly> possiblePlies = board.getPossiblePlies(colorToMove);
				Integer moveScore = 0;
				if (chessPly.getPieceType() != PieceType.PAWN) {
					final int pieceCentiPawns = chessPly.getPiece().getCentiPawns();
					final int halfPieceCentiPawns = chessPly.getPiece().getHalfCentiPawns();
					if ((chessPly.getTargetFieldBit() & pawnDefendedFields) != 0) {
						moveScore -= 100 + pieceCentiPawns;
					}
					if ((chessPly.getSourceFieldBit() & pawnDefendedFields) != 0) {
						moveScore += 100 + pieceCentiPawns;
					}

					if (opponentKnights != 0 && chessPly.getPieceType().ordinal() <= PieceType.ROOK.ordinal()) {
						// for Rook or more important piece
						final int sourceFieldIdx = chessPly.getSource().getIdx();
						if (attackedByRorB[sourceFieldIdx] == null) {
							final long sourceKnightMovesAttackers = instance.getKnightMoves(sourceFieldIdx);
							attackedByRorB[sourceFieldIdx] = ((sourceKnightMovesAttackers & opponentKnights) != 0);
						}
						if (aboutToBeAttackedByRorB[sourceFieldIdx] == null) {
							final long knightDistanceTwoForSource = instance.getKnightDistanceTwo(sourceFieldIdx);
							aboutToBeAttackedByRorB[sourceFieldIdx] = ((knightDistanceTwoForSource & opponentKnights) != 0);
						}
						final int targetFieldIdx = chessPly.getTarget().getIdx();
						if (attackedByRorB[targetFieldIdx] == null) {
							final long targetKnightMovesAttackers = instance.getKnightMoves(targetFieldIdx);
							attackedByRorB[targetFieldIdx] = ((targetKnightMovesAttackers & opponentKnights) != 0);
						}
						if (aboutToBeAttackedByRorB[targetFieldIdx] == null) {
							final long knightDistanceTwoForTarget = instance.getKnightDistanceTwo(targetFieldIdx);
							aboutToBeAttackedByRorB[targetFieldIdx] = ((knightDistanceTwoForTarget & opponentKnights) != 0);
						}

						if (attackedByRorB[sourceFieldIdx]) {
							// source field attacked by knight
							moveScore += pieceCentiPawns;
						}
						if (attackedByRorB[targetFieldIdx]) {
							// target field attacked by knight
							moveScore -= pieceCentiPawns;
						}

						if (aboutToBeAttackedByRorB[sourceFieldIdx]) {
							// source field can be attacked by knight in one
							// move
							moveScore += halfPieceCentiPawns;
						}
						if (aboutToBeAttackedByRorB[targetFieldIdx]) {
							// target field can be attacked by knight one move
							moveScore -= halfPieceCentiPawns;
						}
					}

				} else {

				}
				for (final ChessPly chessPly2 : possiblePlies) {
					moveScore += chessPly2.getMoveDeltaScore() + 1;
				}
				scoredMoves.add(new RatedPly(chessPly, moveScore));
			}
			Collections.sort(scoredMoves, ratedPlyComparator);
			addScoreMoves(result, scoredMoves);
		} else {
			result.addAll(movesForNullMovePruning);
		}
		return result;
	}

	private static void addScoreMoves(final List<ChessPly> result, final Collection<RatedPly> scoredMoves) {
		for (final RatedPly ratedPly : scoredMoves) {
			result.add(ratedPly.getChessPly());
		}
	}
}
