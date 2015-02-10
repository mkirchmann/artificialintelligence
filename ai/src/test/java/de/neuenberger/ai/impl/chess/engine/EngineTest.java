package de.neuenberger.ai.impl.chess.engine;

import java.util.List;
import java.util.regex.Pattern;

import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;

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
		Assertions.assertThat(bestMove.toString()).isEqualTo("(#) f7-f8N");
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
		Assertions.assertThat(bestMove.toString()).isEqualTo("(#) Nd6-f7");
	}

	@Test
	public void testNoMovesWhenMated() {
		final String fen = "8/8/8/8/8/5k2/6q1/7K w";
		noMoveWhenMated(fen, Color.WHITE);
	}

	@Test
	public void testDefensiveBestMove() {
		final String fen = "7k/8/8/8/4q3/8/5bPP/7K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, Color.WHITE, 2);
		final PlyResult bestMove = engine.getBestMove();

		Assertions.assertThat(bestMove.toString()).matches(Pattern.compile("\\(\\d{0,9}\\) h2-h3.*"));
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

	@Test
	public void testBestMoveMateIn2() {
		final String fen = "5k2/5p1p/6p1/6B1/8/8/8/4R2K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		Assertions.assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, Color.WHITE, 2);
		final PlyResult bestMove = engine.getBestMove();

		Assertions.assertThat(bestMove.toString()).isEqualTo("(#) Bh3 Kg8 Rf8");
	}

	@Test
	public void testBestMoveMateIn2_variant2() {
		final String fen = "5k2/5p1p/6p1/6B1/8/8/8/3R3K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		Assertions.assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, Color.WHITE, 2);
		final PlyResult bestMove = engine.getBestMove();

		Assertions.assertThat(bestMove.toString()).matches(Pattern.compile("(#) Bg6-f6 .{4,5} Rf1-f8"));
	}

	@Ignore
	@Test
	public void testMateIn3() {
		final String fen = "1kr3q1/ppp5/5N2/8/8/2R1Q3/8/7K w";

		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		Assertions.assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, Color.WHITE, 4);
		final PlyResult bestMove = engine.getBestMove();

		Assertions.assertThat(bestMove.toString()).matches(Pattern.compile("(#) Nf6-d7 Kb8-a8 Qe3xa7 Ka8xa7 Rc3-a3"));
	}

	@Test
	public void testBestMoveTakeQueenNotRook() {
		final String fen = "1r6/8/1R6/4k1BK/3q4/8/8/7B w";

		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		Assertions.assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, Color.WHITE, 2);

		final PlyResult bestMove = engine.getBestMove();

		Assertions.assertThat(bestMove.toString()).matches(Pattern.compile("\\(\\d{0,9}\\) Bg5-f6 Ke5-f5 Bf6xd4"));
	}
}
