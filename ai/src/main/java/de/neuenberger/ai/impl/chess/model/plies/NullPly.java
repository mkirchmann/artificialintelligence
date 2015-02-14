package de.neuenberger.ai.impl.chess.model.plies;

import de.neuenberger.ai.impl.chess.model.BoardChanger;
import de.neuenberger.ai.impl.chess.model.ChessBoardModifier;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class NullPly implements ChessBoardModifier {

	private final Color nextToMove;

	public NullPly(final Color nextToMove) {
		this.nextToMove = nextToMove;
	}

	@Override
	public void applyTo(final BoardChanger boardChanger) {
		// do nothing, it is a null move.

	}

	@Override
	public void applyWhosToMove(final BoardChanger boardChanger) {
		boardChanger.setWhosToMove(nextToMove);

	}

}
