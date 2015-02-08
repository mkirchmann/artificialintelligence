package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.base.model.Ply;
import de.neuenberger.ai.impl.chess.model.ChessBoard.BoardChangerImpl;

/**
 * A ply is immutable.
 * 
 * @author Michael
 * 
 */
public class ChessPly implements Ply {
	private final int sourceX;
	private final int sourceY;
	private final int targetX;
	private final int targetY;
	private final boolean capture;
	private final boolean check;
	private final Piece piece;

	public ChessPly(final Piece piece, final int sourceX, final int sourceY, final int targetX, final int targetY,
			final boolean capture, final boolean check) {
		this.piece = piece;
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.targetX = targetX;
		this.targetY = targetY;
		this.capture = capture;
		this.check = check;
	}

	/**
	 * @return the sourceX
	 */
	public int getSourceX() {
		return sourceX;
	}

	/**
	 * @return the sourceY
	 */
	public int getSourceY() {
		return sourceY;
	}

	/**
	 * @return the targetX
	 */
	public int getTargetX() {
		return targetX;
	}

	/**
	 * @return the targetY
	 */
	public int getTargetY() {
		return targetY;
	}

	/**
	 * @return the capture
	 */
	public boolean isCapture() {
		return capture;
	}

	/**
	 * @return the check
	 */
	public boolean isCheck() {
		return check;
	}

	/**
	 * @return the piece
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * 
	 * @param boardChanger
	 *            the board changer. This reference may not be kept. It must
	 *            only be used within this call.
	 */
	public void applyTo(final BoardChangerImpl boardChanger) {
		boardChanger.movePiece(sourceX, sourceY, targetX, targetY);
	}
}
