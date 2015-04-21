package main.movePatterns.pawnMovePatterns;

import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.pieces.ChessPiece;

public class PawnCaptureMcCooeys extends PawnCapture {

	
	public PawnCaptureMcCooeys(ChessPiece owner) {
		super(owner);
	}
	
	@Override
	public void buildMoveData(ChessBoard board) {
	}
	
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildPathDataCapture(board, 3);
	}
	
	@Override
	public PawnCaptureMcCooeys copy(ChessPiece owner){
		return new PawnCaptureMcCooeys(owner);
	}

}
