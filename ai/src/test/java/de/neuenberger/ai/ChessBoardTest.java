package de.neuenberger.ai;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import de.neuenberger.ai.impl.chess.model.ChessBoard;

public class ChessBoardTest {
	@Test
	public void testInitialSetup() {
		final ChessBoard chessBoard = ChessBoard.createInitialChessBoard();
		final String string = chessBoard.toString();

		Assertions.assertThat(string).startsWith("rnbqkbnr\npppppppp\n");
		Assertions.assertThat(string).endsWith("PPPPPPPP\nRNBQKBNR\n");
	}
}
