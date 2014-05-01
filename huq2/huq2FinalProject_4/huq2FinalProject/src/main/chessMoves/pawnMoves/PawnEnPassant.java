package main.chessMoves.pawnMoves;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;
import main.ChessSpace;
import main.CommandFiles.CommandHistory;
import main.CommandFiles.CommandSequence;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.PawnMove;
import main.chessPieces.Pawn;

/**
 * defines behavior for the Pawn En Passant move. 
 * @author Solaman
 *
 */
public class PawnEnPassant extends PawnMove {

	Stack<Integer> storedTurnsActive;
	
	public PawnEnPassant(ChessPiece owner) {
		super( owner);
		storedTurnsActive= new Stack<Integer>();
	}
	
	/**
	 * this move can only threaten a pawn that has yet to move and is of the opponent's
	 * pieces
	 */
	@Override
	public boolean canThreaten(ChessPiece piece){
		return ( piece instanceof Pawn && !piece.hasMoved() && super.canThreaten(piece));
	}
	
	@Override
	public void clearMoveData(ChessBoard board){
		super.clearMoveData(board);
		while(!storedTurnsActive.isEmpty() && storedTurnsActive.peek().intValue() > board.getTurnNumber()+1){
			storedTurnsActive.pop();
		}
	}
	
	@Override
	public List<CommandSequence> getCommandSequences(ChessBoard board){
		if( !storedTurnsActive.isEmpty() && storedTurnsActive.peek().intValue() == board.getTurnNumber()+1)
			return commandSequences;
		return new ArrayList<CommandSequence>();
			
	}
	
	
	@Override
	public void buildMoveData(ChessBoard board) {
		buildMoveData(board, 0, 1);
	}

	@Override
	public void buildMoveData(HexagonalBoard board) {
		buildMoveData((ChessBoard) board, -1, 1);
	}
	
	protected void buildMoveData(ChessBoard board, int enemyYDisp, int captYDis){
		CommandHistory history= board.getCommandHistory();
		int xCoord= owner.getXCoord();
		int yCoord= owner.getYCoord();
		for(int xDelta= -1; xDelta<2; xDelta+=2){
			ChessSpace spaceToCheck= board.getChessSpace(xCoord+xDelta, yCoord+enemyYDisp*yDelta);
			if(spaceToCheck == null)
				continue;
			addSpaceDependency( spaceToCheck);
			ChessPiece piece= spaceToCheck.getOccupant();
			if(piece != null && piece instanceof Pawn && piece.getPlayer() != owner.getPlayer()){
				if( history.queryNumOfMoves(1, piece, PawnFirstMove.class) ==1){
					addCommandSequence(spaceToCheck, spaceToCheck, null, owner.getPosition(), board.getChessSpace( xCoord+xDelta, yCoord+yDelta*captYDis) );
					storedTurnsActive.push( new Integer( board.getTurnNumber()+1));
				}
			}
		}
	}

	@Override
	public ChessMove copy(ChessPiece owner) {
		return new PawnEnPassant(owner);
	}

}
