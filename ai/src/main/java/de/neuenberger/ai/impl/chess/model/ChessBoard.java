package de.neuenberger.ai.impl.chess.model;

import java.util.LinkedList;
import java.util.List;

import de.neuenberger.ai.base.model.Board;
import de.neuenberger.ai.impl.chess.model.Piece.Color;
import de.neuenberger.ai.impl.chess.model.pieces.Bishop;
import de.neuenberger.ai.impl.chess.model.pieces.King;
import de.neuenberger.ai.impl.chess.model.pieces.Knight;
import de.neuenberger.ai.impl.chess.model.pieces.Pawn;
import de.neuenberger.ai.impl.chess.model.pieces.Queen;
import de.neuenberger.ai.impl.chess.model.pieces.Rook;

public class ChessBoard implements Board<Piece, Color, ChessPly> {

	private final Piece[][] boardContents;
	private ChessPly lastPly;

	public ChessBoard() {
		boardContents = new Piece[8][8];
	}

	@Override
	public List<ChessPly> getPossiblePlies(final Color color) {
		final List<ChessPly> result = new LinkedList<>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				final Piece piece = boardContents[y][x];
				if (piece != null && piece.getColor() == color) {
					piece.addPossiblePlies(result, this, x, y, true);
				}
			}
		}
		return result;
	}

	@Override
	public ChessBoard clone() {
		final ChessBoard result = new ChessBoard();

		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				final Piece piece = boardContents[y][x];
				if (piece != null) {
					result.boardContents[y][x] = piece;
				}
			}
		}

		return result;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		for (int y = 7; y >= 0; y--) {
			for (int x = 0; x < 8; x++) {
				final Piece piece = boardContents[y][x];
				if (piece != null) {
					builder.append(piece);
				} else {
					builder.append("-");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	@Override
	public Piece getPieceAt(final int x, final int y) {
		return boardContents[y][x];
	}

	@Override
	public int getMinX() {
		return 0;
	}

	@Override
	public int getMaxX() {
		return 7;
	}

	@Override
	public int getMaxY() {
		return 7;
	}

	@Override
	public int getMinY() {
		return 0;
	}

	@Override
	public Board<Piece, Color, ChessPly> apply(final ChessPly p) {
		final ChessBoard clone = clone();
		clone.lastPly = p;
		final BoardChangerImpl boardChanger = clone.createBoardChanger();
		p.applyTo(boardChanger);
		boardChanger.invalidate();

		return clone;
	}

	public static ChessBoard createInitialChessBoard() {
		final ChessBoard chessBoard = new ChessBoard();
		final Pawn blackPawn = new Pawn(Color.BLACK);
		final Pawn whitePawn = new Pawn(Color.WHITE);
		for (int i = 0; i < 8; i++) {
			chessBoard.boardContents[1][i] = whitePawn;
			chessBoard.boardContents[6][i] = blackPawn;
		}

		final Rook blackRook = new Rook(Color.BLACK);
		final Rook whiteRook = new Rook(Color.WHITE);
		chessBoard.boardContents[0][0] = whiteRook;
		chessBoard.boardContents[0][7] = whiteRook;
		chessBoard.boardContents[7][0] = blackRook;
		chessBoard.boardContents[7][7] = blackRook;

		final Piece whiteKnight = new Knight(Color.WHITE);
		final Piece blackKnight = new Knight(Color.BLACK);
		chessBoard.boardContents[0][1] = whiteKnight;
		chessBoard.boardContents[0][6] = whiteKnight;
		chessBoard.boardContents[7][1] = blackKnight;
		chessBoard.boardContents[7][6] = blackKnight;

		final Piece whiteBishop = new Bishop(Color.WHITE);
		final Piece blackBishop = new Bishop(Color.BLACK);
		chessBoard.boardContents[0][2] = whiteBishop;
		chessBoard.boardContents[0][5] = whiteBishop;
		chessBoard.boardContents[7][2] = blackBishop;
		chessBoard.boardContents[7][5] = blackBishop;

		chessBoard.boardContents[0][3] = new Queen(Color.WHITE);
		chessBoard.boardContents[7][3] = new Queen(Color.BLACK);
		chessBoard.boardContents[0][4] = new King(Color.WHITE);
		chessBoard.boardContents[7][4] = new King(Color.BLACK);

		return chessBoard;
	}

	private BoardChangerImpl createBoardChanger() {
		return new BoardChangerImpl(this);
	}

	@Override
	public boolean isAttackedByOpponent(final int targetX, final int targetY, final Color color) {
		final List<ChessPly> tempList = new LinkedList<>();
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				final Piece piece = boardContents[y][x];
				if (piece != null && piece.getColor() != color) {
					piece.addPossiblePlies(tempList, this, x, y, false);
					for (final ChessPly ply : tempList) {
						if (ply.getTargetX() == targetX && ply.getTargetY() == targetY) {
							return true;
						}
					}
					tempList.clear();
				}
			}
		}
		return false;
	}

	@Override
	public boolean checkCoordinatesValid(final int newX, final int newY) {
		return getMinX() <= newX && newX <= getMaxX() && getMinY() <= newY && newY <= getMaxY();
	}

	@Override
	public boolean isInCheck(final Color color) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				final Piece piece = boardContents[y][x];
				if (piece != null && piece.getRepresentation() == 'K' && piece.getColor() == color) {
					// found king.
					return isAttackedByOpponent(x, y, color);
				}
			}
		}
		return false;
	}

	@Override
	public ChessPly getLastPly() {
		return lastPly;
	}

	private void movePiece(final int sourceX, final int sourceY, final int targetX, final int targetY) {
		final Piece piece = boardContents[sourceY][sourceX];
		boardContents[sourceY][sourceX] = null;
		boardContents[targetY][targetX] = piece;
	}

	private void setPieceAt(final int targetX, final int targetY, final Piece piece) {
		boardContents[targetY][targetX] = piece;
	}

	public static class BoardChangerImpl implements BoardChanger {
		boolean valid = true;

		final ChessBoard chessBoard;

		public void invalidate() {
			valid = false;
		}

		public BoardChangerImpl(final ChessBoard chessBoard) {
			this.chessBoard = chessBoard;
		}

		@Override
		public void movePiece(final int sourceX, final int sourceY, final int targetX, final int targetY) {
			checkValid();
			chessBoard.movePiece(sourceX, sourceY, targetX, targetY);
		}

		@Override
		public void setPieceAt(final int targetX, final int targetY, final Piece piece) {
			checkValid();
			chessBoard.setPieceAt(targetX, targetY, piece);
		}

		private void checkValid() {
			if (!valid) {
				throw new IllegalStateException("Board Changer has been invalidated.");
			}
		}

	}

}
