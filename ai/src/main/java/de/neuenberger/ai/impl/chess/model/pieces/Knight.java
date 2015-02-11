package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.piece.mover.BasePiecePlyFactory;

public class Knight extends Piece {

	private final BasePiecePlyFactory factory;

	public Knight(final Color color) {
		super('N', color, 30);
		factory = new BasePiecePlyFactory(this);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {

		factory.checkPieceAndAddPly(plies, board, x, y, x + 1, y + 2, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x + 1, y - 2, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x - 1, y + 2, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x - 1, y - 2, checkSaveness);

		factory.checkPieceAndAddPly(plies, board, x, y, x + 2, y + 1, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x - 2, y + 1, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x + 2, y - 1, checkSaveness);
		factory.checkPieceAndAddPly(plies, board, x, y, x - 2, y - 1, checkSaveness);

	}

}
