package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.Piece.Color;

public interface BoardChanger {
	/**
	 * moves a piece from source coordinate to target coordinate
	 * 
	 * @param sourceX
	 *            source x coordinate
	 * @param sourceY
	 *            source y coordinate
	 * @param targetX
	 *            target x coordinate
	 * @param targetY
	 *            target y coordinate
	 */
	void movePiece(final int sourceX, final int sourceY, final int targetX, final int targetY);

	/**
	 * Sets a piece to the coordinates
	 * 
	 * @param targetX
	 *            target x coordinate
	 * @param targetY
	 *            target y coordinate
	 * @param piece
	 *            may be null or a piece implementation.
	 */
	void setPieceAt(final int targetX, final int targetY, final Piece piece);

	/**
	 * Set who has to move next. This also calculates if the king to be moved is
	 * in check. After this call, no more changes are accepted.
	 * 
	 * @param whosToMove
	 */
	void setWhosToMove(final Color whosToMove);
}
