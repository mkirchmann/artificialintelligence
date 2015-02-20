package de.neuenberger.ai.impl.chess.model.delegate;

import java.util.List;

import de.neuenberger.ai.impl.chess.model.ChessPly;
import de.neuenberger.ai.impl.chess.model.PlyList;
import de.neuenberger.ai.impl.chess.model.bitboard.Position;

public class CheckerPlyList implements PlyList {

	private final Position position;
	private final RuntimeException runtimeException;

	public CheckerPlyList(final Position position, final RuntimeException rte) {
		this.position = position;
		runtimeException = rte;
	}

	@Override
	public void add(final ChessPly chessPly) {
		if (chessPly.getTarget() == position) {
			throw runtimeException;
		}
	}

	@Override
	public List<ChessPly> getCollection() {
		return null;
	}

}
