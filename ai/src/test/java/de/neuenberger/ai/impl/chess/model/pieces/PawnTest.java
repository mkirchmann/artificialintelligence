package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class PawnTest {
	ChessBoard chessBoard = ChessBoard.createInitialChessBoard();

	@Test
	public void test() {
		final Piece pieceAt = chessBoard.getPieceAt(4, 1);

		final List<ChessPly> plies = new ArrayList<>();
		pieceAt.addPossiblePlies(plies, chessBoard, 4, 1, true);

		Assertions.assertThat(plies).hasSize(2);
	}
}
