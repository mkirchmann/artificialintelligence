package de.neuenberger.ai.impl.chess.model.delegate;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardBuilder;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.bitboard.BitBoardPreCalculations;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;
import de.neuenberger.ai.impl.chess.model.pieces.Bishop;
import de.neuenberger.ai.impl.chess.model.pieces.King;
import de.neuenberger.ai.impl.chess.model.pieces.Knight;
import de.neuenberger.ai.impl.chess.model.pieces.Pawn;
import de.neuenberger.ai.impl.chess.model.pieces.Queen;
import de.neuenberger.ai.impl.chess.model.pieces.Rook;

public class IsInCheckCalculationStrategyTest {
	private IsInCheckCalculationStrategy bruteForce;
	private IsInCheckCalculationStrategy useBitBoard;
	private final BitBoardPreCalculations instance = BitBoardPreCalculations.getInstance();
	private final Piece blackKing = new King(Color.BLACK);
	private final Piece whiteKing = new King(Color.WHITE);
	private final Piece pieces[] = new Piece[] { new Knight(Color.WHITE), new Bishop(Color.WHITE),
			new Pawn(Color.WHITE), new Queen(Color.WHITE), new Rook(Color.WHITE), new Knight(Color.BLACK),
			new Bishop(Color.BLACK), new Pawn(Color.BLACK), new Queen(Color.BLACK), new Rook(Color.BLACK) };

	@Before
	public void before() {
		bruteForce = new IsInCheckBruteForceAllFieldsStrategy();
		useBitBoard = new IsInCheckUseBitBoard();
	}

	@Test
	public void testCheckForAllPieces() {
		for (int i = 0; i < 63; i++) {
			final Position blackKingPosition = instance.getPosition(i);
			Position whiteKingPosition;
			if (i < 20) {
				whiteKingPosition = instance.getPosition(63 - i);
			} else {
				whiteKingPosition = instance.getPosition(i % 8);
			}
			final ChessBoardBuilder boardBuilder = new ChessBoardBuilder().putPiece(blackKingPosition, blackKing)
					.putPiece(whiteKingPosition, whiteKing);

			for (int j = 0; j < 63; j++) {
				final Position piecePosition = instance.getPosition(j);
				if (j == i || j == whiteKingPosition.getIdx()) {
					continue;
				}
				for (final Piece piece : pieces) {
					final ChessBoard chessBoard = boardBuilder.putPiece(piecePosition, piece).getBoard();

					final boolean whiteInCheckBruteforce = bruteForce.isInCheck(Color.WHITE, chessBoard,
							chessBoard.getBitBoardInstance());
					final boolean whiteInCheckBitBoard = useBitBoard.isInCheck(Color.WHITE, chessBoard,
							chessBoard.getBitBoardInstance());
					final boolean blackInCheckBruteforce = bruteForce.isInCheck(Color.BLACK, chessBoard,
							chessBoard.getBitBoardInstance());
					final boolean blackInCheckBitBoard = useBitBoard.isInCheck(Color.BLACK, chessBoard,
							chessBoard.getBitBoardInstance());

					Assertions.assertThat(whiteInCheckBitBoard)
							.describedAs(chessBoard.toString() + "\n" + chessBoard.getBitBoardInstance().debugInfo())
							.isEqualTo(whiteInCheckBruteforce);
					Assertions.assertThat(blackInCheckBitBoard)
							.describedAs(chessBoard.toString() + "\n" + chessBoard.getBitBoardInstance().debugInfo())
							.isEqualTo(blackInCheckBruteforce);
				}
			}
		}
	}

	@Test
	public void testBruteForce() {
		testPerformanceForStrategy(bruteForce);
	}

	@Test
	public void testBitBoard() {
		testPerformanceForStrategy(useBitBoard);
	}

	void testPerformanceForStrategy(final IsInCheckCalculationStrategy strategy) {
		for (int i = 0; i < 63; i++) {
			final Position blackKingPosition = instance.getPosition(i);
			Position whiteKingPosition;
			if (i < 20) {
				whiteKingPosition = instance.getPosition(63 - i);
			} else {
				whiteKingPosition = instance.getPosition(i % 8);
			}
			final ChessBoardBuilder boardBuilder = new ChessBoardBuilder().putPiece(blackKingPosition, blackKing)
					.putPiece(whiteKingPosition, whiteKing);

			for (int j = 0; j < 63; j++) {
				final Position piecePosition = instance.getPosition(j);
				if (j == i || j == whiteKingPosition.getIdx()) {
					continue;
				}
				for (final Piece piece : pieces) {
					final ChessBoard chessBoard = boardBuilder.putPiece(piecePosition, piece).getBoard();

					final boolean whiteInCheckBruteforce = strategy.isInCheck(Color.WHITE, chessBoard,
							chessBoard.getBitBoardInstance());
					final boolean whiteInCheckBitBoard = strategy.isInCheck(Color.WHITE, chessBoard,
							chessBoard.getBitBoardInstance());

				}
			}
		}
	}

}
