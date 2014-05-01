package main.chessMoves.pawnMoves;


import main.chessBoards.HexagonalBoard;
import main.chessMoves.PawnMove;
import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;
import main.ChessSpace;


/**
 * @author Solaman -defines the behavior of the basic pawn maneuver
 * 
 */
public final class PawnBasic extends PawnMove {
	
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
	
	public void buildMoveData(HexagonalBoard board){
		buildPathDataCoord( (ChessBoard) board, owner.getXCoord(), owner.getYCoord()+yDelta*2);
	}

	@Override
	public ChessMove copy(ChessPiece owner) {
		return new PawnBasic(owner);
	}

	
	
}
