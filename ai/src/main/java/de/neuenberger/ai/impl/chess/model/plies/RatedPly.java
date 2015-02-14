package de.neuenberger.ai.impl.chess.model.plies;

import de.neuenberger.ai.impl.chess.model.ChessPly;

public class RatedPly {
	private final Integer score;
	private final ChessPly chessPly;

	public RatedPly(final ChessPly chessPly, final int score) {
		this.chessPly = chessPly;
		this.score = score;
	}

	/**
	 * @return the chessPly
	 */
	public ChessPly getChessPly() {
		return chessPly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RatedPly [score=" + score + ", chessPly=" + chessPly + "]";
	}

	/**
	 * @return the score
	 */
	public Integer getScore() {
		return score;
	}
}
