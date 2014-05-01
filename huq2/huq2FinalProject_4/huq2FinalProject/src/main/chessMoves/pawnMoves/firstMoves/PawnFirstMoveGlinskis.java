package main.chessMoves.pawnMoves.firstMoves;

import java.util.ArrayList;
import java.util.List;

import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;
import main.ChessSpace;
import main.CommandFiles.CommandHistory;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.pawnMoves.PawnCapture;
import main.chessMoves.pawnMoves.PawnFirstMove;
import main.chessPieces.Pawn;

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
		
		CommandHistory history = board.getCommandHistory();
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
	public ChessMove copy(ChessPiece owner){
		return new PawnFirstMoveGlinskis( owner);
	}

}
