package de.neuenberger.ai.impl.chess.model;

import java.util.List;

public interface PlyList {
	public void add(ChessPly chessPly);

	public List<ChessPly> getCollection();
}
