package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.piece.mover.BasePiecePlyFactory;

public class Knight extends Piece {

	private final BasePiecePlyFactory factory;

	public Knight(final Color color) {
		super(PieceType.KNIGHT, color);
		factory = new BasePiecePlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final Position source,
			final boolean checkSaveness) {

		final List<Position> list = board.getKnightMoves(source);
		for (final Position position : list) {
			factory.checkPieceAndAddPly(plies, board, source, position, checkSaveness);
		}
	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2658" : "\u265E";
	}

}
