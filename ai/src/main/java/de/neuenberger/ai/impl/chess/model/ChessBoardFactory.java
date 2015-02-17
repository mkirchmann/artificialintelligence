package de.neuenberger.ai.impl.chess.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.neuenberger.ai.impl.chess.model.BitBoard.Position;
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

						final Position position = BitBoard.getInstance().fromZeroBasedCoordinates(x, y);
						boardChanger.setPieceAt(position, piece);

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
		return setupByFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w");
	}
}
