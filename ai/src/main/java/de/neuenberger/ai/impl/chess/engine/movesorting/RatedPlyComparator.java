package de.neuenberger.ai.impl.chess.engine.movesorting;

import java.util.Comparator;

import de.neuenberger.ai.impl.chess.model.plies.RatedPly;

public class RatedPlyComparator implements Comparator<RatedPly> {

	@Override
	public int compare(final RatedPly o1, final RatedPly o2) {
		return o2.getScore().compareTo(o1.getScore());
	}

}
