package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.Piece.Color;

public interface BoardChanger {
	/**
	 * moves a piece from source coordinate to target coordinate
	 * 
	 * @param source
	 *            source coordinate
	 * @param target
	 *            target coordinate
	 */
	void movePiece(final BitBoard.Position source, final BitBoard.Position target);

	/**
	 * Sets a piece to the coordinates
	 * 
	 * @param target
	 *            target coordinate
	 * @param piece
	 *            may be null or a piece implementation.
	 */
	void setPieceAt(final BitBoard.Position target, final Piece piece);

	/**
	 * Set who has to move next. This also calculates if the king to be moved is
	 * in check. After this call, no more changes are accepted.
	 * 
	 * @param whosToMove
	 */
	void setWhosToMove(final Color whosToMove);

}
