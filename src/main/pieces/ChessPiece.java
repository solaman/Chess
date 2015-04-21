package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.chessGUI.ChessPieceRepresentations;
import main.moveHistory.MoveSequence;
import main.movePatterns.MovePattern;

/**
 * Helps communication between the ChessBoard the piece is on as well as the ChessMoves it contains.
 * @author Solaman
 *
 */
public class ChessPiece {
	
	/**
	 * list of moves the piece can perform
	 */
	protected List< MovePattern> moves;
	
	/**
	 * owner of the piece
	 */
	protected int player;
	
	/**
	 * position of the piece by ChessSpace
	 */
	ChessSpace position;
	
	/**
	 * number of moves the piece has made
	 */
	protected int movesMade;

	/**
	 * @param player- player the piece belongs to
	 */
	public ChessPiece( int player){
		movesMade=0;
		moves= new ArrayList<MovePattern>();
		this.player= player;
	}
	
	public ChessPiece copy(){
		return new ChessPiece(this);
	}
	
	public ChessPiece( ChessPiece piece){
		this.player= piece.player;
		moves= new ArrayList<MovePattern>();
		for( MovePattern move : piece.moves)
			moves.add( move.copy(piece));
	}

	/**
	 * @return -player this piece belongs to
	 */
	public int getPlayer(){
		return player;
	}
	
	public void incrementMovesMade(){
		movesMade++;
	}
	
	public void decrementMovesMade(){
		movesMade--;
	}
	
	/**
	 * @return if the piece has made 0 moves, it has not moved
	 */
	public boolean hasMoved(){
		return ( movesMade != 0);
	}
	
	/**
	 * @return -1 if position is null
	 */
	public int getXCoord(){
		if(position == null)
			return -1;
		return position.getXCoord();
	}
	
	/**
	 * @return -1 if position is null
	 */
	public int getYCoord(){
		if(position == null)
			return -1;
		return position.getYCoord();
	}
	
	/**
	 * @return ChessSpace position
	 */
	public ChessSpace getPosition(){
		return position;
	}
	
	/**
	 * @param ps - position to set for ChessPiece
	 */
	public void setPosition(ChessSpace ps){
		position=ps;
	}
	
	
	/**
	 * @param movesToAdd -moves to add to ChessPiece
	 */
	public void addMoves(MovePattern... movesToAdd){
		for(MovePattern move : movesToAdd)
			moves.add(move);
	}

	/**
	 * @param board -board to get moves on
	 * @return all liftPlaceSequences from the moves the ChessPiece holds
	 */
	public List< MoveSequence> getAllCommandSequences(ChessBoard board){
		List< MoveSequence> commandSequences = new ArrayList< MoveSequence >();
		for(MovePattern move : moves){
			if(!move.getCommandSequences(board).isEmpty())
				commandSequences.addAll( move.getCommandSequences(board));
		}
			
		return commandSequences;
		
	}
	
	/**
	 * @return list of moves attached to the piece
	 */
	public List<MovePattern> getMoves(){
		return moves;
	}

	/**
	 * @param reps- ChessPieceRepresentations object that defines how the piece should be represented
	 * @return -null, as the basic ChessPiece class has no representation
	 * @throws IOException error occurred while getting image
	 */
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException {
		return null;
	}
	
}
