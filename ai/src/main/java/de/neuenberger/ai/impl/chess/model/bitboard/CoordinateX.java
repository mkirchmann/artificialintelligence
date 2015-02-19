package de.neuenberger.ai.impl.chess.model.bitboard;

public enum CoordinateX {
	A, B, C, D, E, F, G, H;
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	};
}