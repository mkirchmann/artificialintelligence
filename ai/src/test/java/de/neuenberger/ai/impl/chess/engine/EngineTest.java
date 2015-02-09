package de.neuenberger.ai.impl.chess.engine;

import org.junit.Test;

import de.neuenberger.ai.impl.chess.engine.ChessEngine.PlyResult;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class EngineTest {
	@Test
	public void testBestMove() {
		final ChessBoard setupByFEN = new ChessBoardFactory().setupByFEN("6nn/5Prk/6pp/8/8/8/8/7K");
		System.out.println(setupByFEN);
		final ChessEngine engine = new ChessEngine();
		final PlyResult bestMove = engine.getBestMove(setupByFEN, Color.WHITE, 2);

		System.out.println(bestMove);
	}
}
