package de.neuenberger.ai.impl.chess.model.piece.mover;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.BitBoard;
import de.neuenberger.ai.impl.chess.model.BitBoard.Position;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class StraightPlyFactory extends BasePiecePlyFactory {
	public StraightPlyFactory(final Piece piece) {
		super(piece);
	}

	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final BitBoard.Position source,
			final boolean checkSaveness) {
		List<Position> list = board.getTopVertical(source);
		for (final Position position : list) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, source, position, checkSaveness);
			if (doBreak) {
				break;
			}
		}

		list = board.getBottomVertical(source);
		for (final Position position : list) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, source, position, checkSaveness);
			if (doBreak) {
				break;
			}
		}

		list = board.getLeftHorizontal(source);
		for (final Position position : list) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, source, position, checkSaveness);
			if (doBreak) {
				break;
			}
		}

		list = board.getRightHorizontal(source);
		for (final Position position : list) {
			final boolean doBreak = checkPieceAndAddPly(plies, board, source, position, checkSaveness);
			if (doBreak) {
				break;
			}
		}
	}
}
