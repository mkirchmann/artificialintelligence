package de.neuenberger.ai.impl.chess.model.piece.mover;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.BitBoard;
import de.neuenberger.ai.impl.chess.model.ChessBoard;
import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.Piece;
import de.neuenberger.ai.impl.chess.model.Piece.Color;

public class BasePiecePlyFactory {
	private final Piece piece;

	public BasePiecePlyFactory(final Piece piece) {
		this.piece = piece;

	}

	/**
	 * 
	 * @param plies
	 * @param board
	 * @param sourceX
	 *            source coordinate x
	 * @param sourceY
	 *            source coordinate y
	 * @param newX
	 *            target coordinate x
	 * @param newY
	 *            target coordinate y
	 * @param checkOwnKingsafeness
	 * @return Returns true if there was any piece.
	 */
	public boolean checkPieceAndAddPly(final List<ChessPly> plies, final ChessBoard board,
			final BitBoard.Position source, final BitBoard.Position target, final boolean checkOwnKingsafeness) {
		final boolean doBreak;
		final Piece pieceAt = board.getPieceAt(target);
		if (pieceAt != null) {
			if (pieceAt.getColor() != getColor()) { // different color, it
													// is a
													// capture move.
				final ChessPly ply = new ChessPly(piece, source, target, pieceAt, false);
				checkValidityOfPlyAndAdd(plies, board, checkOwnKingsafeness, ply);
			}
			doBreak = true;
		} else {
			final ChessPly ply = new ChessPly(piece, source, target, null, false);
			checkValidityOfPlyAndAdd(plies, board, checkOwnKingsafeness, ply);
			doBreak = false;
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
