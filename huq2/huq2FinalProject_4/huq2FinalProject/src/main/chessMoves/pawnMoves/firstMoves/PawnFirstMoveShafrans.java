package main.chessMoves.pawnMoves.firstMoves;

import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.pawnMoves.PawnFirstMove;

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
	public ChessMove copy(ChessPiece owner){
		return new PawnFirstMoveShafrans(owner);
	}

}
