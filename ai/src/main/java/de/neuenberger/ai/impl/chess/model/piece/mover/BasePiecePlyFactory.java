package de.neuenberger.ai.impl.chess.model.piece.mover;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class BasePiecePlyFactory {
	private final Piece piece;

	public BasePiecePlyFactory(final Piece piece) {
		this.piece = piece;

	}

	public boolean checkPieceAndAddPly(final List<ChessPly> plies, final ChessBoard board, final int sourceX,
			final int sourceY, final int newX, final int newY, final boolean checkOwnKingsafeness) {
		final boolean doBreak;
		if (board.checkCoordinatesValid(newX, newY)) {
			final Piece pieceAt = board.getPieceAt(newX, newY);
			if (pieceAt != null) {
				if (pieceAt.getColor() != getColor()) { // different color, it
														// is a
														// capture move.
					final ChessPly ply = new ChessPly(piece, sourceX, sourceY, newX, newY, true, false);
					checkValidityOfPlyAndAdd(plies, board, checkOwnKingsafeness, ply);
				}
				doBreak = true;
			} else {
				final ChessPly ply = new ChessPly(piece, sourceX, sourceY, newX, newY, false, false);
				checkValidityOfPlyAndAdd(plies, board, checkOwnKingsafeness, ply);
				doBreak = false;
			}
		} else {
			doBreak = true;
		}
		return doBreak;
	}

	private Color getColor() {
		return piece.getColor();
	}

	private void checkValidityOfPlyAndAdd(final List<ChessPly> plies, final ChessBoard board,
			final boolean checkOwnKingsafeness, final ChessPly ply) {
		final boolean add;
		if (checkOwnKingsafeness) {
			final ChessBoard targetBoard = board.apply(ply);
			add = !targetBoard.isInCheck(getColor());
			ply.setCheck(targetBoard.isInCheck(getColor().getOtherColor()));
		} else {
			add = true;
		}
		if (add) {
			plies.add(ply);
		}
	}
}
