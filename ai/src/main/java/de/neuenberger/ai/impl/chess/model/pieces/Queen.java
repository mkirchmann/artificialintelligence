package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class Queen extends Piece {

	Piece bishop;
	Piece rook;

	public Queen(final Color color) {
		super('Q', color, 83);
		bishop = new Bishop(color);
		rook = new Rook(color);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		bishop.addPossiblePlies(plies, board, x, y, checkSaveness);
		rook.addPossiblePlies(plies, board, x, y, checkSaveness);
	}

}
