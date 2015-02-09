package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class Rook extends Piece {

	public Rook(final Color color) {
		super('R', color, 50);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x + i, y, false);
			if (doBreak) {
				break;
			}
		}

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x - i, y, false);
			if (doBreak) {
				break;
			}
		}

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x, y - 1, false);
			if (doBreak) {
				break;
			}
		}

		for (int i = 1; i < 8; i++) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, x, y, x, y + 1, false);
			if (doBreak) {
				break;
			}
		}
	}

}
