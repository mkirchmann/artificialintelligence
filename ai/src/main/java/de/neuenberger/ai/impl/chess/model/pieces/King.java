package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class King extends Piece {

	public King(final Color color) {
		super('K', color);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final Board<Piece, Color, ChessPly> board, final int x,
			final int y, final boolean checkSaveness) {
		// TODO castling

		// simple move.
		checkPieceAndAddPly(plies, board, x, y, x + 1, y + 1, false);
		checkPieceAndAddPly(plies, board, x, y, x + 1, y, false);
		checkPieceAndAddPly(plies, board, x, y, x + 1, y - 1, false);

		checkPieceAndAddPly(plies, board, x, y, x, y + 1, false);
		checkPieceAndAddPly(plies, board, x, y, x, y - 1, false);

		checkPieceAndAddPly(plies, board, x, y, x - 1, y + 1, false);
		checkPieceAndAddPly(plies, board, x, y, x - 1, y, false);
		checkPieceAndAddPly(plies, board, x, y, x - 1, y - 1, false);

	}

}
