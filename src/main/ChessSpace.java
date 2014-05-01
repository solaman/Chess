package main;

import java.util.ArrayList;
import java.util.List;



/**
 *  Used to hold and relay information about a ChessSpace. including it's occupant and
 * Moves whose behavior depends on the state of the ChessSpace.
 * @author Solaman
 *
 */
public final class ChessSpace {
	
	/**
	 * occupant of space
	 */
	private ChessPiece occupant;
	
	/**
	 * list of ChessMove's that depend on the state of the space for their behavior
	 */
	private List<ChessMove> dependentMoves;
	
	/**
	 *x Coordinate of space 
	 */
	int xCoord;
	
	/**
	 * y coordinate of space
	 */
	int yCoord;

	/**
	 * constructor
	 * @param xCoord space's x coordinate
	 * @param yCoord space's y coordinate
	 */
	public ChessSpace(int xCoord, int yCoord){
		dependentMoves= new ArrayList<ChessMove>();
		occupant= null;
		this.xCoord= xCoord;
		this.yCoord=yCoord;
	}
	
	public int getXCoord() {
		return xCoord;
	}
	
	public int getYCoord() {
		return yCoord;
	}

	/**
	 * @return occupant of space
	 */
	public ChessPiece getOccupant(){	
		return occupant;	
	}
	
	/**
	 * @param occupy -piece to occupy the space
	 */
	public void setOccupant(ChessPiece occupy) {
		occupant=occupy;
	}
	
	/**
	 * @param move -move to remove from dependentMoves
	 */
	public void removeDependentMove(ChessMove move){
		dependentMoves.remove(move);
	}
	
	/**
	 * @param move -move to add to dependentMoves
	 */
	public void addDependentMove(ChessMove move){
		dependentMoves.add(move);
	}

	/**
	 * @return -dependentMoves
	 */
	public List<ChessMove> getDependentMoves() {
		return dependentMoves;
	}
}
