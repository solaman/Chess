package main.movePatterns;


import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.pieces.ChessPiece;

/**
 * Defines the behavior of the Basic Knight maneuver
 * @author Solaman
 */
public final class KnightMovePattern extends MovePattern {
	
	/**
	 * @param owner -owner of move
	 */
	public KnightMovePattern(ChessPiece owner) {
		super(owner);
	}

	@Override
	public void buildMoveData(ChessBoard board) {	
		buildMoveDataVector(board, 1, 2, 1);
		buildMoveDataVector(board, 1, -2, 1);
		buildMoveDataVector(board, -1, 2, 1);
		buildMoveDataVector(board, -1, -2, 1);
		buildMoveDataVector(board, 2, 1, 1);
		buildMoveDataVector(board, 2, -1, 1);
		buildMoveDataVector(board, -2, 1, 1);
		buildMoveDataVector(board, -2, -1, 1);
	
	}
	
	/*
	 * TODO should be tested
	 */
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildMoveDataVector(board, 1, 2, 1);
		buildMoveDataVector(board, -1, 2, 1);
		buildMoveDataVector(board, 2, 1 ,1);
		buildMoveDataVector(board, 2, -1, 1);
		
		buildMoveDataSpaces(board, -1, 5,  -1, -5,  1, 5, 1, -5,
				-2, -4,  -2, 4,  2, -4,  2, 4,  
				-3, 1,  3, 1,   3, -1,   -3, -1);
	}

	@Override
	public MovePattern copy(ChessPiece owner) {
		return new KnightMovePattern(owner);
	}
	
}
