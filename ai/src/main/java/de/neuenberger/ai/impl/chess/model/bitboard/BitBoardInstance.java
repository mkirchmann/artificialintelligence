package de.neuenberger.ai.impl.chess.model.bitboard;

import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.Piece.PieceType;

public class BitBoardInstance {

	/**
	 * One Board per piece and color Long[Color][Piece] .
	 */
	private final long[][] pieceBitBoard;
	/**
	 * One Board per Color
	 */
	private final long[] colorBitBoard;

	private final BitBoardPreCalculations instance;

	public BitBoardInstance(final BitBoardPreCalculations instance) {
		this.instance = instance;
		pieceBitBoard = new long[2][6];
		colorBitBoard = new long[2];
	}

	@Override
	public BitBoardInstance clone() {
		final BitBoardInstance clone = new BitBoardInstance(this.instance);
		for (int c = 0; c < 2; c++) {
			clone.colorBitBoard[c] = colorBitBoard[c];
			for (int p = 0; p < 6; p++) {
				clone.pieceBitBoard[c][p] = pieceBitBoard[c][p];
			}
		}
		return clone;
	}

	/**
	 * 
	 * @param color
	 *            given Color
	 * @param pieceType
	 *            given PieceType
	 * @return
	 */
	public long getPieceBitBoard(final Color color, final PieceType pieceType) {
		return pieceBitBoard[color.ordinal()][pieceType.ordinal()];
	}

	/**
	 * set the piece board
	 * 
	 * @param color
	 *            given Color
	 * @param pieceType
	 *            given PieceType
	 * @param value
	 *            given value
	 */
	public void setPieceBitBoard(final Color color, final PieceType pieceType, final long value) {
		pieceBitBoard[color.ordinal()][pieceType.ordinal()] = value;
	}

	/**
	 * @return the colorBitBoard
	 */
	public long getColorBitBoard(final Color color) {
		return colorBitBoard[color.ordinal()];
	}

	/**
	 * set the colorBitBoard
	 * 
	 * @param color
	 *            given Color
	 * @param value
	 *            given value
	 */
	public void setColorBitBoard(final Color color, final long value) {
		colorBitBoard[color.ordinal()] = value;
	}

	public void movePiece(final Piece movedPiece, final Piece capturedPiece, final Position source,
			final Position target) {
		final int colorIndex = movedPiece.getColor().ordinal();
		final int otherColorIndex = movedPiece.getColor().getOtherColor().ordinal();
		final int pieceIndex = movedPiece.getPieceType().ordinal();
		colorBitBoard[colorIndex] = (colorBitBoard[colorIndex] | target.getFieldBit()) & source.getInverseFieldBit();
		pieceBitBoard[colorIndex][pieceIndex] = (pieceBitBoard[colorIndex][pieceIndex] | target.getFieldBit())
				& source.getInverseFieldBit();
		if (capturedPiece != null) {
			colorBitBoard[otherColorIndex] &= target.getInverseFieldBit();
			pieceBitBoard[otherColorIndex][capturedPiece.getPieceType().ordinal()] &= target.getInverseFieldBit();
		}
	}

	public void setPiece(final Piece piece, final Position target) {
		final PieceType[] pieceTypes = PieceType.values();
		final int otherColorIndex;
		if (piece == null) {
			otherColorIndex = Color.BLACK.ordinal();
			final int colorIndex = Color.WHITE.ordinal();
			colorBitBoard[colorIndex] &= target.getInverseFieldBit();
			clearPieceBoardsAtTarget(target, pieceTypes, colorIndex);
		} else {
			final int colorIndex = piece.getColor().ordinal();
			otherColorIndex = piece.getColor().getOtherColor().ordinal();
			final int pieceIndex = piece.getPieceType().ordinal();
			colorBitBoard[colorIndex] |= target.getFieldBit();
			colorBitBoard[otherColorIndex] &= target.getInverseFieldBit();
			pieceBitBoard[colorIndex][pieceIndex] |= target.getFieldBit();
		}
		clearPieceBoardsAtTarget(target, pieceTypes, otherColorIndex);
	}

	private void clearPieceBoardsAtTarget(final Position target, final PieceType[] pieceTypes, final int otherColorIndex) {
		for (final PieceType pieceType : pieceTypes) {
			pieceBitBoard[otherColorIndex][pieceType.ordinal()] &= target.getInverseFieldBit();
		}
	}

	public int getPieceScore() {
		final PieceType[] values = PieceType.values();
		int sum = 0;
		for (final PieceType pieceType : values) {
			sum += pieceType.getCentiPawns()
					* (Long.bitCount(pieceBitBoard[0][pieceType.ordinal()]) - Long.bitCount(pieceBitBoard[1][pieceType
							.ordinal()]));
		}
		return sum;
	}

	@Override
	public String toString() {
		final char board[] = new char[64];
		final PieceType[] values = PieceType.values();
		final Color[] colors = Color.values();
		for (int i = 0; i < 64; i++) {
			board[i] = '-';
			outer: for (final Color color : colors) {
				for (final PieceType pieceType : values) {
					if ((pieceBitBoard[color.ordinal()][pieceType.ordinal()] & instance.getPosition(i).getFieldBit()) != 0) {
						char rep;
						if (color == Color.BLACK) {
							rep = ("" + pieceType.getRepresentation()).toLowerCase().charAt(0);
						} else {
							rep = pieceType.getRepresentation();
						}
						board[i] = rep;
						break outer;
					}
				}
			}
		}
		final StringBuilder builder = new StringBuilder();
		for (int y = 7; y >= 0; y--) {
			builder.append("|");
			builder.append(y + 1);
			for (int x = 0; x < 8; x++) {
				builder.append('|');
				final Position position = instance.fromZeroBasedCoordinates(x, y);
				builder.append(board[position.getIdx()]);
			}
			builder.append("|\n");
		}
		builder.append("| |A|B|C|D|E|F|G|H|");
		return builder.toString();
	}

	public String debugLong(final long value) {
		final char board[] = new char[64];
		for (int i = 0; i < 64; i++) {
			board[i] = '0';
			final PieceType[] values = PieceType.values();
			final Color[] colors = Color.values();
			if ((value & instance.getPosition(i).getFieldBit()) != 0) {
				final char rep = '1';
				board[i] = rep;
			}
		}
		final StringBuilder builder = new StringBuilder();
		for (int y = 7; y >= 0; y--) {
			builder.append("|");
			builder.append(y + 1);
			for (int x = 0; x < 8; x++) {
				builder.append('|');
				final Position position = instance.fromZeroBasedCoordinates(x, y);
				builder.append(board[position.getIdx()]);
			}
			builder.append("|\n");
		}
		builder.append("| |A|B|C|D|E|F|G|H|");
		return builder.toString();
	}

	public String debugInfo() {
		final PieceType[] values = PieceType.values();
		final Color[] colors = Color.values();
		String result = "";
		for (final Color color : colors) {
			result += "info: color: " + color + "\n";
			result += debugLong(colorBitBoard[color.ordinal()]);
			result += "\n";
			for (final PieceType pieceType : values) {
				result += "info: color: " + color + ", pieceType: " + pieceType + "\n";
				result += debugLong(pieceBitBoard[color.ordinal()][pieceType.ordinal()]);
				result += "\n";
			}
		}
		return result;
	}

	/**
	 * 
	 * @param color
	 * @return Returns a bitmask of all positions defended by a pawn.
	 */
	public long getPawnDefendedFields(final Color color) {
		final long pawnBoard = getPieceBitBoard(color, PieceType.PAWN);
		long result;
		if (color == Color.BLACK) {
			result = (pawnBoard & instance.getRanksInverse(7)) >> 7 | (pawnBoard & instance.getRanksInverse(0)) >> 9;
		} else {
			result = (pawnBoard & instance.getRanksInverse(0)) << 7 | (pawnBoard & instance.getRanksInverse(7)) << 9;
		}
		return result;
	}
}
