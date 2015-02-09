package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.base.model.Ply;

public interface ChessBoardModifier extends Ply {
	void applyTo(final BoardChanger boardChanger);

	void applyWhosToMove(BoardChanger boardChanger);
}
