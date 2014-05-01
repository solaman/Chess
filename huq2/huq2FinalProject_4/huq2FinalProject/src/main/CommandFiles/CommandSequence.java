package main.CommandFiles;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;
import main.ChessSpace;

/**
 * @author Solaman
 * holds information used to perform and undo a "move" that a ChessMove (should have) created.
 * Note: the "type" of a move is analogous to a ChessMove (Rook's basic movement), and a way to physically perform that move is
 * analogous to a CommandSequence (rook to E5).
 * Note: the CommandSequence description is temporally dependent, i.e. does not give an accurate description of the move it is used
 * out of sequence
 */
public class CommandSequence {
	
	/**
	 * holds the LiftPlaceCommands that describe the lift commands of the move
	 */
	List<LiftPlaceCommand> liftCommandSequence;
	
	/**
	 * holds the LiftPlaceCommands that describe the place commands of the move
	 */
	List<LiftPlaceCommand> placeCommandSequence;
	
	/**
	 * denotes the "target" of the CommandSequence
	 * helpful for the GUI to describe the move minimally
	 */
	ChessSpace targetSpace;
	
	/**
	 * Piece that performs the CommandSequence
	 */
	ChessPiece performingPiece;

	/**
	 * move that allows the CommandSequence, note: the specific instance of ChessMove
	 * is NOT relevant to the behavior of CommandSequence nor any class that uses CommandSequence
	 */
	ChessMove movePerformed;
	
	private void init(ChessPiece performingPiece, ChessMove performingMove, ChessSpace target, List<ChessSpace> liftPlaceSequence) {
		targetSpace= target;
		this.performingPiece= performingPiece;
		this.movePerformed= performingMove;
				
		init(liftPlaceSequence);
	}
	
	private void init(List<ChessSpace> liftPlaceSequence){
		liftCommandSequence = new ArrayList<LiftPlaceCommand>();
		placeCommandSequence = new ArrayList<LiftPlaceCommand>();
		for(int liftIndex =0; liftIndex < liftPlaceSequence.size(); liftIndex +=2){
			
			ChessSpace liftSpace = liftPlaceSequence.get(liftIndex);
			liftCommandSequence.add( new LiftPlaceCommand(
					liftSpace.getOccupant(), liftSpace));
			
			ChessSpace placeSpace = liftPlaceSequence.get(liftIndex+1);
			if(placeSpace != null)
				placeCommandSequence.add( new LiftPlaceCommand(
						liftSpace.getOccupant(), placeSpace));
			else
				placeCommandSequence.add( new LiftPlaceCommand(
						null, null));
		}
	}
	
	/**
	 * @param board -board to interpret JSONObject from
	 * @param commandObject -describes how to build the CommandSequence using the board
	 * @throws JSONException -error occurred while interpreting JSON
	 * @throws ClassNotFoundException -JSON described a Move Class erroneously
	 */
	public CommandSequence(ChessBoard board, JSONObject commandObject) throws JSONException, ClassNotFoundException {
		JSONArray coords= commandObject.getJSONArray("performing piece");
		performingPiece= board.getOccupant( coords.getInt(0), coords.getInt(1));
		Class movePerformedC= Class.forName( commandObject.getString("move performed") );
		for(ChessMove move : performingPiece.getMoves())
			if( move.getClass() == movePerformedC ){
				movePerformed= move;
				continue;
			}
		
		coords= commandObject.getJSONArray("target space");
		targetSpace= board.getChessSpace( coords.getInt(0), coords.getInt(1));
		List<ChessSpace> liftPlaceSequence= spacesFromJSON( board, commandObject);
		init(liftPlaceSequence);
		
	}
	

	private List<ChessSpace> spacesFromJSON(ChessBoard board, JSONObject listObject) throws JSONException{
		List<ChessSpace> sequence= new ArrayList<ChessSpace>();
		JSONArray coords;
		JSONArray liftArray= listObject.getJSONArray("lift sequence");
		JSONArray placeArray= listObject.getJSONArray("place sequence");
		for(int lpIndex=0; lpIndex < placeArray.length() ; lpIndex++){
			coords= liftArray.getJSONArray(lpIndex);
			sequence.add( board.getChessSpace(coords.getInt(0), coords.getInt(1)));
			coords= placeArray.getJSONArray(lpIndex);
			sequence.add( board.getChessSpace(coords.getInt(0), coords.getInt(1)));
		}
		return sequence;
	}
	
	
	/**
	 * constructor. By convention, the liftPlaceSequence should be ordered by space to lift AND THEN 
	 * place the piece. If a piece is not to be placed back on the board, a lift space should be followed by null
	 * @param performingPiece - piece that would perform the Command
	 * @param target -target space of the CommandSequence
	 * @param liftPlaceSequence -liftPlace spaces used to describe the CommandSequence
	 */
	public CommandSequence(ChessPiece performingPiece, ChessMove performingMove, ChessSpace target, ChessSpace...liftPlaceSequence) {
		List<ChessSpace> lpSequence= new ArrayList<ChessSpace>();
		for(int i=0; i< liftPlaceSequence.length; i++)
			lpSequence.add(liftPlaceSequence[i]);
		init(performingPiece, performingMove, target, lpSequence);
	}
	
	/**
	 * constructor with List<ChessSpace> instead of ChessSpace...
	 */
	public CommandSequence(ChessPiece performingPiece, ChessMove performingMove, ChessSpace target, List<ChessSpace> liftPlaceSequence) {
		init(performingPiece, performingMove, target, liftPlaceSequence);
	}

	/**
	 * @return target of CommandSequence
	 */
	public ChessSpace getTargetSpace(){
		return targetSpace;
	}
	
	public ChessPiece getPerformingPiece(){
		return performingPiece;
	}
	
	public ChessMove getMovePerformed(){
		return movePerformed;
	}
	
	/**
	 * Perform the LiftPlaceCommands on the provided board.
	 * @param board -board the CommandSequence is performed on
	 * @param movesToUpdate -list of moves that must be updated after performance
	 */
	public void doCommands(ChessBoard board){
		for(LiftPlaceCommand command : liftCommandSequence)
			command.performLift(board);
		for(LiftPlaceCommand command : placeCommandSequence)
			command.performPlace(board);
		performingPiece.incrementMovesMade();
	}
	
	/**
	 * undoes the command. Functionality is not protected if the command was never performed
	 * @param board -board the CommandSeqeunce is undone on
	 * @param turnToGoBack -turnToGoBack to 
	 * @param movesToUpdate -list of moves to update after the move is undone
	 */
	public void undoCommands(ChessBoard board){
		for(LiftPlaceCommand command : placeCommandSequence)
			command.undoPlace( board);
		for(LiftPlaceCommand command : liftCommandSequence)
			command.undoLift( board);
		performingPiece.decrementMovesMade();
	}

	/**
	 * convert the Command into a JSONObject.
	 * NOTE: this will be erroneous if performed after doCommands is called
	 * @param board -board to convert the Command on
	 * @return JSONObject of the Command
	 * @throws JSONException -error occured while converted the Command into JSONObject
	 */
	public JSONObject asJSON(ChessBoard board) throws JSONException{
		JSONObject commandObject= new JSONObject();
		JSONArray coords= new JSONArray();
		coords.put(performingPiece.getXCoord());
		coords.put(performingPiece.getYCoord());
		commandObject.put("performing piece", coords);
		
		commandObject.put("move performed", movePerformed.getClass().getName());

		coords= new JSONArray();
		coords.put(targetSpace.getXCoord());
		coords.put(targetSpace.getYCoord());
		commandObject.put("target space", coords);
		
		List<ChessSpace> liftPlaceSequence= new ArrayList<ChessSpace>();
		commandObject.put("lift sequence", asJSON(liftCommandSequence) );
		commandObject.put("place sequence", asJSON(placeCommandSequence));
		return commandObject;
	}
	
	private JSONArray asJSON(List<LiftPlaceCommand> commandSequence) throws JSONException{
		JSONArray sequence= new JSONArray();
		for(LiftPlaceCommand command: commandSequence){
			sequence.put( command.asJSON());
		}
		return sequence;
	}

}
