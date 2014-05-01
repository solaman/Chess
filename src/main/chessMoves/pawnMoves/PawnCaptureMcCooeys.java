package main.chessMoves.pawnMoves;

import main.chessBoards.HexagonalBoard;
import main.ChessBoard;
import main.ChessPiece;

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
