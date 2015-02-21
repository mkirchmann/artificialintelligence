package de.neuenberger.ai.impl.chess.model.bitboard;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BitBoardPreCalculations {
	private static final int NUMBER_OF_FIELDS = 64;
	private final Position[] positions;
	private List<Position>[] upperLeftDiagonalPositionsList;
	private List<Position>[] upperRightDiagonalPositionsList;
	private List<Position>[] lowerLeftDiagonalPositionsList;
	private List<Position>[] lowerRightDiagonalPositionsList;
	private List<Position>[] topVerticalPositionsList;
	private List<Position>[] bottomVerticalPositionsList;
	private List<Position>[] leftHorizontalPositionsList;
	private List<Position>[] rightHorizontalPositionsList;
	private List<Position>[] kingNormalFieldsPositionsList;
	private List<Position>[] knightMovesPositionsList;
	private List<Position>[] blackPawnNormalMovesPositionsList;
	private List<Position>[] whitePawnNormalMovesPositionsList;
	private List<Position>[] blackPawnCaptureMovesPositionsList;
	private List<Position>[] whitePawnCaptureMovesPositionsList;

	private final long[] upperLeftDiagonal = new long[NUMBER_OF_FIELDS];
	private final long[] upperRightDiagonal = new long[NUMBER_OF_FIELDS];
	private final long[] lowerLeftDiagonal = new long[NUMBER_OF_FIELDS];
	private final long[] lowerRightDiagonal = new long[NUMBER_OF_FIELDS];
	private final long[] topVertical = new long[NUMBER_OF_FIELDS];
	private final long[] bottomVertical = new long[NUMBER_OF_FIELDS];
	private final long[] leftHorizontal = new long[NUMBER_OF_FIELDS];
	private final long[] rightHorizontal = new long[NUMBER_OF_FIELDS];
	private final long[] kingNormalFields = new long[NUMBER_OF_FIELDS];
	private final long[] knightMoves = new long[NUMBER_OF_FIELDS];
	private final long[] blackPawnNormalMoves = new long[NUMBER_OF_FIELDS];
	private final long[] whitePawnNormalMoves = new long[NUMBER_OF_FIELDS];
	private final long[] blackPawnCaptureMoves = new long[NUMBER_OF_FIELDS];
	private final long[] whitePawnCaptureMoves = new long[NUMBER_OF_FIELDS];
	private final long[] knightDistanceTwo = new long[NUMBER_OF_FIELDS];

	private Set<Position>[] allPossibleAttackerPositionsPositionsList;
	private final long[] allPossibleAttackerPositions = new long[NUMBER_OF_FIELDS];
	private static final BitBoardPreCalculations instance = new BitBoardPreCalculations();

	private final long[] ranks = new long[8];
	private final long[] ranksInverse = new long[8];
	private final long[] files = new long[8];
	private final long[] filesInverse = new long[8];

	private final long whiteFields;
	private final long blackFields;

	private final int binarySearchDelta[] = new int[] { 16, 8, 4, 2, 1, 1, 1 };

	Logger log = LoggerFactory.getLogger(getClass());
	private final Position positionH8;

	private BitBoardPreCalculations() {
		positions = new Position[NUMBER_OF_FIELDS];
		final CoordinateX[] xCoordinates = CoordinateX.values();
		int idx = 0;
		long tempBlackFields = 0;
		long tempWhiteFields = 0;
		for (int i = 1; i <= 8; i++) {
			for (final CoordinateX coordinateX : xCoordinates) {
				positions[idx] = new Position(coordinateX, i, idx);
				ranks[i - 1] |= positions[idx].getFieldBit();
				files[coordinateX.ordinal()] |= positions[idx].getFieldBit();
				final int blackOrWhite = (i - 1 + coordinateX.ordinal()) % 2;
				if (blackOrWhite == 0) {
					tempBlackFields |= positions[idx].getFieldBit();
				} else {
					tempWhiteFields |= positions[idx].getFieldBit();
				}
				idx++;
			}
		}
		for (int i = 0; i < 8; i++) {
			ranksInverse[i] = ~ranks[i];
			filesInverse[i] = ~files[i];
		}
		positionH8 = positions[63];
		createDiagonals();
		createStraights();
		createKnightMoves();
		createKingMoves();
		createPawnMoves();
		createAllPossibleAttackerPositions();

		whiteFields = tempWhiteFields;
		blackFields = tempBlackFields;
	}

	private void createAllPossibleAttackerPositions() {
		allPossibleAttackerPositionsPositionsList = new Set[NUMBER_OF_FIELDS];
		for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
			allPossibleAttackerPositionsPositionsList[i] = new HashSet<>();
			allPossibleAttackerPositionsPositionsList[i].addAll(knightMovesPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(topVerticalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(bottomVerticalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(rightHorizontalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(leftHorizontalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(leftHorizontalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(upperLeftDiagonalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(lowerLeftDiagonalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(upperRightDiagonalPositionsList[i]);
			allPossibleAttackerPositionsPositionsList[i].addAll(lowerRightDiagonalPositionsList[i]);
			allPossibleAttackerPositions[i] = listToBitBoard(allPossibleAttackerPositionsPositionsList[i]);
		}

	}

	private void createKingMoves() {
		kingNormalFieldsPositionsList = new List[NUMBER_OF_FIELDS];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				kingNormalFieldsPositionsList[position.getIdx()] = new LinkedList<>();

				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x + 1, y + 1);
				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x + 1, y);
				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x + 1, y - 1);

				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x, y - 1);
				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x, y + 1);

				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x - 1, y + 1);
				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x - 1, y);
				checkBoundsAndAdd(kingNormalFieldsPositionsList[position.getIdx()], x - 1, y - 1);
			}
		}
	}

	private void createKnightMoves() {
		knightMovesPositionsList = new List[NUMBER_OF_FIELDS];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				knightMovesPositionsList[position.getIdx()] = new LinkedList<>();
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x + 1, y + 2);
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x + 1, y - 2);
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x - 1, y + 2);
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x - 1, y - 2);

				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x + 2, y + 1);
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x - 2, y + 1);
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x + 2, y - 1);
				checkBoundsAndAdd(knightMovesPositionsList[position.getIdx()], x - 2, y - 1);
			}
		}
		for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
			final Position sourcePosition = getPosition(i);
			final List<Position> moves = getKnightMoves(sourcePosition);
			final Set<Position> knightDistanceTwoPositions = new HashSet<>();
			for (final Position distanceOneKnightPositions : moves) {
				knightDistanceTwoPositions.addAll(getKnightMoves(distanceOneKnightPositions));
			}
			knightDistanceTwoPositions.remove(sourcePosition);
			knightDistanceTwo[i] = listToBitBoard(knightDistanceTwoPositions);
		}
	}

	private void createStraights() {
		topVerticalPositionsList = new List[NUMBER_OF_FIELDS];
		bottomVerticalPositionsList = new List[NUMBER_OF_FIELDS];
		leftHorizontalPositionsList = new List[NUMBER_OF_FIELDS];
		rightHorizontalPositionsList = new List[NUMBER_OF_FIELDS];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				topVerticalPositionsList[position.getIdx()] = new LinkedList<>();
				bottomVerticalPositionsList[position.getIdx()] = new LinkedList<>();
				leftHorizontalPositionsList[position.getIdx()] = new LinkedList<>();
				rightHorizontalPositionsList[position.getIdx()] = new LinkedList<>();
				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(rightHorizontalPositionsList[position.getIdx()], x + i, y)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(leftHorizontalPositionsList[position.getIdx()], x - i, y)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(bottomVerticalPositionsList[position.getIdx()], x, y - i)) {
						break;
					}
				}

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(topVerticalPositionsList[position.getIdx()], x, y + i)) {
						break;
					}
				}

			}
		}
	}

	private void createDiagonals() {
		upperLeftDiagonalPositionsList = new List[NUMBER_OF_FIELDS];
		upperRightDiagonalPositionsList = new List[NUMBER_OF_FIELDS];
		lowerLeftDiagonalPositionsList = new List[NUMBER_OF_FIELDS];
		lowerRightDiagonalPositionsList = new List[NUMBER_OF_FIELDS];
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				upperLeftDiagonalPositionsList[position.getIdx()] = new LinkedList<>();
				upperRightDiagonalPositionsList[position.getIdx()] = new LinkedList<>();
				lowerLeftDiagonalPositionsList[position.getIdx()] = new LinkedList<>();
				lowerRightDiagonalPositionsList[position.getIdx()] = new LinkedList<>();

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(upperLeftDiagonalPositionsList[position.getIdx()], x - i, y + i)) {
						break;
					}
				}
				upperLeftDiagonal[position.getIdx()] = listToBitBoard(upperLeftDiagonalPositionsList[position.getIdx()]);

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(upperRightDiagonalPositionsList[position.getIdx()], x + i, y + i)) {
						break;
					}
				}
				upperRightDiagonal[position.getIdx()] = listToBitBoard(upperRightDiagonalPositionsList[position
						.getIdx()]);

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(lowerLeftDiagonalPositionsList[position.getIdx()], x - i, y - i)) {
						break;
					}
				}
				lowerLeftDiagonal[position.getIdx()] = listToBitBoard(lowerLeftDiagonalPositionsList[position.getIdx()]);

				for (int i = 1; i < 8; i++) {
					if (!checkBoundsAndAdd(lowerRightDiagonalPositionsList[position.getIdx()], x + i, y - i)) {
						break;
					}
				}
				lowerRightDiagonal[position.getIdx()] = listToBitBoard(lowerRightDiagonalPositionsList[position
						.getIdx()]);
			}
		}
	}

	public static long listToBitBoard(final Collection<Position> list) {
		long result = 0l;
		for (final Position position : list) {
			result |= position.getFieldBit();
		}
		return result;
	}

	private void createPawnMoves() {
		blackPawnNormalMovesPositionsList = new List[NUMBER_OF_FIELDS];
		whitePawnNormalMovesPositionsList = new List[NUMBER_OF_FIELDS];
		blackPawnCaptureMovesPositionsList = new List[NUMBER_OF_FIELDS];
		whitePawnCaptureMovesPositionsList = new List[NUMBER_OF_FIELDS];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				final Position position = fromZeroBasedCoordinates(x, y);
				blackPawnNormalMovesPositionsList[position.getIdx()] = new LinkedList<>();
				whitePawnNormalMovesPositionsList[position.getIdx()] = new LinkedList<>();
				blackPawnCaptureMovesPositionsList[position.getIdx()] = new LinkedList<>();
				whitePawnCaptureMovesPositionsList[position.getIdx()] = new LinkedList<>();
				checkBoundsAndAdd(blackPawnNormalMovesPositionsList[position.getIdx()], x, y - 1);
				if (y == 6) {
					checkBoundsAndAdd(blackPawnNormalMovesPositionsList[position.getIdx()], x, y - 2);
				}
				checkBoundsAndAdd(blackPawnCaptureMovesPositionsList[position.getIdx()], x + 1, y - 1);
				checkBoundsAndAdd(blackPawnCaptureMovesPositionsList[position.getIdx()], x - 1, y - 1);
				checkBoundsAndAdd(whitePawnNormalMovesPositionsList[position.getIdx()], x, y + 1);
				if (y == 1) {
					checkBoundsAndAdd(whitePawnNormalMovesPositionsList[position.getIdx()], x, y + 2);
				}
				checkBoundsAndAdd(whitePawnCaptureMovesPositionsList[position.getIdx()], x + 1, y + 1);
				checkBoundsAndAdd(whitePawnCaptureMovesPositionsList[position.getIdx()], x - 1, y + 1);
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
		return upperLeftDiagonalPositionsList[position.getIdx()];
	}

	/**
	 * @return the upperRightDiagonal
	 */
	public List<Position> getUpperRightDiagonal(final Position position) {
		return upperRightDiagonalPositionsList[position.getIdx()];
	}

	/**
	 * @return the lowerLeftDiagonal
	 */
	public List<Position> getLowerLeftDiagonal(final Position position) {
		return lowerLeftDiagonalPositionsList[position.getIdx()];
	}

	/**
	 * @return the lowerRightDiagonal
	 */
	public List<Position> getLowerRightDiagonal(final Position position) {
		return lowerRightDiagonalPositionsList[position.getIdx()];
	}

	/**
	 * @return the topVertical
	 */
	public List<Position> getTopVertical(final Position position) {
		return topVerticalPositionsList[position.getIdx()];
	}

	/**
	 * @return the bottomVertical
	 */
	public List<Position> getBottomVertical(final Position position) {
		return bottomVerticalPositionsList[position.getIdx()];
	}

	/**
	 * @return the leftHorizontal
	 */
	public List<Position> getLeftHorizontal(final Position position) {
		return leftHorizontalPositionsList[position.getIdx()];
	}

	/**
	 * @return the rightHorizontal
	 */
	public List<Position> getRightHorizontal(final Position position) {
		return rightHorizontalPositionsList[position.getIdx()];
	}

	/**
	 * @return the kingNormalFields
	 */
	public List<Position> getKingNormalFields(final Position position) {
		return kingNormalFieldsPositionsList[position.getIdx()];
	}

	/**
	 * @return the knightMoves
	 */
	public List<Position> getKnightMoves(final Position position) {
		return knightMovesPositionsList[position.getIdx()];
	}

	/**
	 * @return the blackPawnNormalMoves
	 */
	public List<Position> getBlackPawnNormalMoves(final Position position) {
		return blackPawnNormalMovesPositionsList[position.getIdx()];
	}

	/**
	 * @return the whitePawnNormalMoves
	 */
	public List<Position> getWhitePawnNormalMoves(final Position position) {
		return whitePawnNormalMovesPositionsList[position.getIdx()];
	}

	/**
	 * @return the blackPawnCaptureMoves
	 */
	public List<Position> getBlackPawnCaptureMoves(final Position position) {
		return blackPawnCaptureMovesPositionsList[position.getIdx()];
	}

	/**
	 * @return the whitePawnCaptureMoves
	 */
	public List<Position> getWhitePawnCaptureMoves(final Position position) {
		return whitePawnCaptureMovesPositionsList[position.getIdx()];
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

	public int binarySearchForIndex(final long king) {
		int result = -1;
		if (king == positionH8.getFieldBit()) {
			result = positionH8.getIdx();
		} else if (king == 0) {
			// do nothing...
		} else {
			int currentIndex = 32;

			for (int i = 0; i < binarySearchDelta.length; i++) {
				final Position position = getPosition(currentIndex);
				final long fieldBit = position.getFieldBit();
				if (king == fieldBit) {
					// found
					result = currentIndex;
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
	public Set<Position> getAllPossibleAttackerPositions(final Position position) {
		return allPossibleAttackerPositionsPositionsList[position.getIdx()];
	}

	/**
	 * @return the attackCheckPositions
	 */
	public Set<Position> getAllPossibleAttackerPositionsForIndex(final int fieldIndex) {
		return allPossibleAttackerPositionsPositionsList[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the upperLeftDiagonal
	 */
	public long getUpperLeftDiagonal(final int fieldIndex) {
		return upperLeftDiagonal[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the upperRightDiagonal
	 */
	public long getUpperRightDiagonal(final int fieldIndex) {
		return upperRightDiagonal[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the lowerLeftDiagonal
	 */
	public long getLowerLeftDiagonal(final int fieldIndex) {
		return lowerLeftDiagonal[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the lowerRightDiagonal
	 */
	public long getLowerRightDiagonal(final int fieldIndex) {
		return lowerRightDiagonal[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the topVertical
	 */
	public long getTopVertical(final int fieldIndex) {
		return topVertical[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the bottomVertical
	 */
	public long getBottomVertical(final int fieldIndex) {
		return bottomVertical[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the leftHorizontal
	 */
	public long getLeftHorizontal(final int fieldIndex) {
		return leftHorizontal[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the rightHorizontal
	 */
	public long getRightHorizontal(final int fieldIndex) {
		return rightHorizontal[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the kingNormalFields
	 */
	public long getKingNormalFields(final int fieldIndex) {
		return kingNormalFields[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the knightMoves
	 */
	public long getKnightMoves(final int fieldIndex) {
		return knightMoves[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the blackPawnNormalMoves
	 */
	public long getBlackPawnNormalMoves(final int fieldIndex) {
		return blackPawnNormalMoves[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the whitePawnNormalMoves
	 */
	public long getWhitePawnNormalMoves(final int fieldIndex) {
		return whitePawnNormalMoves[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the blackPawnCaptureMoves
	 */
	public long getBlackPawnCaptureMoves(final int fieldIndex) {
		return blackPawnCaptureMoves[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the whitePawnCaptureMoves
	 */
	public long getWhitePawnCaptureMoves(final int fieldIndex) {
		return whitePawnCaptureMoves[fieldIndex];
	}

	/**
	 * @param fieldIndex
	 *            given index of the field
	 * @return the allPossibleAttackerPositions
	 */
	public long getAllPossibleAttackerPositions(final int fieldIndex) {
		return allPossibleAttackerPositions[fieldIndex];
	}

	/**
	 * @param rankIdx
	 *            the rank index
	 * @return the ranks
	 */
	public long getRanks(final int rankIdx) {
		return ranks[rankIdx];
	}

	/**
	 * @param rankIdx
	 *            the rank index
	 * @return the ranksInverse
	 */
	public long getRanksInverse(final int rankIdx) {
		return ranksInverse[rankIdx];
	}

	/**
	 * @param fileIdx
	 *            File index
	 * @return the files
	 */
	public long getFiles(final int fileIdx) {
		return files[fileIdx];
	}

	/**
	 * @param fileIdx
	 *            File index
	 * @return the filesInverse
	 */
	public long getFilesInverse(final int fileIdx) {
		return filesInverse[fileIdx];
	}

	public long getKnightDistanceTwo(final Position target) {
		return getKnightDistanceTwo(target.getIdx());
	}

	public long getKnightDistanceTwo(final int idx) {
		return knightDistanceTwo[idx];
	}

	public long getWhiteFields() {
		return whiteFields;
	}

	public long getBlackFields() {
		return blackFields;
	}

}
