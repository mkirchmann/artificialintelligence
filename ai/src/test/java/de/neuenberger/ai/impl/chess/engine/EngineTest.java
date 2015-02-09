package de.neuenberger.ai.impl.chess.engine;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.engine.ChessEngine.PlyResult;
import de.neuenberger.ai.impl.chess.engine.ChessEngine.SpecialScore;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class EngineTest {
	ChessBoardFactory factory = new ChessBoardFactory();
	final ChessEngine engine = new ChessEngine();

	@Test
	public void testBestMove() {
		final ChessBoard setupByFEN = factory.setupByFEN("6nn/5Prk/6pp/8/8/8/8/7K");
		System.out.println(setupByFEN);
		final PlyResult bestMove = engine.getBestMove(setupByFEN, Color.WHITE, 2);

		System.out.println(bestMove);
		Assertions.assertThat(bestMove.getScore()).isSameAs(SpecialScore.MATE);
	}

	@Test
	public void testNoMovesWhenMated() {
		final ChessBoard setupByFEN = factory.setupByFEN("8/8/8/8/8/5k2/6q1/7K w");
		final PlyResult bestMove = engine.getBestMove(setupByFEN, Color.WHITE, 2);
		Assertions.assertThat(bestMove.getScore()).isSameAs(SpecialScore.MATE);
	}

	@Test
	public void testStalemate() {
		final ChessBoard stalematePosition = factory.setupByFEN("8/8/8/8/8/4k3/5q2/7K w");
		final PlyResult bestMove = engine.getBestMove(stalematePosition, Color.WHITE, 1);

		Assertions.assertThat(stalematePosition.isCheck()).isFalse();
		Assertions.assertThat(bestMove.getScore()).isEqualTo(0);
	}
}
