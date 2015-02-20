package de.neuenberger.ai.impl.chess.model.delegate;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardInstance;

public interface IsInCheckCalculationStrategy {
	public boolean isInCheck(final Color color, ChessBoard chessBoard, BitBoardInstance instance);
}
