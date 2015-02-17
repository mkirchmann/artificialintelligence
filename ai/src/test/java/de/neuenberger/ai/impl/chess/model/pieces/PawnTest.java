package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.BitBoard.Position;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;

public class PawnTest {
	ChessBoard chessBoard = new ChessBoardFactory().createInitalSetup();

	@Test
	public void test() {
		final Position position = chessBoard.fromPosition(4, 1);
		final Piece pieceAt = chessBoard.getPieceAt(position);

		final List<ChessPly> plies = new ArrayList<>();
		pieceAt.addPossiblePlies(plies, chessBoard, position, true);

		Assertions.assertThat(plies).hasSize(2);
	}
}
