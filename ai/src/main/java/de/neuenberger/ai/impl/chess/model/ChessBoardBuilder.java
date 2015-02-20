package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;

public class ChessBoardBuilder {
	private final ChessBoard board;

	public ChessBoardBuilder() {
		this(new ChessBoard());
	}

	public ChessBoardBuilder(final ChessBoard board) {
		this.board = board;
	}

	public ChessBoardBuilder putPiece(final Position position, final Piece piece) {
		final ChessBoardModifier putPieceModifier = new ChessBoardModifier() {

			@Override
			public void applyWhosToMove(final BoardChanger boardChanger) {
				boardChanger.setWhosToMove(board.getWhosToMove());

			}

			@Override
			public void applyTo(final BoardChanger boardChanger) {
				boardChanger.setPieceAt(position, piece);
			}
		};
		return new ChessBoardBuilder(board.apply(putPieceModifier));
	}

	public ChessBoardBuilder setWhosToMove(final Color color) {
		final ChessBoardModifier modifier = new ChessBoardModifier() {

			@Override
			public void applyWhosToMove(final BoardChanger boardChanger) {
				boardChanger.setWhosToMove(color);
			}

			@Override
			public void applyTo(final BoardChanger boardChanger) {

			}
		};
		return new ChessBoardBuilder(board.apply(modifier));
	}

	public ChessBoard getBoard() {
		return board;
	}
}
