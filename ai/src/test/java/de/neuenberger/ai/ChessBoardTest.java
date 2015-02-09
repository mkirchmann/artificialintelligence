package de.neuenberger.ai;

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

	@Before
	public void before() {
		chessBoard = new ChessBoardFactory().createInitalSetup();
	}

	@Test
	public void testInitialSetup() {
		final String string = chessBoard.toString();

		Assertions.assertThat(string).startsWith("rnbqkbnr\npppppppp\n");
		Assertions.assertThat(string).endsWith("PPPPPPPP\nRNBQKBNR\nWHITE to move");
	}

	@Test
	public void testCoordinatesValidity() {
		Assertions.assertThat(chessBoard.checkCoordinatesValid(-1, -1)).isFalse();
	}

	@Test
	public void testMoves() {
		final List<ChessPly> possiblePlies = chessBoard.getPossiblePlies(Color.WHITE);
		Assertions.assertThat(possiblePlies).hasSize(20);
	}

}
