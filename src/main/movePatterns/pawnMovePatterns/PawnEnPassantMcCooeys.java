package main.movePatterns.pawnMovePatterns;

import main.boards.HexagonalBoard;
import main.movePatterns.MovePattern;
import main.pieces.ChessPiece;

public class PawnEnPassantMcCooeys extends PawnEnPassant {

	public PawnEnPassantMcCooeys(ChessPiece owner) {
		super(owner);
	}
	
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildMoveData(board, -1, 3);
		buildMoveData(board, 1, 3);
	}
	
	@Override
	public MovePattern copy(ChessPiece owner){
		return new PawnEnPassantMcCooeys(owner);
	}

}
