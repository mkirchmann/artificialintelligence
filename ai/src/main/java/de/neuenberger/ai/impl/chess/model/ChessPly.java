package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.pieces.Pawn;

/**
 * A ply is immutable.
 * 
 * @author Michael
 * 
 */
public class ChessPly implements ChessBoardModifier {
	private final int sourceX;
	private final int sourceY;
	private final int targetX;
	private final int targetY;
	private final boolean capture;
	private boolean check;
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
	@Override
	public void applyTo(final BoardChanger boardChanger) {
		boardChanger.movePiece(sourceX, sourceY, targetX, targetY);
	}

	@Override
	public void applyWhosToMove(final BoardChanger boardChanger) {
		boardChanger.setWhosToMove(piece.getColor().getOtherColor());
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		if (piece instanceof Pawn) {
			// do nothing
		} else {
			builder.append(piece.getRepresentation());
		}
		builder.append((char) ('a' + sourceX));
		builder.append((char) ('1' + sourceY));
		if (isCapture()) {
			builder.append('x');
		} else {
			builder.append('-');
		}
		builder.append((char) ('a' + targetX));
		builder.append((char) ('1' + targetY));
		return builder.toString();
	}

	/**
	 * @param check
	 *            the check to set
	 */
	public void setCheck(final boolean check) {
		this.check = check;
	}
}
