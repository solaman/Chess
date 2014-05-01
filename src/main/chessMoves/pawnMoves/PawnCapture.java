package main.chessMoves.pawnMoves;

import main.chessBoards.HexagonalBoard;
import main.chessMoves.PawnMove;
import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;
import main.ChessSpace;

/**
 * defines behavior for the Pawn Capture move
 * @author Solaman
 *
 */
public class PawnCapture extends PawnMove {
	
	/**
	 * build Pawn Capture
	 * @param owner
	 */
	public PawnCapture(ChessPiece owner) {
		super(owner);
	}
	
	@Override
	public void buildMoveData(ChessBoard board) {
		buildPathDataCapture(board, 1);
	}
	
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildPathDataCapture(board, 1);
	}
	
	protected void buildPathDataCapture(ChessBoard board, int yDisp){
		int xCoord= owner.getXCoord();
		int yCoord= owner.getYCoord();
		yCoord+= yDelta*yDisp;
		
		for(int xDelta=-1; xDelta<2; xDelta+=2){
			ChessSpace toMove= board.getChessSpace(xCoord+xDelta, yCoord);
			if( toMove == null) continue;
			
			addSpaceDependency(toMove);
			if(toMove.getOccupant() != null && toMove.getOccupant().getPlayer() != owner.getPlayer()){
				addCommandSequence(toMove, toMove, null, owner.getPosition(), toMove );
			}
		}
	}

	@Override
	public ChessMove copy(ChessPiece owner) {
		return new PawnCapture( owner);
	}

}
