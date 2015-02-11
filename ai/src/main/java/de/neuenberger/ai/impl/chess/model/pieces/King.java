package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

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
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		// TODO castling

		// simple move.
		factory.checkPieceAndAddPly(plies, board, x, y, x + 1, y + 1, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x + 1, y, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x + 1, y - 1, checkSaveness);

		factory.checkPieceAndAddPly(plies, board, x, y, x, y + 1, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x, y - 1, checkSaveness);

		factory.checkPieceAndAddPly(plies, board, x, y, x - 1, y + 1, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x - 1, y, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x - 1, y - 1, checkSaveness);

	}

}
