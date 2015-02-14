package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.piece.mover.DiagonalPlyFactory;

public class Bishop extends Piece {

	private final DiagonalPlyFactory factory;

	public Bishop(final Color color) {
		super('B', color, 30);

		factory = new DiagonalPlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		factory.addPossiblePlies(plies, board, x, y, checkSaveness);
	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2657" : "\u265D";
	}

}
