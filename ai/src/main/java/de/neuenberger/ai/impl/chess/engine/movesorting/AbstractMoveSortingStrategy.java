package de.neuenberger.ai.impl.chess.engine.movesorting;

public abstract class AbstractMoveSortingStrategy implements MoveSortingStrategy {

	private MoveSortingStrategy nextMoveSortingStrategy;

	@Override
	public void setNextMoveSortingStrategy(final MoveSortingStrategy fifoStrategy) {
		nextMoveSortingStrategy = fifoStrategy;
	}

	/**
	 * @return the nextMoveSortingStrategy
	 */
	@Override
	public MoveSortingStrategy getNextMoveSortingStrategy() {
		return nextMoveSortingStrategy;
	}

}
