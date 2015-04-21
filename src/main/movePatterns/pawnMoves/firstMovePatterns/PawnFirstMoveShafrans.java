package main.movePatterns.pawnMoves.firstMovePatterns;

import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.movePatterns.MovePattern;
import main.movePatterns.pawnMovePatterns.PawnFirstMove;
import main.pieces.ChessPiece;

public class PawnFirstMoveShafrans extends PawnFirstMove {

	public PawnFirstMoveShafrans(ChessPiece owner) {
		super(owner);
	}
	
	@Override
	public void buildMoveData(ChessBoard board){}
	
	@Override
	public void buildMoveData(HexagonalBoard board){
		int destination= (board.getYLength()-1)/2;
		int yCoord = owner.getYCoord()+yDelta*4;
		
		int steps=2;
		if(yDelta == 1)
			while( yCoord <= destination){
				buildPathData(board, steps, 2 );
				yCoord+= yDelta*2;
				steps++;
			}
		else
			while( yCoord>= destination){
				buildPathData(board, steps, 2 );
				yCoord+= yDelta*2;
				steps++;
			}
	}
	
	@Override
	public MovePattern copy(ChessPiece owner){
		return new PawnFirstMoveShafrans(owner);
	}

}
