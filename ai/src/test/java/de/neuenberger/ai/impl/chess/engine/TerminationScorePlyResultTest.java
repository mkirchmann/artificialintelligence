package de.neuenberger.ai.impl.chess.engine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;

@RunWith(MockitoJUnitRunner.class)
public class TerminationScorePlyResultTest {

	@Mock
	ChessBoard board;

	@Test
	public void testCompareTo() throws Exception {
		final TerminationScorePlyResult result1 = new TerminationScorePlyResult(TerminationScore.MATE, board);
		result1.insertPly(Mockito.mock(ChessPly.class));

		final TerminationScorePlyResult result2 = new TerminationScorePlyResult(TerminationScore.MATE, board);
		result2.insertPly(Mockito.mock(ChessPly.class));

		final int compareResult1 = result1.compareTo(result2);
		Assertions.assertThat(compareResult1).isEqualTo(0);

		result1.insertPly(Mockito.mock(ChessPly.class));
		result1.insertPly(Mockito.mock(ChessPly.class));
		final int compareResult2 = result1.compareTo(result2);
		Assertions.assertThat(compareResult2).isEqualTo(-1);

		result2.insertPly(Mockito.mock(ChessPly.class));
		result2.insertPly(Mockito.mock(ChessPly.class));
		final int compareResult3 = result1.compareTo(result2);
		Assertions.assertThat(compareResult3).isEqualTo(0);
		result2.insertPly(Mockito.mock(ChessPly.class));

		final int compareResult4 = result1.compareTo(result2);
		Assertions.assertThat(compareResult4).isEqualTo(1);
	}

	@Test
	public void testCompareToWithMated() throws Exception {
		final TerminationScorePlyResult result1 = new TerminationScorePlyResult(TerminationScore.MATED, board);
		result1.insertPly(Mockito.mock(ChessPly.class));

		final TerminationScorePlyResult result2 = new TerminationScorePlyResult(TerminationScore.MATED, board);
		result2.insertPly(Mockito.mock(ChessPly.class));

		final int compareResult1 = result1.compareTo(result2);
		Assertions.assertThat(compareResult1).isEqualTo(0);

		result1.insertPly(Mockito.mock(ChessPly.class));
		result1.insertPly(Mockito.mock(ChessPly.class));
		final int compareResult2 = result1.compareTo(result2);
		Assertions.assertThat(compareResult2).isEqualTo(1);

		result2.insertPly(Mockito.mock(ChessPly.class));
		result2.insertPly(Mockito.mock(ChessPly.class));
		final int compareResult3 = result1.compareTo(result2);
		Assertions.assertThat(compareResult3).isEqualTo(0);
		result2.insertPly(Mockito.mock(ChessPly.class));

		final int compareResult4 = result1.compareTo(result2);
		Assertions.assertThat(compareResult4).isEqualTo(-1);
	}

}
