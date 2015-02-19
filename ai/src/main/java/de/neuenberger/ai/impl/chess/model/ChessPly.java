package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.pieces.Pawn;

/**
 * A ply is immutable.
 * 
 * @author Michael
 * 
 */
public class ChessPly implements ChessBoardModifier {
	private final Position source;
	private final Position target;
	private final boolean capture;
	private boolean check;
	private final Piece piece;
	private final Piece capturedPiece;
	private Integer moveDeltaScore;

	public ChessPly(final Piece piece, final Position source, final Position target, final Piece capturedPiece,
			final boolean check) {
		this.piece = piece;
		this.source = source;
		this.target = target;
		this.capturedPiece = capturedPiece;
		this.capture = capturedPiece != null;
		this.check = check;
	}

	/**
	 * @return the source
	 */
	public Position getSource() {
		return source;
	}

	/**
	 * @return the target
	 */
	public Position getTarget() {
		return target;
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
		boardChanger.movePiece(source, target, capturedPiece);
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
		builder.append(source);
		if (isCapture()) {
			builder.append('x');
		} else {
			builder.append('-');
		}
		builder.append(target);
		appendPromotionInfo(builder);
		if (isCheck()) {
			builder.append("+");
		}
		return builder.toString();
	}

	protected void appendPromotionInfo(final StringBuilder builder) {
		// do nothing in base implementation. This is only interesting for
		// promotion.

	}

	/**
	 * @param check
	 *            the check to set
	 */
	public void setCheck(final boolean check) {
		this.check = check;
	}

	/**
	 * @return the capturedPiece
	 */
	public Piece getCapturedPiece() {
		return capturedPiece;
	}

	public Integer getMoveDeltaScore() {
		if (moveDeltaScore == null) {
			moveDeltaScore = calculatedMoveDeltaScore();
		}
		return moveDeltaScore;
	}

	protected int calculatedMoveDeltaScore() {
		int result = 0;
		if (check) {
			result += 5;
		}
		if (capture) {
			result += capturedPiece.getSimpleScore() - piece.getSimpleScore();
		}
		return result;
	}

}
