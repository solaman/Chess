package main.moveHistory;

import org.json.*;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.pieces.ChessPiece;

/**
 * holds information to do or undo a lift or place on the board
 * @author Solaman
 *
 */
public class LiftPlaceCommand {
	/**
	 * piece to lift/place
	 */
	private ChessPiece piece;
	
	/**
	 * space to lift from/place on
	 */
	ChessSpace space;
	
	/**
	 * @param piece -piece involved with command
	 * @param space -space involved with command
	 * @param liftOrPlace -designates the type of command
	 */
	public LiftPlaceCommand(ChessPiece piece, ChessSpace space) {
		this.piece = piece;
		this.space = space;
	}

	/**
	 * undoes the performed command
	 * @param board -board to undo command on
	 * @param movesToUpdate -add to this the moves to be updated after the command is undone
	 */
	public void undoLift( ChessBoard board) {
		board.placePiece(piece, space);
	}
	
	public void undoPlace(ChessBoard board){
		if(space != null)
			board.liftPiece(space);
	}

	/**
	 * perform the command
	 * @param board -board to do the command on
	 * @param movesToUpdate -add to this the moves to be updated after the command is done
	 */
	public void performLift(ChessBoard board) {
		board.liftPiece(space);
	}
	
	public void performPlace(ChessBoard board){
		board.placePiece(piece, space);
	}
	
	/**
	 * used to help reconstruct the object
	 * note: as the object's other fields are entirely dependent on 
	 * @return
	 */
	public JSONArray asJSON(){
		JSONArray coords= new JSONArray();
		if(space != null){
			coords.put(space.getXCoord());
			coords.put(space.getYCoord());
		} else{
			coords.put(-1);
			coords.put(-1);
		}
		return coords;
	}

}
