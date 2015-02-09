package de.neuenberger.ai.impl.chess.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.pieces.Bishop;
import de.neuenberger.ai.impl.chess.model.pieces.King;
import de.neuenberger.ai.impl.chess.model.pieces.Knight;
import de.neuenberger.ai.impl.chess.model.pieces.Pawn;
import de.neuenberger.ai.impl.chess.model.pieces.Queen;
import de.neuenberger.ai.impl.chess.model.pieces.Rook;

public class ChessBoardFactory {

	public ChessBoardFactory() {

	}

	final ChessBoard chessBoard = new ChessBoard();

	public static class BoardInitialSetup implements ChessBoardModifier {

		@Override
		public void applyTo(final BoardChanger boardChanger) {
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

		@Override
		public void applyWhosToMove(final BoardChanger boardChanger) {
			boardChanger.setWhosToMove(Color.WHITE);
		}

	}

	private static class FENSetup implements ChessBoardModifier {

		private final String fen;
		private final Map<Character, Piece> pieceMap;
		private final String parameters;

		public FENSetup(final String fen, final String parameters) {
			this.fen = fen;
			this.parameters = parameters;

			final Map<Character, Piece> char2piece = new HashMap<>();
			char2piece.put('p', new Pawn(Color.BLACK));
			char2piece.put('P', new Pawn(Color.WHITE));
			char2piece.put('r', new Rook(Color.BLACK));
			char2piece.put('R', new Rook(Color.WHITE));
			char2piece.put('n', new Knight(Color.BLACK));
			char2piece.put('N', new Knight(Color.WHITE));
			char2piece.put('b', new Bishop(Color.BLACK));
			char2piece.put('B', new Bishop(Color.WHITE));
			char2piece.put('q', new Queen(Color.BLACK));
			char2piece.put('Q', new Queen(Color.WHITE));
			char2piece.put('K', new King(Color.WHITE));
			char2piece.put('k', new King(Color.BLACK));
			pieceMap = Collections.unmodifiableMap(char2piece);
		}

		@Override
		public void applyTo(final BoardChanger boardChanger) {
			final String[] split = fen.split("/");
			if (split.length != 8) {
				throw new IllegalArgumentException("Illegal fen: '" + fen + "'");
			}
			for (int y = 0; y < 8; y++) {
				final String string = split[7 - y];
				final char[] charArray = string.toCharArray();
				int x = 0;
				for (int i = 0; i < charArray.length; i++) {
					final char c = charArray[i];
					if ('1' <= c && c <= '9') {
						x += c - '1';
					} else {
						final Piece piece = pieceMap.get(c);

						if (piece == null) {
							throw new IllegalArgumentException("Illegal Character: '" + c + "'");
						}

						boardChanger.setPieceAt(x, y, piece);

					}
					x++;
				}
			}
		}

		@Override
		public void applyWhosToMove(final BoardChanger boardChanger) {
			Color whosTurnIsIt = Color.WHITE;

			if (parameters != null) {
				if (parameters.startsWith("b")) {
					whosTurnIsIt = Color.BLACK;
				} else if (parameters.startsWith("w")) {
					whosTurnIsIt = Color.WHITE;
				}
			}

			boardChanger.setWhosToMove(whosTurnIsIt);
		}

	}

	public ChessBoard setupByFEN(final String fen) {
		final int idxSpace = fen.indexOf(' ');
		if (idxSpace > 0) {
			return chessBoard.apply(new FENSetup(fen.substring(0, idxSpace), fen.substring(idxSpace).trim()));
		} else {
			return chessBoard.apply(new FENSetup(fen, null));
		}
	}

	public ChessBoard createInitalSetup() {

		return chessBoard.apply(new BoardInitialSetup());
	}
}
