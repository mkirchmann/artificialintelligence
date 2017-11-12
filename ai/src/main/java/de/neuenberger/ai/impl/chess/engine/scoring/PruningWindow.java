package de.neuenberger.ai.impl.chess.engine.scoring;

import de.neuenberger.ai.impl.chess.engine.PlyResult;

public class PruningWindow {
	private PlyResult alpha;
	private PlyResult beta;
	private PlyResult bestScore;

	public PruningWindow clone() {
		PruningWindow clone = new PruningWindow();
		clone.setAlpha(alpha);
		clone.setBeta(beta);
		return clone;
	}

	public PlyResult getAlpha() {
		return alpha;
	}

	public void setAlpha(PlyResult alpha) {
		this.alpha = alpha;
	}

	public PlyResult getBeta() {
		return beta;
	}

	public void setBeta(PlyResult beta) {
		this.beta = beta;
	}

	public void useAlphaIfBetter(PlyResult currentScore) {
		if (alpha == null) {
			alpha = currentScore;
		} else if (alpha.compareTo(currentScore) < 0) {
			alpha = currentScore;
		}
	}

	public void useBetaIfBetter(PlyResult currentScore) {
		if (beta == null) {
			beta = currentScore;
		} else {
			if (beta.compareTo(currentScore) > 0) {
				beta = currentScore;
			}
		}
	}

	public boolean cutOffConditionMet() {
		return beta != null && alpha != null && beta.compareTo(alpha) < 0;
	}

	public void setBestScore(PlyResult currentScore) {
		bestScore = currentScore;
	}

	public PlyResult getBestScore() {
		return bestScore;
	}

	public boolean hasBestScore() {
		return bestScore != null;
	}

}
