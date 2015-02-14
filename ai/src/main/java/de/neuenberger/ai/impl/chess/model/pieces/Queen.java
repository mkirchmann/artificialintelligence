package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.piece.mover.DiagonalPlyFactory;
import de.neuenberger.ai.impl.chess.model.piece.mover.StraightPlyFactory;

public class Queen extends Piece {

	private final DiagonalPlyFactory diagonalPlyFactory;
	private final StraightPlyFactory straightPlyFactory;

	public Queen(final Color color) {
		super('Q', color, 83);
		diagonalPlyFactory = new DiagonalPlyFactory(this);
		straightPlyFactory = new StraightPlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		diagonalPlyFactory.addPossiblePlies(plies, board, x, y, checkSaveness);
		straightPlyFactory.addPossiblePlies(plies, board, x, y, checkSaveness);
	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2655" : "\u265B";
	}

}
