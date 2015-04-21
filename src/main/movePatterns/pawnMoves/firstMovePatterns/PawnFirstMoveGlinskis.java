package main.movePatterns.pawnMoves.firstMovePatterns;

import java.util.ArrayList;
import java.util.List;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.moveHistory.MoveHistory;
import main.movePatterns.MovePattern;
import main.movePatterns.pawnMovePatterns.PawnCapture;
import main.movePatterns.pawnMovePatterns.PawnFirstMove;
import main.pieces.ChessPiece;
import main.pieces.Pawn;

/**
 * moves the piece forward two spaces.
 * First Move is allowed during a pawn's first movement, or when the pawn
 * performs a capture such that it lands on a space where a pawn of the same team was initially set up.
 * @author Solaman
 *
 */
public class PawnFirstMoveGlinskis extends PawnFirstMove {

	/**
	 * list of spaces pawns were initially set up at
	 */
	private List<ChessSpace> pawnsInitialSetUp;
	
	public PawnFirstMoveGlinskis(ChessPiece owner) {
		super(owner);
		pawnsInitialSetUp = new ArrayList<ChessSpace>();
	}
	
	@Override
	public void initializeMoveData(HexagonalBoard board){
		for( ChessPiece piece : board.pieces.get(owner.getPlayer()))
			if(piece instanceof Pawn)
				pawnsInitialSetUp.add(piece.getPosition());
		buildMoveData(board);
	}
	
	
	@Override
	protected boolean canPerform(ChessBoard board){
		
		MoveHistory history = board.getCommandHistory();
		int numOfMoves= history.queryNumOfMoves(-2, owner, null);
		if(numOfMoves>0 && numOfMoves  > history.queryNumOfMoves(-2, owner, PawnCapture.class))
			return false;
		if( pawnsInitialSetUp.indexOf( owner.getPosition()) == -1)
			return false;
		return true;
	}
	
	/*
	 * Glinski's first move is not defined for rectangular boards
	 */
	@Override
	public void buildMoveData(ChessBoard board){}
	
	@Override
	public void buildMoveData(HexagonalBoard board){
		super.buildPathData(board, 2, 2);
	}
	
	@Override
	public MovePattern copy(ChessPiece owner){
		return new PawnFirstMoveGlinskis( owner);
	}

}
