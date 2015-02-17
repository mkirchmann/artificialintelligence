package de.neuenberger.ai.impl.chess.model;

import java.util.LinkedList;
import java.util.List;

import de.neuenberger.ai.impl.chess.model.BitBoard.Position.CoordinateX;

public class BitBoard {
	public static class Position {
		public enum CoordinateX {
			A, B, C, D, E, F, G, H;
			@Override
			public String toString() {
				return super.toString().toLowerCase();
			};
		}

		private final CoordinateX x;
		private final int y;
		private final int idx;

		private Position(final CoordinateX x, final int y, final int idx) {
			this.x = x;
			this.y = y;
			this.idx = idx;
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
	}

	private final Position[] positions;
	private List<Position>[] upperLeftDiagonal;
	private List<Position>[] upperRightDiagonal;
	private List<Position>[] lowerLeftDiagonal;
	private List<Position>[] lowerRightDiagonal;
	private List<Position>[] topVertical;
	private List<Position>[] bottomVertical;
	private List<Position>[] leftHorizontal;
	private List<Position>[] rightHorizontal;
	private List<Position>[] kingNormalFields;
	private List<Position>[] knightMoves;
	private List<Position>[] blackPawnNormalMoves;
	private List<Position>[] whitePawnNormalMoves;
	private List<Position>[] blackPawnCaptureMoves;
	private List<Position>[] whitePawnCaptureMoves;

	private static final BitBoard instance = new BitBoard();

	private BitBoard() {
		positions = new Position[64];
		final CoordinateX[] xCoordinates = CoordinateX.values();
		int idx = 0;
		for (int i = 1; i <= 8; i++) {
			for (final CoordinateX coordinateX : xCoordinates) {
				positions[idx] = new Position(coordinateX, i, idx);
				idx++;
			}
		}
		createDiagonals();
		createStraights();
		createKnightMoves();
		createKingMoves();
		createPawnMoves();
	}

	private void createKingMoves() {
		kingNormalFields = new List[64];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				kingNormalFields[position.idx] = new LinkedList<>();

				checkBoundsAndAdd(kingNormalFields[position.idx], x + 1, y + 1);
				checkBoundsAndAdd(kingNormalFields[position.idx], x + 1, y);
				checkBoundsAndAdd(kingNormalFields[position.idx], x + 1, y - 1);

				checkBoundsAndAdd(kingNormalFields[position.idx], x, y - 1);
				checkBoundsAndAdd(kingNormalFields[position.idx], x, y + 1);

				checkBoundsAndAdd(kingNormalFields[position.idx], x - 1, y + 1);
				checkBoundsAndAdd(kingNormalFields[position.idx], x - 1, y);
				checkBoundsAndAdd(kingNormalFields[position.idx], x - 1, y - 1);
			}
		}
	}

	private void createKnightMoves() {
		knightMoves = new List[64];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				knightMoves[position.idx] = new LinkedList<>();
				checkBoundsAndAdd(knightMoves[position.idx], x + 1, y + 2);
				checkBoundsAndAdd(knightMoves[position.idx], x + 1, y - 2);
				checkBoundsAndAdd(knightMoves[position.idx], x - 1, y + 2);
				checkBoundsAndAdd(knightMoves[position.idx], x - 1, y - 2);

				checkBoundsAndAdd(knightMoves[position.idx], x + 2, y + 1);
				checkBoundsAndAdd(knightMoves[position.idx], x - 2, y + 1);
				checkBoundsAndAdd(knightMoves[position.idx], x + 2, y - 1);
				checkBoundsAndAdd(knightMoves[position.idx], x - 2, y - 1);
			}
		}
	}

	private void createStraights() {
		topVertical = new List[64];
		bottomVertical = new List[64];
		leftHorizontal = new List[64];
		rightHorizontal = new List[64];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				topVertical[position.idx] = new LinkedList<>();
				bottomVertical[position.idx] = new LinkedList<>();
				leftHorizontal[position.idx] = new LinkedList<>();
				rightHorizontal[position.idx] = new LinkedList<>();
				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(rightHorizontal[position.idx], x + i, y)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(leftHorizontal[position.idx], x - i, y)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(bottomVertical[position.idx], x, y - i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(topVertical[position.idx], x, y + i)) {
						break;
					}
				}

			}
		}
	}

	private void createDiagonals() {
		upperLeftDiagonal = new List[64];
		upperRightDiagonal = new List[64];
		lowerLeftDiagonal = new List[64];
		lowerRightDiagonal = new List[64];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				upperLeftDiagonal[position.idx] = new LinkedList<>();
				upperRightDiagonal[position.idx] = new LinkedList<>();
				lowerLeftDiagonal[position.idx] = new LinkedList<>();
				lowerRightDiagonal[position.idx] = new LinkedList<>();

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(upperLeftDiagonal[position.idx], x - i, y + i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(upperRightDiagonal[position.idx], x + i, y + i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(lowerLeftDiagonal[position.idx], x - i, y - i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(lowerRightDiagonal[position.idx], x + i, y - i)) {
						break;
					}
				}
			}
		}
	}

	private void createPawnMoves() {
		blackPawnNormalMoves = new List[64];
		whitePawnNormalMoves = new List[64];
		blackPawnCaptureMoves = new List[64];
		whitePawnCaptureMoves = new List[64];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				blackPawnNormalMoves[position.idx] = new LinkedList<>();
				whitePawnNormalMoves[position.idx] = new LinkedList<>();
				blackPawnCaptureMoves[position.idx] = new LinkedList<>();
				whitePawnCaptureMoves[position.idx] = new LinkedList<>();
				checkBoundsAndAdd(blackPawnNormalMoves[position.idx], x, y - 1);
				if (y == 6) {
					checkBoundsAndAdd(blackPawnNormalMoves[position.idx], x, y - 2);
				}
				checkBoundsAndAdd(blackPawnCaptureMoves[position.idx], x + 1, y - 1);
				checkBoundsAndAdd(blackPawnCaptureMoves[position.idx], x - 1, y - 1);
				checkBoundsAndAdd(whitePawnNormalMoves[position.idx], x, y + 1);
				if (y == 1) {
					checkBoundsAndAdd(whitePawnNormalMoves[position.idx], x, y + 2);
				}
				checkBoundsAndAdd(whitePawnCaptureMoves[position.idx], x + 1, y + 1);
				checkBoundsAndAdd(whitePawnCaptureMoves[position.idx], x - 1, y + 1);
			}
		}
	}

	private boolean checkBoundsAndAdd(final List<Position> list, final int x, final int y) {
		boolean result;
		if (0 <= x && x < 8 && 0 <= y && y < 8) {
			list.add(fromZeroBasedCoordinates(x, y));
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	Position fromZeroBasedCoordinates(final int x, final int y) {
		return positions[y * 8 + x];
	}

	/**
	 * @return the instance
	 */
	public static BitBoard getInstance() {
		return instance;
	}

	public Position getPosition(final int i) {
		return positions[i];
	}

	/**
	 * @return the upperLeftDiagonal
	 */
	public List<Position> getUpperLeftDiagonal(final Position position) {
		return upperLeftDiagonal[position.idx];
	}

	/**
	 * @return the upperRightDiagonal
	 */
	public List<Position> getUpperRightDiagonal(final Position position) {
		return upperRightDiagonal[position.idx];
	}

	/**
	 * @return the lowerLeftDiagonal
	 */
	public List<Position> getLowerLeftDiagonal(final Position position) {
		return lowerLeftDiagonal[position.idx];
	}

	/**
	 * @return the lowerRightDiagonal
	 */
	public List<Position> getLowerRightDiagonal(final Position position) {
		return lowerRightDiagonal[position.idx];
	}

	/**
	 * @return the topVertical
	 */
	public List<Position> getTopVertical(final Position position) {
		return topVertical[position.idx];
	}

	/**
	 * @return the bottomVertical
	 */
	public List<Position> getBottomVertical(final Position position) {
		return bottomVertical[position.idx];
	}

	/**
	 * @return the leftHorizontal
	 */
	public List<Position> getLeftHorizontal(final Position position) {
		return leftHorizontal[position.idx];
	}

	/**
	 * @return the rightHorizontal
	 */
	public List<Position> getRightHorizontal(final Position position) {
		return rightHorizontal[position.idx];
	}

	/**
	 * @return the kingNormalFields
	 */
	public List<Position> getKingNormalFields(final Position position) {
		return kingNormalFields[position.idx];
	}

	/**
	 * @return the knightMoves
	 */
	public List<Position> getKnightMoves(final Position position) {
		return knightMoves[position.idx];
	}

	/**
	 * @return the blackPawnNormalMoves
	 */
	public List<Position> getBlackPawnNormalMoves(final Position position) {
		return blackPawnNormalMoves[position.idx];
	}

	/**
	 * @return the whitePawnNormalMoves
	 */
	public List<Position> getWhitePawnNormalMoves(final Position position) {
		return whitePawnNormalMoves[position.idx];
	}

	/**
	 * @return the blackPawnCaptureMoves
	 */
	public List<Position> getBlackPawnCaptureMoves(final Position position) {
		return blackPawnCaptureMoves[position.idx];
	}

	/**
	 * @return the whitePawnCaptureMoves
	 */
	public List<Position> getWhitePawnCaptureMoves(final Position position) {
		return whitePawnCaptureMoves[position.idx];
	}
}
