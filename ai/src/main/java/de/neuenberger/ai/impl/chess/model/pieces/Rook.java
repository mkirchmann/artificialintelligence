package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.piece.mover.StraightPlyFactory;

public class Rook extends Piece {

	private final StraightPlyFactory factory;

	public Rook(final Color color) {
		super('R', color, 50);
		factory = new StraightPlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		factory.addPossiblePlies(plies, board, x, y, checkSaveness);
	}

}
