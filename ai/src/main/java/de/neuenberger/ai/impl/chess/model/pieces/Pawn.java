package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.plies.PromotionPly;

public class Pawn extends Piece {

	private transient Piece[] promotionPieces;

	public Pawn(final Color color) {
		super('P', color, 10);
	}

	@Override
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final int x, final int y,
			final boolean checkSaveness) {
		boolean moveTwo;
		int direction;
		int enPassantLine;
		boolean promotion;
		if (getColor() == Color.WHITE) {
			moveTwo = y == 1;
			direction = 1;
			promotion = y == 6;
			enPassantLine = 4;
		} else if (getColor() == Color.BLACK) {
			moveTwo = y == 6;
			direction = -1;
			promotion = y == 1;
			enPassantLine = 3;
		} else {
			throw new IllegalStateException();
		}

		if (x > board.getMinX()) {
			final int newX = x - 1;
			final int newY = y + direction;
			checkIfIsCaptureAndIfSoAdd(plies, board, x, y, checkSaveness, promotion, newX, newY);
		}
		if (x < board.getMaxX()) {
			final int newX = x + 1;
			final int newY = y + direction;
			checkIfIsCaptureAndIfSoAdd(plies, board, x, y, checkSaveness, promotion, newX, newY);
		}

		// en passant.
		final ChessPly lastPly = board.getLastPly();

		if (!promotion && lastPly != null && lastPly.getPiece() instanceof Pawn) {
			final int steps = Math.abs(lastPly.getSourceY() - lastPly.getTargetY());
			if (lastPly.getTargetY() == y && y == enPassantLine && steps == 2) {
				if (lastPly.getTargetX() == x - 1) {
					// capture en passant to -1
				} else if (lastPly.getTargetX() == x + 1) {
					// capture en passant to +1
				}
			}
		}

		for (int i = 0; i < 2; i++) {
			final int newY = y + i * direction + direction;
			final boolean valid = board.checkCoordinatesValid(x, newY);
			if (!valid) {
				break;
			}
			final Piece pieceAt = board.getPieceAt(x, newY);
			if (pieceAt == null) { // ok.
				final boolean capture = false;
				final boolean check = false;
				createPlyValidateAndAddToList(board, plies, x, y, promotion, x, newY, null, check, checkSaveness);
			} else {
				break;
			}
			if (!moveTwo) {
				break;
			}
		}
	}

	private void checkIfIsCaptureAndIfSoAdd(final List<ChessPly> plies, final ChessBoard board, final int x,
			final int y, final boolean checkSaveness, final boolean promotion, final int newX, final int newY) {
		final Piece pieceAt = board.getPieceAt(newX, newY);
		if (pieceAt != null && pieceAt.getColor() != getColor()) {
			createPlyValidateAndAddToList(board, plies, x, y, promotion, newX, newY, pieceAt, false, checkSaveness);
		}
	}

	private void createPlyValidateAndAddToList(final ChessBoard board, final List<ChessPly> plies, final int x,
			final int y, final boolean promotion, final int newX, final int newY, final Piece capture,
			final boolean check, final boolean checkSaveness) {
		if (promotion) {
			final Piece[] promotionPieces = getPromotionPieces();
			for (final Piece piece : promotionPieces) {
				final PromotionPly ply = new PromotionPly(this, x, y, newX, newY, piece, capture, check);
				final boolean isCheck;
				if (checkSaveness) {
					final ChessBoard applied = board.apply(ply);
					isCheck = applied.isInCheck(getColor());
					ply.setCheck(applied.isInCheck(getColor().getOtherColor()));
				} else {
					isCheck = false;
				}
				if (isCheck) { // not a valid move.
					break;
				} else {
					plies.add(ply);
				}
			}
		} else {
			final ChessPly ply = new ChessPly(this, x, y, newX, newY, capture, check);
			boolean valid;
			if (checkSaveness) {
				final Board<Piece, Color, ChessPly> applied = board.apply(ply);
				valid = !applied.isInCheck(getColor());
			} else {
				valid = true;
			}
			if (valid) {
				plies.add(ply);
			}
		}
	}

	private Piece[] getPromotionPieces() {
		if (promotionPieces == null) {
			promotionPieces = new Piece[] { new Queen(getColor()), new Rook(getColor()), new Knight(getColor()),
					new Bishop(getColor()) };
		}
		return promotionPieces;
	}

	@Override
	public String getUnicode() {
		return (isWhite()) ? "\u2659" : "\u265F";
	}
}
