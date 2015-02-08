package de.neuenberger.ai.base.model;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.Piece.Color;

public interface Board<O, C, P extends Ply> {
	List<P> getPossiblePlies(C c);

	O getPieceAt(int x, int y);

	int getMinX();

	int getMaxX();

	int getMaxY();

	int getMinY();

	Board<O, C, P> apply(P p);

	boolean isAttackedByOpponent(int x, int y, Color color);

	boolean checkCoordinatesValid(final int newX, final int newY);

	boolean isInCheck(Color color);

	P getLastPly();
}
