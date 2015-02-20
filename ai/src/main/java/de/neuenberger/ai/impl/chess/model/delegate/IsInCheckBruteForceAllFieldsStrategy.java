package de.neuenberger.ai.impl.chess.model.delegate;

import java.util.Collection;
import java.util.Set;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.Piece.PieceType;
import de.neuenberger.ai.impl.chess.model.PlyList;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardInstance;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;

public class IsInCheckBruteForceAllFieldsStrategy implements IsInCheckCalculationStrategy {
	BitBoardPreCalculations bitBoard = BitBoardPreCalculations.getInstance();
	private final RuntimeException runtimeException = new RuntimeException();

	public boolean isAttackedByOpponent(final ChessBoard chessBoard, final Position target, final Color color) {
		// i.e. whether these can capture the target coordinates ...
		final Set<Position> positions = bitBoard.getAllPossibleAttackerPositions(target);
		final PlyList plyList = new CheckerPlyList(target, runtimeException);
		return checkPositionsForAttackOfTarget(chessBoard, target, color, positions, plyList);
	}

	/**
	 * @param chessBoard
	 * @param target
	 * @param color
	 * @param positions
	 * @param tempList
	 */
	public boolean checkPositionsForAttackOfTarget(final ChessBoard chessBoard, final Position target,
			final Color color, final Collection<Position> positions, final PlyList tempList) {
		boolean result = false;
		try {
			for (final Position position : positions) {
				final Piece piece = chessBoard.getPieceAt(position.getIdx());
				if (piece != null && piece.getColor() != color) {
					piece.addPossiblePlies(tempList, chessBoard, position, false);
				}
			}
		} catch (final RuntimeException ex) {
			if (ex == runtimeException) {
				result = true;
			} else {
				throw ex;
			}
		}
		return result;
	}

	@Override
	public boolean isInCheck(final Color color, final ChessBoard chessBoard, final BitBoardInstance bitBoardInstance) {
		final long king = bitBoardInstance.getPieceBitBoard(color, PieceType.KING);
		final Position position = bitBoard.binarySearch(king);
		if (position != null) {
			return isAttackedByOpponent(chessBoard, position, color);
		}
		return true;
	}
}
