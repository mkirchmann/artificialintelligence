package de.neuenberger.ai.impl.chess.engine.movesorting;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

@RunWith(MockitoJUnitRunner.class)
public class NullMoveSortingStrategyTest {
	private NullMoveSortingStrategy strategy;

	@Mock
	private ChessBoard chessBoard;

	@Before
	public void before() {
		strategy = new NullMoveSortingStrategy();

	}

	@Test
	public void testMoveOrder() throws Exception {
		final Color black = Color.BLACK;
		final ChessPly ply100 = createPly(100);
		final ChessPly ply101 = createPly(101);
		final ChessPly ply90 = createPly(90);
		final ChessPly ply103 = createPly(103);
		final ChessPly ply70 = createPly(70);
		final ChessPly noAdvantage1 = createPly(0);
		final ChessPly noAdvantage2 = createPly(0);
		final ChessPly noAdvantage3 = createPly(0);
		final ChessPly noAdvantage4 = createPly(0);

		final ChessPly createPlyA = createPly(10);
		final ChessPly createPlyB = createPly(0);
		final ChessPly createPlyC = createPly(30);

		final ChessBoard board1 = Mockito.mock(ChessBoard.class);
		final ChessBoard board2 = Mockito.mock(ChessBoard.class);
		final ChessBoard board3 = Mockito.mock(ChessBoard.class);
		final ChessBoard board4 = Mockito.mock(ChessBoard.class);
		Mockito.when(chessBoard.apply(noAdvantage1)).thenReturn(board1);
		Mockito.when(chessBoard.apply(noAdvantage2)).thenReturn(board2);
		Mockito.when(chessBoard.apply(noAdvantage3)).thenReturn(board3);
		Mockito.when(chessBoard.apply(noAdvantage4)).thenReturn(board4);

		final List<ChessPly> initialMoveList = Arrays.asList(ply100, ply90, ply101, ply70, ply103, noAdvantage1,
				noAdvantage2, noAdvantage3, noAdvantage4);

		Mockito.when(board1.getPossiblePlies(black)).thenReturn(Arrays.asList(createPlyA, createPlyB, createPlyC));
		Mockito.when(board2.getPossiblePlies(black)).thenReturn(Collections.EMPTY_LIST);
		Mockito.when(board3.getPossiblePlies(black)).thenReturn(Arrays.asList(createPlyB, createPlyC));
		Mockito.when(board4.getPossiblePlies(black)).thenReturn(Arrays.asList(createPlyA));

		// execute
		final List<ChessPly> result = strategy.sortMoves(chessBoard, black, initialMoveList);

		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).containsExactly(ply103, ply101, ply100, ply90, ply70, noAdvantage1, noAdvantage3,
				noAdvantage4, noAdvantage2);

	}

	private ChessPly createPly(final int score) {
		final ChessPly mock = Mockito.mock(ChessPly.class);
		Mockito.when(mock.getMoveDeltaScore()).thenReturn(score);
		return mock;
	}
}
