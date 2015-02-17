package de.neuenberger.ai.impl.chess.model.pieces;

import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.BitBoard;
import de.neuenberger.ai.impl.chess.model.BitBoard.Position;
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
	public void addPossiblePlies(final List<ChessPly> plies, final ChessBoard board, final BitBoard.Position source,
			final boolean checkSaveness) {
		int direction;
		int enPassantLine;
		boolean promotion;
		final int x = source.getX().ordinal();
		final int y = source.getY() - 1;

		List<Position> pawnCaptures;
		List<Position> pawnNormalMoves;

		if (getColor() == Color.WHITE) {
			direction = 1;
			promotion = source.getY() == 7;
			enPassantLine = 5;
			pawnCaptures = board.getWhitePawnCaptureMoves(source);
			pawnNormalMoves = board.getWhitePawnNormalMoves(source);
		} else if (getColor() == Color.BLACK) {
			direction = -1;
			promotion = source.getY() == 2;
			enPassantLine = 4;
			pawnCaptures = board.getBlackPawnCaptureMoves(source);
			pawnNormalMoves = board.getBlackPawnNormalMoves(source);
		} else {
			throw new IllegalStateException();
		}

		for (final Position target : pawnCaptures) {
			checkIfIsCaptureAndIfSoAdd(plies, board, source, checkSaveness, promotion, target);
		}

		// en passant.
		final ChessPly lastPly = board.getLastPly();

		if (!promotion && lastPly != null && lastPly.getPiece() instanceof Pawn) {
			final int steps = Math.abs(lastPly.getSource().getY() - lastPly.getTarget().getY());
			if (lastPly.getTarget().getY() == y && y == enPassantLine && steps == 2) {
				if (lastPly.getTarget().getX().ordinal() == x - 1) {
					// capture en passant to -1
				} else if (lastPly.getTarget().getX().ordinal() == x + 1) {
					// capture en passant to +1
				}
			}
		}

		for (final Position target : pawnNormalMoves) {
			final Piece pieceAt = board.getPieceAt(target);
			if (pieceAt == null) { // ok.
				final boolean capture = false;
				final boolean check = false;
				createPlyValidateAndAddToList(board, plies, source, promotion, target, null, check, checkSaveness);
			} else {
				break;
			}
		}
	}

	private void checkIfIsCaptureAndIfSoAdd(final List<ChessPly> plies, final ChessBoard board,
			final BitBoard.Position source, final boolean checkSaveness, final boolean promotion,
			final BitBoard.Position target) {
		final Piece pieceAt = board.getPieceAt(target);
		if (pieceAt != null && pieceAt.getColor() != getColor()) {
			createPlyValidateAndAddToList(board, plies, source, promotion, target, pieceAt, false, checkSaveness);
		}
	}

	private void createPlyValidateAndAddToList(final ChessBoard board, final List<ChessPly> plies,
			final BitBoard.Position source, final boolean promotion, final BitBoard.Position target,
			final Piece capture, final boolean check, final boolean checkSaveness) {
		if (promotion) {
			final Piece[] promotionPieces = getPromotionPieces();
			for (final Piece piece : promotionPieces) {
				final PromotionPly ply = new PromotionPly(this, source, target, piece, capture, check);
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
			final ChessPly ply = new ChessPly(this, source, target, capture, check);
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
