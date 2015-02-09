package de.neuenberger.ai.impl.chess.model;

import de.neuenberger.ai.impl.chess.model.ChessBoard.BoardChangerImpl;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.pieces.Bishop;
import de.neuenberger.ai.impl.chess.model.pieces.King;
import de.neuenberger.ai.impl.chess.model.pieces.Knight;
import de.neuenberger.ai.impl.chess.model.pieces.Pawn;
import de.neuenberger.ai.impl.chess.model.pieces.Queen;
import de.neuenberger.ai.impl.chess.model.pieces.Rook;

public class ChessBoardFactory {

	public static class BoardInitialSetup implements ChessBoardModifier {

		@Override
		public void applyTo(final BoardChangerImpl boardChanger) {
			final Pawn blackPawn = new Pawn(Color.BLACK);
			final Pawn whitePawn = new Pawn(Color.WHITE);
			for (int i = 0; i < 8; i++) {
				boardChanger.setPieceAt(i, 1, whitePawn);
				boardChanger.setPieceAt(i, 6, blackPawn);
			}

			final Rook blackRook = new Rook(Color.BLACK);
			final Rook whiteRook = new Rook(Color.WHITE);
			boardChanger.setPieceAt(0, 0, whiteRook);
			boardChanger.setPieceAt(7, 0, whiteRook);
			boardChanger.setPieceAt(0, 7, blackRook);
			boardChanger.setPieceAt(7, 7, blackRook);

			final Piece whiteKnight = new Knight(Color.WHITE);
			final Piece blackKnight = new Knight(Color.BLACK);
			boardChanger.setPieceAt(1, 0, whiteKnight);
			boardChanger.setPieceAt(6, 0, whiteKnight);
			boardChanger.setPieceAt(1, 7, blackKnight);
			boardChanger.setPieceAt(6, 7, blackKnight);

			final Piece whiteBishop = new Bishop(Color.WHITE);
			final Piece blackBishop = new Bishop(Color.BLACK);
			boardChanger.setPieceAt(2, 0, whiteBishop);
			boardChanger.setPieceAt(5, 0, whiteBishop);
			boardChanger.setPieceAt(2, 7, blackBishop);
			boardChanger.setPieceAt(5, 7, blackBishop);

			boardChanger.setPieceAt(3, 0, new Queen(Color.WHITE));
			boardChanger.setPieceAt(3, 7, new Queen(Color.BLACK));
			boardChanger.setPieceAt(4, 0, new King(Color.WHITE));
			boardChanger.setPieceAt(4, 7, new King(Color.BLACK));
		}

	}

	public ChessBoard createInitalSetup() {
		final ChessBoard chessBoard = new ChessBoard();
		return chessBoard.apply(new BoardInitialSetup());
	}
}
