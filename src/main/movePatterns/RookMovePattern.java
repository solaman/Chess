package main.movePatterns;

import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.pieces.ChessPiece;

/**
 * defines the behavior of the basic rook move
 * @author Solaman
 */
public class RookMovePattern extends MovePattern {

	/**
	 * @param owner -owner of the piece
	 */
	public RookMovePattern(ChessPiece owner) {
		super(owner);
	}
	
	@Override
	public void buildMoveData(ChessBoard board) {
		buildMoveDataVector(board, 0, 1, -2);
		buildMoveDataVector(board, 0, -1, -2);
		buildMoveDataVector(board, 1, 0, -2);
		buildMoveDataVector(board, -1, 0, -2);
		
	}
	
	/*
	 * TODO requires Testing
	 */
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildMoveDataVector(board, 1, 1, -2);
		buildMoveDataVector(board, 1, -1, -2);
		buildMoveDataVector(board, -1, 1, -2);
		buildMoveDataVector(board, -1, -1, -2);
		
		buildMoveDataVector(board, 0, -2, -2);
		buildMoveDataVector(board, 0, 2, -2);
	}

	@Override
	public MovePattern copy(ChessPiece owner) {
		return new RookMovePattern(owner);
	}
	
}
