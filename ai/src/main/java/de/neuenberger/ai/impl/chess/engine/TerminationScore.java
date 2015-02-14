package de.neuenberger.ai.impl.chess.engine;

public enum TerminationScore {
	STALEMATE(0, "Stalemate"), MATE(2100000000, "#"), MATED(-2100000000, "-#");

	private final Integer internalScore;
	private final String addition;

	TerminationScore(final Integer internalScore, final String addition) {
		this.internalScore = internalScore;
		this.addition = addition;
	}

	public int compare(final Object o) {
		int result;
		if (o instanceof Integer) {
			result = internalScore.compareTo((Integer) o);
		} else if (o instanceof TerminationScore) {
			result = internalScore.compareTo(((TerminationScore) o).internalScore);
		} else {
			throw new IllegalArgumentException();
		}
		return result;
	}

	@Override
	public String toString() {
		return addition;
	}

	public int getScoreEquivalent() {
		return internalScore;
	}
}