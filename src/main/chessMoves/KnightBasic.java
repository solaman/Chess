package main.chessMoves;


import main.chessBoards.HexagonalBoard;
import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;

/**
 * Defines the behavior of the Basic Knight maneuver
 * @author Solaman
 */
public final class KnightBasic extends ChessMove {
	
	/**
	 * @param owner -owner of move
	 */
	public KnightBasic(ChessPiece owner) {
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
	public ChessMove copy(ChessPiece owner) {
		return new KnightBasic(owner);
	}
	
}
