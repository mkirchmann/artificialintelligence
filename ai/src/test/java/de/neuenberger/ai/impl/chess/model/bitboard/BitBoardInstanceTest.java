package de.neuenberger.ai.impl.chess.model.bitboard;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.Piece.PieceType;
import de.neuenberger.ai.impl.chess.model.pieces.Bishop;
import de.neuenberger.ai.impl.chess.model.pieces.Pawn;
import de.neuenberger.ai.impl.chess.model.pieces.Queen;
import de.neuenberger.ai.impl.chess.model.pieces.Rook;

public class BitBoardInstanceTest {
	private BitBoardInstance bitBoardInstance;

	final BitBoardPreCalculations instance = BitBoardPreCalculations.getInstance();
	final Position positionC2 = instance.fromZeroBasedCoordinates(2, 1);
	final Position positionC1 = instance.fromZeroBasedCoordinates(2, 0);
	final Position positionE2 = instance.fromZeroBasedCoordinates(4, 1);
	final Position positionE4 = instance.fromZeroBasedCoordinates(4, 3);
	final Position positionF1 = instance.fromZeroBasedCoordinates(5, 0);
	final Position positionC4 = instance.fromZeroBasedCoordinates(2, 3);
	final Position positionD1 = instance.fromZeroBasedCoordinates(3, 0);
	final Position positionF3 = instance.fromZeroBasedCoordinates(5, 2);
	final Position positionF7 = instance.fromZeroBasedCoordinates(5, 6);
	final Queen blackQueen = new Queen(Color.BLACK);
	final Queen whiteQueen = new Queen(Color.WHITE);
	final Piece whiteRook = new Rook(Color.WHITE);
	final Pawn whitePawn = new Pawn(Color.WHITE);
	final Bishop whiteBishop = new Bishop(Color.WHITE);

	private final Piece blackPawn = new Pawn(Color.BLACK);

	@Before
	public void before() {

		bitBoardInstance = new BitBoardInstance(instance);
	}

	@Test
	public void testScore() {
		bitBoardInstance.setPiece(whitePawn, positionC2);
		bitBoardInstance.setPiece(whiteQueen, positionC1);
		Assertions.assertThat(bitBoardInstance.getPieceScore()).isEqualTo(93);
	}

	@Test
	public void testCapture() {

		bitBoardInstance.setPiece(blackQueen, positionC2);
		bitBoardInstance.setPiece(whiteRook, positionC1);
		bitBoardInstance.movePiece(blackQueen, whiteRook, positionC2, positionC1); // captures
		// other
		// Queen.
		Assertions.assertThat(bitBoardInstance.getPieceScore()).isEqualTo(-PieceType.QUEEN.getCentiPawns());
	}

	@Test
	public void testSetFieldNull() {
		bitBoardInstance.setPiece(blackQueen, positionC1);
		bitBoardInstance.setPiece(whitePawn, positionC2);
		bitBoardInstance.setPiece(null, positionC1);
		Assertions.assertThat(bitBoardInstance.getPieceScore()).isEqualTo(PieceType.PAWN.getCentiPawns());
	}

	@Test
	public void testCloneVirtuallyTheSame() throws Exception {
		final BitBoardInstance bitBoardInstance2 = new ChessBoardFactory().createInitalSetup().getBitBoardInstance();
		final String string = bitBoardInstance2.toString();

		final BitBoardInstance clone = bitBoardInstance2.clone();
		final String result = clone.toString();
		System.out.println(result);
		Assertions.assertThat(result).isEqualTo(string);
	}

	@Test
	public void testMoving() throws Exception {
		final BitBoardInstance bitBoardInstance2 = new ChessBoardFactory().createInitalSetup().getBitBoardInstance();
		final String string = bitBoardInstance2.toString();

		final BitBoardInstance cloneWhiteE4 = bitBoardInstance2.clone();
		final String result = cloneWhiteE4.toString();
		System.out.println(result);
		cloneWhiteE4.movePiece(whitePawn, null, positionE2, positionE4);
		System.out.println(cloneWhiteE4);
		System.out.println(cloneWhiteE4.getPieceScore());
		final BitBoardInstance cloneBishopC4 = cloneWhiteE4.clone();
		cloneBishopC4.movePiece(whiteBishop, null, positionF1, positionC4);
		System.out.println(cloneBishopC4);
		System.out.println(cloneBishopC4.getPieceScore());
		final BitBoardInstance whiteQueenOut = cloneBishopC4.clone();
		whiteQueenOut.movePiece(whiteQueen, null, positionD1, positionF3);
		System.out.println(whiteQueenOut);
		System.out.println(whiteQueenOut.getPieceScore());
		final BitBoardInstance whiteQueenMates = whiteQueenOut.clone();
		whiteQueenMates.movePiece(whiteQueen, blackPawn, positionF3, positionF7);
		System.out.println(whiteQueenMates);
		System.out.println(whiteQueenMates.getPieceScore());
		System.out.println(whiteQueenMates.debugInfo());
	}
}
