package de.neuenberger.ai.impl.chess.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class BasePlyList implements PlyList {
	final List<ChessPly> chessPlyList;
	final List<ChessPly> unmodifiablePlyList;

	public BasePlyList() {
		this(new LinkedList<ChessPly>());
	}

	public BasePlyList(final List<ChessPly> chessPlyList) {
		this.chessPlyList = chessPlyList;
		unmodifiablePlyList = Collections.unmodifiableList(chessPlyList);
	}

	@Override
	public void add(final ChessPly chessPly) {
		chessPlyList.add(chessPly);
	}

	@Override
	public List<ChessPly> getCollection() {
		return unmodifiablePlyList;
	}
}
