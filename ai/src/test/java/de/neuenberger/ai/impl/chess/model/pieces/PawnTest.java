package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.BasePlyList;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoardFactory;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.PlyList;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;

public class PawnTest {
	ChessBoard chessBoard = new ChessBoardFactory().createInitalSetup();

	@Test
	public void test() {
		final Position position = chessBoard.fromPosition(4, 1);
		final Piece pieceAt = chessBoard.getPieceAt(position);

		final PlyList plies = new BasePlyList();
		pieceAt.addPossiblePlies(plies, chessBoard, position, true);

		Assertions.assertThat(plies.getCollection()).hasSize(2);
	}

	@Test
	public void testPawnMoveIsCheck() {
		final ChessBoard board = new ChessBoardFactory().setupByFEN("k1q5/7P/1P6/2N5/8/8/8/1K6 w");
		final Position position = board.fromPosition(1, 5);
		final Pawn whitePawn = new Pawn(Color.WHITE);
		final PlyList pliesList = new BasePlyList();
		whitePawn.addPossiblePlies(pliesList, board, position, true);
		final List<ChessPly> plies = pliesList.getCollection();
		Assertions.assertThat(plies).hasSize(1);
		final ChessPly chessPly = plies.get(0);
		Assertions.assertThat(chessPly.isCheck()).isTrue();
	}
}
