package de.neuenberger.ai.impl.chess.model.plies;

import de.neuenberger.ai.impl.chess.model.BoardChanger;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class EnPassantPly extends ChessPly {

	private final int otherPawnY;

	public EnPassantPly(final Piece piece, final int sourceX, final int sourceY, final int targetX, final int targetY,
			final int otherPawnY, final boolean capture, final boolean check) {
		super(piece, sourceX, sourceY, targetX, targetY, capture, check);
		this.otherPawnY = otherPawnY;
	}

	@Override
	public void applyTo(final BoardChanger boardChanger) {
		super.applyTo(boardChanger);
		boardChanger.setPieceAt(getTargetX(), otherPawnY, null);
	}

	@Override
	public String toString() {
		return super.toString() + " e.p.";
	}

}
