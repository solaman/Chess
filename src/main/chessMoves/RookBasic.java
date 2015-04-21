package main.chessMoves;

import main.chessBoards.HexagonalBoard;
import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;

/**
 * defines the behavior of the basic rook move
 * @author Solaman
 */
public class RookBasic extends ChessMove {

	/**
	 * @param owner -owner of the piece
	 */
	public RookBasic(ChessPiece owner) {
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
	public ChessMove copy(ChessPiece owner) {
		return new RookBasic(owner);
	}
	
}
