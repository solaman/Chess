package main.movePatterns.pawnMovePatterns;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.movePatterns.MovePattern;
import main.movePatterns.PawnMovePattern;
import main.pieces.ChessPiece;

/**
 * defines the behavior of the Pawn First Move. 
 * @author Solaman
 *
 */
public class PawnFirstMove extends PawnMovePattern {
	
	public PawnFirstMove(ChessPiece owner) {
		super(owner);
	}
	
	@Override
	public boolean canThreaten(ChessPiece tothreaten){
		return false;
	}

	/*
	 * could not use the private buildPathDataVector method
	 * because it assumes that the move could capture enemy pieces
	 * @see main.ChessMove#buildPathData(main.ChessBoard)
	 */
	@Override
	public void buildMoveData(ChessBoard board) {
		buildPathData(board, 2, 1);
	}
	
	/**
	 * behavior differs drastically depending on game played on the hexagonal board,
	 * because convention dictates that moves are separate from the game,
	 * variants should be implemented in a subclass
	 */
	@Override
	public void buildMoveData(HexagonalBoard board){}
	
	protected boolean canPerform(ChessBoard board){
		return !owner.hasMoved();
	}
	
	protected void buildPathData(ChessBoard board, int steps, int stepSize){
		if( !canPerform(board))
			return;
		int xCoord=owner.getXCoord();
		int yCoord= owner.getYCoord();
		ChessSpace toMove= null;
		for(int i=0; i< steps; i++){
			yCoord+=yDelta*stepSize;
			toMove= board.getChessSpace(xCoord, yCoord);
			if(toMove == null)
				return;
			addSpaceDependency(toMove);
			if(toMove.getOccupant() != null)
				return;
		}
		addCommandSequence(toMove, owner.getPosition(), toMove);
	}

	@Override
	public MovePattern copy(ChessPiece owner) {
		return new PawnFirstMove(owner);
	}
	

}
