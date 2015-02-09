package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.base.model.Ply;
import de.neuenberger.ai.impl.chess.model.ChessBoard.BoardChangerImpl;

public interface ChessBoardModifier extends Ply {
	public void applyTo(final BoardChangerImpl boardChanger);
}
