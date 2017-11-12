package de.neuenberger.ai.impl.chess.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BasePlyList implements PlyList {
	final List<ChessPly> chessPlyList;

	public BasePlyList() {
		this(new ArrayList<ChessPly>(50));
	}

	public BasePlyList(final List<ChessPly> chessPlyList) {
		this.chessPlyList = chessPlyList;
	}

	@Override
	public void add(final ChessPly chessPly) {
		chessPlyList.add(chessPly);
	}

	@Override
	public List<ChessPly> getCollection() {
		return Collections.unmodifiableList(chessPlyList);
	}
}
