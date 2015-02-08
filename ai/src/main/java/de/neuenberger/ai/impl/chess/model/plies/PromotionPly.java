package de.neuenberger.ai.impl.chess.model.plies;

import de.neuenberger.ai.impl.chess.model.ChessBoard.BoardChangerImpl;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class PromotionPly extends ChessPly {

	private final Piece newPiece;

	public PromotionPly(final Piece piece, final int sourceX, final int sourceY, final int targetX, final int targetY,
			final Piece newPiece, final boolean capture, final boolean check) {
		super(piece, sourceX, sourceY, targetX, targetY, capture, check);
		this.newPiece = newPiece;
	}

	/**
	 * @return the newPiece
	 */
	public Piece getNewPiece() {
		return newPiece;
	}

	@Override
	public void applyTo(final BoardChangerImpl boardChanger) {
		super.applyTo(boardChanger);
		boardChanger.setPieceAt(getTargetX(), getTargetY(), newPiece);
	}

}
