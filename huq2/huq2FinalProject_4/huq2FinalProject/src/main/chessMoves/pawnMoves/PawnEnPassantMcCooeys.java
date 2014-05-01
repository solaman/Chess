package main.chessMoves.pawnMoves;

import main.ChessMove;
import main.ChessPiece;
import main.chessBoards.HexagonalBoard;

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
	public ChessMove copy(ChessPiece owner){
		return new PawnEnPassantMcCooeys(owner);
	}

}
