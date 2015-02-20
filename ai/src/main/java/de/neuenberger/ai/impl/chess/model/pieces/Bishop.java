package de.neuenberger.ai.impl.chess.model.pieces;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.PlyList;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.piece.mover.DiagonalPlyFactory;

public class Bishop extends Piece {

	private final DiagonalPlyFactory factory;

	public Bishop(final Color color) {
		super(PieceType.BISHOP, color);

		factory = new DiagonalPlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final PlyList plies, final ChessBoard board, final Position position,
			final boolean checkSaveness) {
		factory.addPossiblePlies(plies, board, position, checkSaveness);
	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2657" : "\u265D";
	}

}
