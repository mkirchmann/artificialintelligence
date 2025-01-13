package de.neuenberger.ai.impl.chess.model.bitboard;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBoardPreCalculationsTest {

	private BitBoardPreCalculations instance;

	Logger logger = LoggerFactory.getLogger(getClass());

	@BeforeEach
	public void before() {
		instance = BitBoardPreCalculations.getInstance();
	}

	@Test
	public void testBinarySearch() {

		for (int i = 0; i < 64; i++) {
			final Position expected = instance.getPosition(i);
			logger.info("i: " + i + ", " + expected.getFieldBit());
			final Position bit = instance.binarySearch(expected.getFieldBit());
			Assertions.assertThat(bit).isSameAs(expected);
		}
	}
}
