package de.neuenberger.ai.impl.chess.model.bitboard;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBoardPreCalculations {
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
	private Set<Position>[] attackCheckPositions;
	private static final BitBoardPreCalculations instance = new BitBoardPreCalculations();

	private final int binarySearchDelta[] = new int[] { 16, 8, 4, 2, 1, 1, 1 };

	Logger log = LoggerFactory.getLogger(getClass());
	private final Position positionH8;

	private BitBoardPreCalculations() {
		positions = new Position[64];
		final CoordinateX[] xCoordinates = CoordinateX.values();
		int idx = 0;
		for (int i = 1; i <= 8; i++) {
			for (final CoordinateX coordinateX : xCoordinates) {
				positions[idx] = new Position(coordinateX, i, idx);
				idx++;
			}
		}
		positionH8 = positions[63];
		createDiagonals();
		createStraights();
		createKnightMoves();
		createKingMoves();
		createPawnMoves();
		createAttackCheckPositions();
	}

	private void createAttackCheckPositions() {
		attackCheckPositions = new Set[64];
		for (int i = 0; i < 64; i++) {
			attackCheckPositions[i] = new HashSet<>();
			attackCheckPositions[i].addAll(knightMoves[i]);
			attackCheckPositions[i].addAll(topVertical[i]);
			attackCheckPositions[i].addAll(bottomVertical[i]);
			attackCheckPositions[i].addAll(rightHorizontal[i]);
			attackCheckPositions[i].addAll(leftHorizontal[i]);
			attackCheckPositions[i].addAll(leftHorizontal[i]);
			attackCheckPositions[i].addAll(upperLeftDiagonal[i]);
			attackCheckPositions[i].addAll(lowerLeftDiagonal[i]);
			attackCheckPositions[i].addAll(upperRightDiagonal[i]);
			attackCheckPositions[i].addAll(lowerRightDiagonal[i]);
		}
	}

	private void createKingMoves() {
		kingNormalFields = new List[64];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				kingNormalFields[position.getIdx()] = new LinkedList<>();

				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x + 1, y + 1);
				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x + 1, y);
				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x + 1, y - 1);

				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x, y - 1);
				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x, y + 1);

				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x - 1, y + 1);
				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x - 1, y);
				checkBoundsAndAdd(kingNormalFields[position.getIdx()], x - 1, y - 1);
			}
		}
	}

	private void createKnightMoves() {
		knightMoves = new List[64];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				knightMoves[position.getIdx()] = new LinkedList<>();
				checkBoundsAndAdd(knightMoves[position.getIdx()], x + 1, y + 2);
				checkBoundsAndAdd(knightMoves[position.getIdx()], x + 1, y - 2);
				checkBoundsAndAdd(knightMoves[position.getIdx()], x - 1, y + 2);
				checkBoundsAndAdd(knightMoves[position.getIdx()], x - 1, y - 2);

				checkBoundsAndAdd(knightMoves[position.getIdx()], x + 2, y + 1);
				checkBoundsAndAdd(knightMoves[position.getIdx()], x - 2, y + 1);
				checkBoundsAndAdd(knightMoves[position.getIdx()], x + 2, y - 1);
				checkBoundsAndAdd(knightMoves[position.getIdx()], x - 2, y - 1);
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
				topVertical[position.getIdx()] = new LinkedList<>();
				bottomVertical[position.getIdx()] = new LinkedList<>();
				leftHorizontal[position.getIdx()] = new LinkedList<>();
				rightHorizontal[position.getIdx()] = new LinkedList<>();
				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(rightHorizontal[position.getIdx()], x + i, y)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(leftHorizontal[position.getIdx()], x - i, y)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(bottomVertical[position.getIdx()], x, y - i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(topVertical[position.getIdx()], x, y + i)) {
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
				upperLeftDiagonal[position.getIdx()] = new LinkedList<>();
				upperRightDiagonal[position.getIdx()] = new LinkedList<>();
				lowerLeftDiagonal[position.getIdx()] = new LinkedList<>();
				lowerRightDiagonal[position.getIdx()] = new LinkedList<>();

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(upperLeftDiagonal[position.getIdx()], x - i, y + i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(upperRightDiagonal[position.getIdx()], x + i, y + i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(lowerLeftDiagonal[position.getIdx()], x - i, y - i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(lowerRightDiagonal[position.getIdx()], x + i, y - i)) {
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
				blackPawnNormalMoves[position.getIdx()] = new LinkedList<>();
				whitePawnNormalMoves[position.getIdx()] = new LinkedList<>();
				blackPawnCaptureMoves[position.getIdx()] = new LinkedList<>();
				whitePawnCaptureMoves[position.getIdx()] = new LinkedList<>();
				checkBoundsAndAdd(blackPawnNormalMoves[position.getIdx()], x, y - 1);
				if (y == 6) {
					checkBoundsAndAdd(blackPawnNormalMoves[position.getIdx()], x, y - 2);
				}
				checkBoundsAndAdd(blackPawnCaptureMoves[position.getIdx()], x + 1, y - 1);
				checkBoundsAndAdd(blackPawnCaptureMoves[position.getIdx()], x - 1, y - 1);
				checkBoundsAndAdd(whitePawnNormalMoves[position.getIdx()], x, y + 1);
				if (y == 1) {
					checkBoundsAndAdd(whitePawnNormalMoves[position.getIdx()], x, y + 2);
				}
				checkBoundsAndAdd(whitePawnCaptureMoves[position.getIdx()], x + 1, y + 1);
				checkBoundsAndAdd(whitePawnCaptureMoves[position.getIdx()], x - 1, y + 1);
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

	public Position fromZeroBasedCoordinates(final int x, final int y) {
		return positions[y * 8 + x];
	}

	/**
	 * @return the instance
	 */
	public static BitBoardPreCalculations getInstance() {
		return instance;
	}

	public Position getPosition(final int i) {
		return positions[i];
	}

	/**
	 * @return the upperLeftDiagonal
	 */
	public List<Position> getUpperLeftDiagonal(final Position position) {
		return upperLeftDiagonal[position.getIdx()];
	}

	/**
	 * @return the upperRightDiagonal
	 */
	public List<Position> getUpperRightDiagonal(final Position position) {
		return upperRightDiagonal[position.getIdx()];
	}

	/**
	 * @return the lowerLeftDiagonal
	 */
	public List<Position> getLowerLeftDiagonal(final Position position) {
		return lowerLeftDiagonal[position.getIdx()];
	}

	/**
	 * @return the lowerRightDiagonal
	 */
	public List<Position> getLowerRightDiagonal(final Position position) {
		return lowerRightDiagonal[position.getIdx()];
	}

	/**
	 * @return the topVertical
	 */
	public List<Position> getTopVertical(final Position position) {
		return topVertical[position.getIdx()];
	}

	/**
	 * @return the bottomVertical
	 */
	public List<Position> getBottomVertical(final Position position) {
		return bottomVertical[position.getIdx()];
	}

	/**
	 * @return the leftHorizontal
	 */
	public List<Position> getLeftHorizontal(final Position position) {
		return leftHorizontal[position.getIdx()];
	}

	/**
	 * @return the rightHorizontal
	 */
	public List<Position> getRightHorizontal(final Position position) {
		return rightHorizontal[position.getIdx()];
	}

	/**
	 * @return the kingNormalFields
	 */
	public List<Position> getKingNormalFields(final Position position) {
		return kingNormalFields[position.getIdx()];
	}

	/**
	 * @return the knightMoves
	 */
	public List<Position> getKnightMoves(final Position position) {
		return knightMoves[position.getIdx()];
	}

	/**
	 * @return the blackPawnNormalMoves
	 */
	public List<Position> getBlackPawnNormalMoves(final Position position) {
		return blackPawnNormalMoves[position.getIdx()];
	}

	/**
	 * @return the whitePawnNormalMoves
	 */
	public List<Position> getWhitePawnNormalMoves(final Position position) {
		return whitePawnNormalMoves[position.getIdx()];
	}

	/**
	 * @return the blackPawnCaptureMoves
	 */
	public List<Position> getBlackPawnCaptureMoves(final Position position) {
		return blackPawnCaptureMoves[position.getIdx()];
	}

	/**
	 * @return the whitePawnCaptureMoves
	 */
	public List<Position> getWhitePawnCaptureMoves(final Position position) {
		return whitePawnCaptureMoves[position.getIdx()];
	}

	public Position binarySearch(final long king) {
		Position result = null;
		if (king == positionH8.getFieldBit()) {
			result = positionH8;
		} else if (king == 0) {
			// do nothing...
		} else {
			int currentIndex = 32;

			for (int i = 0; i < binarySearchDelta.length; i++) {
				final Position position = getPosition(currentIndex);
				final long fieldBit = position.getFieldBit();
				if (king == fieldBit) {
					// found
					result = position;
					break;
				} else if (king < fieldBit) {
					currentIndex -= binarySearchDelta[i];
				} else {
					currentIndex += binarySearchDelta[i];
				}
			}
		}
		return result;
	}

	/**
	 * @return the attackCheckPositions
	 */
	public Set<Position> getAttackCheckPositions(final Position position) {
		return attackCheckPositions[position.getIdx()];
	}

}
