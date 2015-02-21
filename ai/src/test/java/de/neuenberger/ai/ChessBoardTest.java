package de.neuenberger.ai;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class ChessBoardTest {

	private ChessBoard chessBoard;
	private ChessBoardFactory chessBoardFactory;

	@Before
	public void before() {
		chessBoardFactory = new ChessBoardFactory();
		chessBoard = chessBoardFactory.createInitalSetup();
	}

	@Test
	public void testInitialSetup() {
		final String string = chessBoard.toString();

		Assertions.assertThat(string).startsWith("rnbqkbnr\npppppppp\n");
		Assertions.assertThat(string).endsWith("PPPPPPPP\nRNBQKBNR\nWHITE to move");
	}

	@Test
	public void testCoordinatesValidity() {
		assertThat(chessBoard.checkCoordinatesValid(-1, -1)).isFalse();
	}

	@Test
	public void testMoves() {
		final List<ChessPly> possiblePlies = chessBoard.getPossiblePlies(Color.WHITE);
		assertThat(possiblePlies).hasSize(20);
	}

	@Test
	public void testSufficientMaterialWith2Knights() {
		final ChessBoard board = chessBoardFactory.setupByFEN("6k1/8/7K/8/4NN2/8/8/8 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}

	@Test
	public void testSufficientMaterialWith1Rook() {
		final ChessBoard board = chessBoardFactory.setupByFEN("2k5/8/2K5/8/8/8/8/4R3 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}

	@Test
	public void testSufficientMaterialWith1Queen() {
		final ChessBoard board = chessBoardFactory.setupByFEN("2k5/8/2K5/8/8/8/8/4Q3 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}

	@Test
	public void testSufficientMaterialWithKnightAndBishop() {
		final ChessBoard board = chessBoardFactory.setupByFEN("2k5/8/2K5/8/8/8/8/4NB2 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}

	@Test
	public void testSufficientMaterialWithKnightVsKnight() {
		final ChessBoard board = chessBoardFactory.setupByFEN("7k/5K1n/8/4N3/8/8/8/8 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isTrue();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}

	@Test
	public void testSufficientMaterialWithPairOfBishops() {
		final ChessBoard board = chessBoardFactory.setupByFEN("5K1k/8/8/8/8/3B4/3B4/8 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}

	@Test
	public void testSufficientMaterialWithBishopsOnDarkSquares() throws Exception {
		final ChessBoard board = chessBoardFactory.setupByFEN("5K1k/8/8/8/8/4B3/3B4/8 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isFalse();
	}

	@Test
	public void testSufficientMaterialWithBishopsOnLightSquares() throws Exception {
		final ChessBoard board = chessBoardFactory.setupByFEN("5K1k/8/8/8/4B3/3B4/8/8 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isFalse();
	}

	@Test
	public void testSufficientMaterialWithOnePawn() {
		final ChessBoard board = chessBoardFactory.setupByFEN("5K1k/8/8/8/8/8/P7/8 w");
		assertThat(board.hasEnoughMatingMaterial(Color.BLACK)).isFalse();
		assertThat(board.hasEnoughMatingMaterial(Color.WHITE)).isTrue();
	}
}
