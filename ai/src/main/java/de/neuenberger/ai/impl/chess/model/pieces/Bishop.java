package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class Bishop extends Piece {

	public Bishop(final Color color) {
		super('B', color);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final Board<Piece, Color, ChessPly> board, final int x,
			final int y, final boolean checkSaveness) {

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x + i, y + 1, false);
			if (doBreak) {
				break;
			}
		}

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x - i, y - 1, false);
			if (doBreak) {
				break;
			}
		}

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x + i, y - 1, false);
			if (doBreak) {
				break;
			}
		}

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x - i, y + 1, false);
			if (doBreak) {
				break;
			}
		}

	}

}
