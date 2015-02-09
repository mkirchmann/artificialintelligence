package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class Knight extends Piece {

	public Knight(final Color color) {
		super('N', color, 30);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {

		checkPieceAndAddPly(plies, board, x, y, x + 1, y + 2, false);
		checkPieceAndAddPly(plies, board, x, y, x + 1, y - 2, false);
		checkPieceAndAddPly(plies, board, x, y, x - 1, y + 2, false);
		checkPieceAndAddPly(plies, board, x, y, x - 1, y - 2, false);

		checkPieceAndAddPly(plies, board, x, y, x + 2, y + 1, false);
		checkPieceAndAddPly(plies, board, x, y, x - 2, y + 1, false);
		checkPieceAndAddPly(plies, board, x, y, x + 2, y - 1, false);
		checkPieceAndAddPly(plies, board, x, y, x - 2, y - 1, false);

	}

}
