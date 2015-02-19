package de.neuenberger.ai.impl.chess.model.plies;

import de.neuenberger.ai.impl.chess.model.BoardChanger;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;

public class EnPassantPly extends ChessPly {

	private final Position otherPawn;

	public EnPassantPly(final Piece piece, final Position source, final Position target,
			final Position otherPawn, final Piece capture, final boolean check) {
		super(piece, source, target, capture, check);
		this.otherPawn = otherPawn;
	}

	@Override
	public void applyTo(final BoardChanger boardChanger) {
		super.applyTo(boardChanger);
		boardChanger.setPieceAt(otherPawn, null);
	}

	@Override
	public String toString() {
		return super.toString() + " e.p.";
	}

}
