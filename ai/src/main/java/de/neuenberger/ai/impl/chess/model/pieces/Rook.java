package de.neuenberger.ai.impl.chess.model.pieces;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.PlyList;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.piece.mover.StraightPlyFactory;

public class Rook extends Piece {

	private final StraightPlyFactory factory;

	public Rook(final Color color) {
		super(PieceType.ROOK, color);
		factory = new StraightPlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final PlyList plies, final ChessBoard board, final Position source,
			final boolean checkSaveness) {
		factory.addPossiblePlies(plies, board, source, checkSaveness);
	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2656" : "\u265C";
	}

}
