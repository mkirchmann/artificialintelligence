package de.neuenberger.ai.impl.chess.model.delegate;

import java.util.Set;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.Piece.PieceType;
import de.neuenberger.ai.impl.chess.model.PlyList;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardInstance;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;

public class IsInCheckUseBitBoard implements IsInCheckCalculationStrategy {

	BitBoardPreCalculations bitBoard = BitBoardPreCalculations.getInstance();
	RuntimeException exception = new RuntimeException();

	@Override
	public boolean isInCheck(final Color color, final ChessBoard chessBoard, final BitBoardInstance instance) {
		final int kingPos = bitBoard.binarySearchForIndex(instance.getPieceBitBoard(color, PieceType.KING));
		final Color opponentColor = color.getOtherColor();
		boolean result;
		if (kingPos >= 0) {
			final long opponentColorBitBoard = instance.getColorBitBoard(opponentColor);
			final long res = bitBoard.getAllPossibleAttackerPositions(kingPos) & opponentColorBitBoard;
			if (res == 0) {
				result = false;
			} else {
				boolean isAttacked = false;
				try {
					final Position kingPosition = bitBoard.getPosition(kingPos);
					final Set<Position> forIndex = bitBoard.getAllPossibleAttackerPositionsForIndex(kingPos);
					final PlyList tempList = new CheckerPlyList(kingPosition, exception);
					outer: for (final Position piecePosition : forIndex) {
						if ((piecePosition.getFieldBit() & res) != 0) {
							final Piece piece = chessBoard.getPieceAt(piecePosition);
							piece.addPossiblePlies(tempList, chessBoard, piecePosition, false);
						}
					}
				} catch (final RuntimeException ex) {
					if (ex == exception) {
						isAttacked = true;
					} else {
						throw ex;
					}
				}
				result = isAttacked;
			}
		} else {
			result = true;
		}
		return result;
	}

}
