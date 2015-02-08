package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class Knight extends Piece {

	public Knight(final Color color) {
		super('N', color);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final Board<Piece, Color, ChessPly> board, final int x,
			final int y, final boolean checkSaveness) {

		checkPieceAndAddPly(plies, board, x, y, x + 1, y + 2);
		checkPieceAndAddPly(plies, board, x, y, x + 1, y - 2);
		checkPieceAndAddPly(plies, board, x, y, x - 1, y + 2);
		checkPieceAndAddPly(plies, board, x, y, x - 1, y - 2);

		checkPieceAndAddPly(plies, board, x, y, x + 2, y + 1);
		checkPieceAndAddPly(plies, board, x, y, x - 2, y + 1);
		checkPieceAndAddPly(plies, board, x, y, x + 2, y - 1);
		checkPieceAndAddPly(plies, board, x, y, x - 2, y - 1);

	}

}
