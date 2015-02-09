package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.Piece.Color;

public interface BoardChanger {
	void movePiece(final int sourceX, final int sourceY, final int targetX, final int targetY);

	void setPieceAt(final int targetX, final int targetY, final Piece piece);

	void setWhosToMove(final Color whosToMove);
}
