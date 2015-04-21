package main.movePatterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.moveHistory.MoveSequence;
import main.pieces.ChessPiece;


/**
 * Used to define the behavior of a "move" in chess (pawn's first move, pawn's basic move).
 * It is responsible for knowing when and how it is performed.
 * note: because it is possible to "undo" moves, implementation of moves
 * must ensure that they behave as desired even if part of it's logic is undone.
 * @author Solaman
 **/
public abstract class MovePattern {	
	
	/**
	 * list of CommandSequences used to define the "movements" the ChessMove currently makes available
	 */
	protected List< MoveSequence> commandSequences;
	
	/**
	 * ChessPiece that "owns" the instance of the ChessMove
	 */
	protected ChessPiece owner;
	
	/**
	 * list of ChessSpaces that should be used to help define the ChessMove's behavior.
	 */
	protected Stack<ChessSpace> dependentSpaces;
	
	/**
	 * builds commandSequences and dependentSpaces,
	 * if owner is not null, adds the move to the owner's list of moves
	 * @param owner of move
	 */
	public MovePattern(ChessPiece owner){
		this.owner= owner;
		commandSequences = new ArrayList< MoveSequence>();
		dependentSpaces= new Stack<ChessSpace>();
		if(owner != null)
			owner.addMoves(this);
	}
	
	public abstract MovePattern copy(ChessPiece owner);

	/** 
	 * @param board -board the return is performed on, more important for
	 * sub classes that need additional information to ensure that they in fact
	 * make movements available. i.e. KingCastling only returns if the king is not threatened.
	 * @return commandSequence used to describe the chessMove's available moves
	 */
	public List<MoveSequence> getCommandSequences(ChessBoard board){
		return commandSequences;
	}	

	/**
	 *Clears any reference to the move stored in spaces found in the
	 *dependentSpaces and clears all data in CommandSequences
	 *left protected in case additional book keeping is done in
	 *subclasses
	 */
	public void clearMoveData(ChessBoard board){
		while(!dependentSpaces.empty())
			dependentSpaces.pop().removeDependentMove(this);
		commandSequences.clear();
	}
	
	/**
	 * build necessary data for the move to define its behavior.
	 * does NOT protect against the owner's position being null
	 * @param board -board to build data from
	 */
	public abstract void buildMoveData(ChessBoard board);
	
	/**
	 * build necessary data for the move to define its behavior.
	 * does NOT protect against the owner's position being null
	 * @param board -board to build data from
	 */
	public abstract void buildMoveData(HexagonalBoard board);
	
	/**
	 * returns true if piece can threaten another,
	 * base implementation returns true if owner is of a different team than
	 * the piece to threaten
	 * @param occupant -piece we wish to threaten
	 * @return -can it be threatened?
	 */
	public boolean canThreaten(ChessPiece occupant) {
		return (occupant.getPlayer() != owner.getPlayer());
	}
	
	/**
	 * uses given *vector* coordinates of spaces to build Path data,
	 * asserts that the given list is of the format x1 y1 x2 y2...xn yn.
	 * e.g. if owner is at 5,3. then buildMoveDataSpaces(board, 2, 2) will build on 7, 5
	 * @param board -board that the move is performed on
	 * @param coords -coordinates of spaces to build Path data from
	 */
	protected final void buildMoveDataSpaces(ChessBoard board, int... coords) {
		assert(coords.length % 2 == 0);
		for(int coordI=0; coordI< coords.length; coordI+=2)
			buildMoveDataSpace( owner.getPosition(), board.getChessSpace(owner.getXCoord()+coords[coordI], owner.getYCoord()+coords[coordI+1]));
	}
		
	/**
	 * steps through ChessSpaces along the designated vector to build path data,
	 * stops when a space in the vector is obstructed
	 * @param board -board that the move is performed on
	 * @param xDelta - change in x coordinate per iteration
	 * @param yDelta - change in y coordinate per iteration
	 * @param iterations - number of iterations, set to -2 if indefinite
	 */
	protected final void buildMoveDataVector(ChessBoard board, int xDelta, int yDelta, 
			int iterations){

		int x= owner.getXCoord();
		int y= owner.getYCoord();
		ChessSpace toMove;
		
		while(iterations == -2 || 0<iterations--){
			x+=xDelta;
			y+=yDelta;
			toMove=board.getChessSpace( x, y);
			
			if(!buildMoveDataSpace(owner.getPosition(), toMove) )
				return;
		}
	}
	
	/**
	 * adds SpaceDependcy to ChessSpace and DependentSpace to the ChessMove if not null, 
	 * if the space is unoccupied: adds a CommandSequence to CommandSequences to lift and place the performing piece
	 * if the space is occupied: a lift and place the performing piece as well as lift the piece at the target space
	 * if it can be threatened (captured)
	 * @param toBuild -space to build on
	 * @return -false if the space is occupied or the space is null
	 */
	protected final boolean buildMoveDataSpace(ChessSpace origin, ChessSpace toBuild){
		if(toBuild == null) return false;
		addSpaceDependency(toBuild);
			
		if(toBuild.getOccupant() == null){
			addCommandSequence( toBuild, origin, toBuild);
			return true;
		}
		else if( toBuild.getOccupant().getPlayer() != owner.getPlayer()){
			addCommandSequence( toBuild, toBuild, null, origin, toBuild);
			return false;
		}
		return false;
	}
	
	/**
	 * adds the Space to dependentSpaces and the move the space's dependentMoves
	 * @param toAdd -space to perform on
	 */
	protected final void addSpaceDependency(ChessSpace... toAdd){
		for(ChessSpace dep: toAdd){
			dependentSpaces.add(dep);
			dep.addDependentMove(this);
		}
	}
	
	/**
	 * out of the given ChessSpaces, builds and then adds a commandSequence to the ChessMove's CommandSequences
	 * @param target -"target" of the CommandSequence, by convention should be unique from all other moves a piece
	 * can perform
	 * @param liftPlaceSequence - sequence of spaces to define CommandSequence, by convention should be: lift AND THEN place ChessSpaces
	 */
	protected final void addCommandSequence(ChessSpace target, ChessSpace...liftPlaceSequence){
		commandSequences.add( new MoveSequence(owner, this, target, liftPlaceSequence));
	}

	/**
	 * initialize data for move. Should only be called at the beginning of a game after pieces are set in position.
	 * @param chessBoard- board to initialize on
	 */
	public void initializeMoveData(ChessBoard chessBoard) {
		buildMoveData(chessBoard);
	}
	
	/**
	 * initialize data for move. Should only be called at the beginning of a game after pieces are set in position.
	 * @param board -board to initialize on
	 */
	public void initializeMoveData(HexagonalBoard board){
		buildMoveData(board);
	}
}
