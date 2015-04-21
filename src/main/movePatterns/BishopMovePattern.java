package main.movePatterns;

import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.pieces.ChessPiece;


/**
 * defines behavior for the basic Bishop move
 * @author Solaman
 */
public final class BishopMovePattern extends MovePattern {
	
	/**
	 * @param owner- owner of move
	 */
	public BishopMovePattern(ChessPiece owner) {
		super(owner);
	}

	@Override
	public void buildMoveData(ChessBoard board) {
		buildMoveDataVector(board, 1, 1, -2);
		buildMoveDataVector(board, -1, -1, -2);
		buildMoveDataVector(board, 1, -1, -2);
		buildMoveDataVector(board, -1, 1, -2);
	}
	
	/*
	 * TODO requires testing
	 */
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildMoveDataVector(board, 1, 3, -2);
		buildMoveDataVector(board, -1, 3, -2);
		buildMoveDataVector(board, 2, 0, -2);
		buildMoveDataVector(board, -2, 0, -2);
		buildMoveDataVector(board, 1, -3, -2);
		buildMoveDataVector(board, -1, -3, -2);
	}

	@Override
	public MovePattern copy(ChessPiece owner) {
		return new BishopMovePattern(owner);
	}
	
}
