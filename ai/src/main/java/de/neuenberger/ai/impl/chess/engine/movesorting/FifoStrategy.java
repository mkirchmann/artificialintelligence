package de.neuenberger.ai.impl.chess.engine.movesorting;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class FifoStrategy extends AbstractMoveSortingStrategy {

	@Override
	public List<ChessPly> sortMoves(final ChessBoard chessBoard, final Color colorToMove, final List<ChessPly> moves) {
		return moves;
	}

}
