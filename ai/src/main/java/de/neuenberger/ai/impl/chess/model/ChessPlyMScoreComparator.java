package de.neuenberger.ai.impl.chess.model;

import java.util.Comparator;

public class ChessPlyMScoreComparator implements Comparator<ChessPly> {

	@Override
	public int compare(final ChessPly o1, final ChessPly o2) {
		return o2.getMoveDeltaScore().compareTo(o1.getMoveDeltaScore());
	}

}
