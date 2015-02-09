package de.neuenberger.ai.impl.chess.engine;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.engine.ChessEngine.PlyResult;
import de.neuenberger.ai.impl.chess.engine.ChessEngine.SpecialScore;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class EngineTest {
	ChessBoardFactory factory = new ChessBoardFactory();

	@Test
	public void testBestMove() {
		final String fen = "6nn/5Prk/6pp/8/8/8/8/7K";
		final PlyResult bestMove = findBestMove(fen);

		System.out.println(bestMove);
		Assertions.assertThat(bestMove.getScore()).isSameAs(SpecialScore.MATE);
	}

	private PlyResult findBestMove(final String fen) {
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		System.out.println(setupByFEN);
		final ChessEngine engine = new ChessEngine(setupByFEN, Color.WHITE, 2);
		final PlyResult bestMove = engine.getBestMove();
		return bestMove;
	}

	@Test
	public void testBestMove2() {
		final PlyResult bestMove = findBestMove("6rk/6pp/3N4/8/2n5/2n5/8/K7 w");
		System.out.println(bestMove);
		Assertions.assertThat(bestMove.getScore()).isSameAs(SpecialScore.MATE);
	}

	@Test
	public void testNoMovesWhenMated() {
		final String fen = "8/8/8/8/8/5k2/6q1/7K w";
		noMoveWhenMated(fen, Color.WHITE);
	}

	private void noMoveWhenMated(final String fen, final Color color) {
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		Assertions.assertThat(setupByFEN.isCheck()).isTrue();
		final List<ChessPly> possiblePlies = setupByFEN.getPossiblePlies(color);
		Assertions.assertThat(possiblePlies).isEmpty();
		final ChessEngine engine = new ChessEngine(setupByFEN, color, 2);
		final PlyResult bestMove = engine.getBestMove();
		Assertions.assertThat(bestMove.getScore()).isSameAs(SpecialScore.MATED);
	}

	@Test
	public void testNoMoveWhenMated2() {
		final String fen = "5Nnn/6rk/6pp/8/8/8/8/7K b";
		noMoveWhenMated(fen, Color.BLACK);
	}

	@Test
	public void testStalemate() {
		final ChessBoard stalematePosition = factory.setupByFEN("8/8/8/8/8/4k3/5q2/7K w");
		final ChessEngine engine = new ChessEngine(stalematePosition, Color.WHITE, 1);
		final PlyResult bestMove = engine.getBestMove();

		Assertions.assertThat(stalematePosition.isCheck()).isFalse();
		Assertions.assertThat(bestMove.getScore()).isSameAs(SpecialScore.STALEMATE);
	}
}
