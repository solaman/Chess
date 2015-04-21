package main.movePatterns;

import java.util.ArrayList;
import java.util.List;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.moveHistory.MoveSequence;
import main.pieces.ChessPiece;
import main.pieces.Rook;

/**
 * Defines the behavior of the Castling move
 * @author Solaman
 *
 */
public class CastlingMovePattern extends MovePattern{
	
	public CastlingMovePattern(ChessPiece owner) {
		super(owner);
	}
	
	/* 
	 * only return commandSequences if the king is not currently threatened
	 */
	@Override
	public List< MoveSequence> getCommandSequences(ChessBoard board){
		List<MovePattern> potentialThreats= owner.getPosition().getDependentMoves();
		for(MovePattern potThreat : potentialThreats)
			if( potThreat.canThreaten(owner))
				return new ArrayList< MoveSequence>();
		return commandSequences;
		
	}

	@Override
	public void buildMoveData(ChessBoard board) {
		if(owner.hasMoved() || board.canThreaten(owner, owner.getPosition()) )
			return;
		
		for( ChessPiece piece: board.pieces.get(owner.getPlayer()))
			if(piece instanceof Rook && !piece.hasMoved()){
				if(piece.getPosition()== null) continue;
				int xDelta= getDelta(owner.getXCoord(), piece.getXCoord());
				int yDelta= getDelta(owner.getYCoord(), piece.getYCoord());
				if(checkRookPath(board, piece, xDelta, yDelta) ){
					int kingY= owner.getYCoord();
					int kingX= owner.getXCoord();
					ChessSpace target=board.getChessSpace( kingX+xDelta*2, kingY+yDelta*2 );
					addCommandSequence(target, piece.getPosition(), board.getChessSpace(kingX+(xDelta), kingY+(yDelta)),
							owner.getPosition(), target  );
				}
			}
	}
	
	/* 
	 * Castling cannot threaten a piece
	 */
	@Override
	public boolean canThreaten(ChessPiece piece){
		return false;
	}
	
	private boolean checkRookPath(ChessBoard board, ChessPiece rook, int xDelta, int yDelta) {
		int yCoord= owner.getYCoord()+yDelta;
		int xCoord= owner.getXCoord()+xDelta;
		ChessSpace toCheck= board.getChessSpace(xCoord, yCoord);
		addSpaceDependency( toCheck);
		
		while( board.getOccupant(xCoord, yCoord) == null){
			yCoord+= yDelta;
			xCoord+= xDelta;
			toCheck= board.getChessSpace(xCoord, yCoord);
			addSpaceDependency( board.getChessSpace(xCoord, yCoord));
		}
		if(toCheck == rook.getPosition())
			return true;
		return false;
	}

	@Override
	public void buildMoveData(HexagonalBoard board) {
		if(owner.hasMoved() || board.canThreaten(owner, owner.getPosition()) )
			return;
		
		for( ChessPiece piece: board.pieces.get(owner.getPlayer()))
			if(piece instanceof Rook && !piece.hasMoved()){
				if(piece.getPosition()== null) continue;
				int xDelta= getDelta(owner.getXCoord(), piece.getXCoord());
				int yDelta= getDelta(owner.getYCoord(), piece.getYCoord());
				if(checkRookPath(board, piece, xDelta, yDelta) ){
					int kingY= owner.getYCoord();
					int kingX= owner.getXCoord();
					ChessSpace target=board.getChessSpace( kingX+xDelta*2, kingY+yDelta*2 );
					addCommandSequence(target, piece.getPosition(), board.getChessSpace(kingX+(xDelta), kingY+(yDelta)),
							owner.getPosition(), target  );
					target= board.getChessSpace(kingX+ xDelta*3, kingY+yDelta*3);
					addCommandSequence(target, piece.getPosition(), board.getChessSpace(kingX+(xDelta*2), kingY+yDelta*2),
							owner.getPosition(), target );
				}
			}
	}
	
	protected int getDelta(int from, int to){
		if( from == to)
			return 0;
		if( from < to)
			return 1;
		return -1;
	}

	@Override
	public MovePattern copy(ChessPiece owner) {
		return new CastlingMovePattern(owner);
	}
	
}
