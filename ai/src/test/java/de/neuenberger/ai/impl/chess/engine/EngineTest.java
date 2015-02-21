package de.neuenberger.ai.impl.chess.engine;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class EngineTest {
	ChessBoardFactory factory = new ChessBoardFactory();

	private static final String ANY_NUMERIC_RATING = "^\\(\\-{0,1}\\d{0,9}\\) ";

	@Test
	public void testBestMoveForUnderPromotionWithMate() {
		final String fen = "6nn/5Prk/6pp/8/8/8/8/7K";
		final PlyResult bestMove = findSimpleBestMove(fen);
		System.out.println(bestMove.getTargetBoard());
		System.out.println(bestMove);
		assertThat(bestMove.getScore()).isSameAs(TerminationScore.MATE);
		assertThat(bestMove.toString()).isEqualTo("(#) f7-f8N+");
	}

	private PlyResult findSimpleBestMove(final String fen) {
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		System.out.println(setupByFEN);
		final ChessEngine engine = new ChessEngine(setupByFEN, 2);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		return bestMove;
	}

	@Test
	public void testBestMoveForSmotheredMate() {
		final PlyResult bestMove = findSimpleBestMove("6rk/6pp/3N4/8/2n5/2n5/8/K7 w");
		System.out.println(bestMove.getTargetBoard());
		System.out.println(bestMove);
		assertThat(bestMove.getScore()).isSameAs(TerminationScore.MATE);
		assertThat(bestMove.toString()).isEqualTo("(#) Nd6-f7+");
	}

	@Test
	public void testNoMovesWhenMated() {
		final String fen = "8/8/8/8/8/5k2/6q1/7K w";
		noMoveWhenMated(fen, Color.WHITE);
	}

	@Test
	public void testBestMoveBaselineMateWithQueen() throws Exception {
		final String fen = "7k/8/8/8/4q3/8/6PP/6K1 b";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, 1);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.toString()).isEqualTo("(#) Qe4-e1+");
	}

	@Test
	public void testDefensiveBestMovePreventBaselineMate() {
		final String fen = "7k/8/8/8/4q3/8/6PP/7K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, 2);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.toMovesString()).matches(Pattern.compile("h2-h[34] .*$"));
	}

	private void noMoveWhenMated(final String fen, final Color color) {
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		assertThat(setupByFEN.isCheck()).isTrue();
		final List<ChessPly> possiblePlies = setupByFEN.getPossiblePlies(color);
		assertThat(possiblePlies).isEmpty();
		final ChessEngine engine = new ChessEngine(setupByFEN, color, 2);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.getScore()).isSameAs(TerminationScore.MATED);
	}

	@Test
	public void testNoMoveWhenMated2() {
		final String fen = "5Nnn/6rk/6pp/8/8/8/8/7K b";
		noMoveWhenMated(fen, Color.BLACK);
	}

	@Test
	public void testStalemate() {
		final ChessBoard stalematePosition = factory.setupByFEN("8/8/8/8/8/4k3/5q2/7K w");
		final ChessEngine engine = new ChessEngine(stalematePosition, 1);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(stalematePosition.isCheck()).isFalse();
		assertThat(bestMove.getScore()).isSameAs(TerminationScore.STALEMATE);
	}

	@Test
	public void testBestMoveMateIn2CombinationOfBishopAndRook() {
		final String fen = "5k2/5p1p/6p1/6B1/8/8/8/4R2K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, 3);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.toString()).isEqualTo("(#) Bg5-h6+ Kf8-g8 Re1-e8+");
	}

	@Test
	public void testBestMoveMateIn2CombinationOfBishopAndRook_variant2() {
		final String fen = "5k2/5p1p/6p1/6B1/8/8/8/3R3K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, 3);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.toString()).matches(Pattern.compile("^\\(#\\) Bg5-f6 .{4,5} Rd1-d8\\+$"));
	}

	@Test
	public void testBestMoveMateIn3CombinationOfBishopAndRook() {
		final String fen = "5k2/5p2/6p1/6B1/8/8/8/3R3K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, 5);
		final PlyResult bestMove = engine.getBestMove();
		assertThat(bestMove.toString()).isEqualTo("(#) Bg5-f6 Kf8-g8 Rd1-d8+ Kg8-h7 Rd8-h8+");
	}

	@Test
	public void testBestMoveMateIn3CombinationOfBishopAndRook_TakeLongestVariant() {
		final String fen = "5k2/5p2/5Bp1/8/8/8/8/3R3K b";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, 4);
		final PlyResult bestMove = engine.getBestMove();
		assertThat(bestMove.toString()).isEqualTo("(-#) Kf8-g8 Rd1-d8+ Kg8-h7 Rd8-h8+");
	}

	@Test
	public void testRookBaselineMate() throws Exception {
		final String fen = "5k2/5p2/5B2/8/8/8/8/3R3K w";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, 2);
		final PlyResult bestMove = engine.getBestMove();
		assertThat(bestMove.toString()).isEqualTo("(#) Rd1-d8+");
	}

	@Test
	public void testMateIn3_CombinationWithKnightQueenSacrificeAndRook() {
		final String fen = "1kr3q1/ppp5/5N2/8/8/2R1Q3/8/7K w";

		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, 5);
		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.toString()).isEqualTo("(#) Nf6-d7+ Kb8-a8 Qe3xa7+ Ka8xa7 Rc3-a3+");
	}

	@Test
	public void testBestMoveForRookBaselineMate() {
		final String fen = "7k/8/8/8/8/8/4r1PP/7K b";
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, 1);
		final PlyResult bestMove = engine.getBestMove();

		assertThat(bestMove.toString()).isEqualTo("(#) Re2-e1+");
	}

	@Test
	public void testBestMoveForMaterialGainTakeQueenNotRook() {
		final String fen = "1r6/8/1R6/4k1BK/3q4/8/8/7B w";

		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		assertThat(setupByFEN.isCheck()).isFalse();
		final ChessEngine engine = new ChessEngine(setupByFEN, 3);

		final PlyResult bestMove = engine.getBestMove();
		System.out.println(bestMove.getTargetBoard());
		assertThat(bestMove.toString()).matches(Pattern.compile("\\(\\d{0,9}\\) Bg5-f6\\+ Ke5-f[45] Bf6xd4.*"));
	}

	@Test
	public void testMateWithKnightAndBishop() throws Exception {
		final String fen = "1k6/8/B7/1K6/3N4/8/8/8 w";
		final String toStringMoveSeries = "(#) Kb5-b6 Kb8-a8 Ba6-b7+ Ka8-b8 Nd4-c6+";
		final int plies = 5;
		findBestMoveSeries(fen, toStringMoveSeries, plies);
	}

	private void findBestMoveSeries(final String fen, final String toStringMoveSeries, final int plies) {
		final PlyResult bestMove = findBestMove(fen, plies);
		assertThat(bestMove.toString()).isEqualTo(toStringMoveSeries);
	}

	private PlyResult findBestMove(final String fen, final int plies) {
		final ChessBoard setupByFEN = factory.setupByFEN(fen);
		final ChessEngine engine = new ChessEngine(setupByFEN, plies);

		final PlyResult bestMove = engine.getBestMove();
		return bestMove;
	}

	@Test
	public void testMateWithPawn() {
		final String fen = "8/8/6pp/6bk/7n/6PK/8/8 w";
		findBestMoveSeries(fen, "(#) g3-g4+", 1);
	}

	@Test
	public void testMateWithBishop() {
		final String fen = "k7/p7/8/4B3/2B5/4K3/8/8 w";
		findBestMoveSeries(fen, "(#) Bc4-d5+", 1);
	}

	@Test
	public void testNearlyMate() {
		final PlyResult findBestMove = findBestMove("7R/2pr4/2n3B1/8/1kP5/1P6/PK6/1N3r2 w", 5);
		patternMatchMove(findBestMove, ANY_NUMERIC_RATING + "Rh8-h5");
	}

	@Test
	public void testWinQueenWithPawnFork() {
		final PlyResult findBestMove = findBestMove("k1q5/7P/1P6/2N5/8/8/8/1K6 w", 4);
		assertThat(findBestMove.toMovesString()).isEqualTo("b6-b7+ Qc8xb7+ Nc5xb7 Ka8xb7");
	}

	@Test
	public void testMateInTwoWithSilentMove() {
		final PlyResult plyResult = findBestMove("8/8/4R1p1/2kPp3/2P2pr1/5p2/3K1p2/3Q4 w", 5);
		assertThat(plyResult.toString()).startsWith("(#) Kd2-c3 ").endsWith("Re6-c6+");
	}

	@Test
	public void testRookStrategy() {
		final PlyResult plyResult = findBestMove("r6k/ppp1pp1p/6p1/8/8/6P1/PPP1PP1P/R6K w", 5);
		assertThat(plyResult.toMovesString()).startsWith("Ra1-d1 ");
	}

	@Test
	public void testMateInTwoStartsWithSilentknightMove() {
		final PlyResult plyResult = findBestMove("7k/2nN4/8/8/8/8/8/1K4R1 w", 5);
		assertThat(plyResult.getScore().toString()).isEqualTo("#");
		assertThat(plyResult.toMovesString()).startsWith("Nd7-f6 ").endsWith(" Rg1-g8+");
	}

	@Test
	public void testQueenNeedsToGoAway() {
		final PlyResult result = findBestMove("6nk/6p1/2r4p/6Q1/8/8/PP6/K7 w", 5);
		assertThat(result.toMovesString()).startsWith("Qg5-");
	}

	@Test
	public void testKnightFork() {
		final PlyResult bestMove = findSimpleBestMove("8/pkp5/1n2q3/8/8/1P1N4/PKP5/8 w");
		assertThat(bestMove.toMovesString()).startsWith("Nd3-c5+ ");
	}

	@Test
	public void testPawnFork() {
		final PlyResult plyResult = findBestMove("8/pkp5/1p6/1n1q4/5R2/1P1N1R2/PKP5/8 w", 4);
		assertThat(plyResult.toMovesString()).startsWith("c2-c4 Qd5-").endsWith("c4xb5");
	}

	@Test
	public void testBishopSkewerWithCheck() {
		final PlyResult plyResult = findBestMove("6q1/8/4k3/8/8/2K5/4B3/5R2 w", 3);
		assertThat(plyResult.toMovesString()).startsWith("Be2-c4+ Ke6-").endsWith("Bc4xg8");
	}

	@Test
	public void testBishopSkewerWithAttack() {
		final PlyResult plyResult = findBestMove("6k1/8/8/3q4/8/2K5/4B3/5R2 w", 3);
		assertThat(plyResult.toMovesString()).startsWith("Be2-c4 Qd5xc4+ ");
	}

	@Test
	public void testRookSkewerWithCheck() {
		final PlyResult plyResult = findBestMove("q2k4/8/8/8/8/2K5/B7/5R2 w", 3);
		assertThat(plyResult.toMovesString()).startsWith("Rf1-f8+ Kd8-").endsWith(" Rf8xa8");
	}

	@Test
	public void testRookSkewerWithAttack() {
		final PlyResult plyResult = findBestMove("k2q4/8/8/8/8/B1K5/8/5R2 w", 3);
		assertThat(plyResult.toMovesString()).isEqualTo("Rf1-f8 Qd8xf8 Ba3xf8");
	}

	@Test
	public void testDefensivePawnMove() {
		final PlyResult plyResult = findBestMove("6k1/5pp1/4n3/8/2N3P1/3K3P/8/8 w", 4);
		assertThat(plyResult.toMovesString()).startsWith("h4-h5 ");
	}

	@Test
	public void testClosePositionForADraw() {
		final PlyResult plyResult = findBestMove("k1b5/8/p7/Pp1p1p2/1P2pPpK/3PP1P1/8/8 w", 5);
		assertThat(plyResult.toMovesString()).startsWith("d3-d4 ");
	}

	private void patternMatchMove(final PlyResult findBestMove, final String regex) {
		assertThat(findBestMove.toString()).matches(Pattern.compile(regex));

	}
}
