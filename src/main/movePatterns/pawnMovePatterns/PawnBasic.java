package main.movePatterns.pawnMovePatterns;


import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.movePatterns.MovePattern;
import main.movePatterns.PawnMovePattern;
import main.pieces.ChessPiece;


/**
 * @author Solaman -defines the behavior of the basic pawn maneuver
 * 
 */
public final class PawnBasic extends PawnMovePattern {
	
	public PawnBasic(ChessPiece owner) {
		super(owner);
	}
		
	/**
	 * @return false -pawn's basic move threatens no-one
	 */
	@Override
	public boolean canThreaten(ChessPiece toThreaten){
		return false;
	}
	
	@Override
	public void buildMoveData(ChessBoard board) {
		int moveToX= owner.getXCoord();
		int moveToY= owner.getYCoord()+yDelta;
		buildPathDataCoord(board, moveToX, moveToY);
	}	
	
	protected void buildPathDataCoord(ChessBoard board, int xCoord, int yCoord){
		ChessSpace toMove= board.getChessSpace(xCoord, yCoord);
		if( toMove == null) return;
		
		addSpaceDependency(toMove);
		if(toMove.getOccupant() == null)
			addCommandSequence(toMove, owner.getPosition(), toMove);
	}
	
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildPathDataCoord( board, owner.getXCoord(), owner.getYCoord()+yDelta*2);
	}

	@Override
	public MovePattern copy(ChessPiece owner) {
		return new PawnBasic(owner);
	}

	
	
}
