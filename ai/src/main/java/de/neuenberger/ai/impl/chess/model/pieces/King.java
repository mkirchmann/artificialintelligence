package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.BitBoard;
import de.neuenberger.ai.impl.chess.model.BitBoard.Position;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.piece.mover.BasePiecePlyFactory;

public class King extends Piece {

	private final BasePiecePlyFactory factory;

	public King(final Color color) {
		super('K', color, 100000);
		factory = new BasePiecePlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final BitBoard.Position source,
			final boolean checkSaveness) {
		// TODO castling

		// simple move.
		final List<Position> list = board.getKingNormalFields(source);
		for (final Position position : list) {
			factory.checkPieceAndAddPly(plies, board, source, position, checkSaveness);
		}

	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2654" : "\u265A";
	}

}
