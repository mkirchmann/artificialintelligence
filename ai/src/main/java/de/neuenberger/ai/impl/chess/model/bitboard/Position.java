package de.neuenberger.ai.impl.chess.model.bitboard;

public class Position {
	private final CoordinateX x;
	private final int y;
	private final int idx;

	private final long fieldBit;
	private final long inverseFieldBit;

	Position(final CoordinateX x, final int y, final int idx) {
		this.x = x;
		this.y = y;
		this.idx = idx;
		fieldBit = 1l << idx;
		inverseFieldBit = ~fieldBit;
	}

	/**
	 * @return the x
	 */
	public CoordinateX getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @return the idx
	 */
	public int getIdx() {
		return idx;
	}

	@Override
	public String toString() {
		return x.toString() + y;
	}

	/**
	 * @return the fieldBit
	 */
	public long getFieldBit() {
		return fieldBit;
	}

	/**
	 * @return the inverseFieldBit
	 */
	public long getInverseFieldBit() {
		return inverseFieldBit;
	}
}