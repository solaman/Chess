package main.boards;

import java.util.ArrayList;
import java.util.List;

import org.json.*;

import main.chessGUI.boardPanels.BoardPanel;
import main.moveHistory.MoveHistory;
import main.moveHistory.MoveSequence;
import main.movePatterns.MovePattern;
import main.pieces.ChessPiece;
import main.pieces.King;


/**
 * responsible for communication of components of the "board" as well as defining procedures commonly found in a game of chess.
 * @author Solaman
 */
public class ChessBoard {

	/**
	 * 2d array of ChessSpaces defining board's layout
	 */
	protected ChessSpace[][] board;
	
	/**
	 * ChessPieces involved in play, each List is the pieces of an individual player
	 */
	public List<List <ChessPiece>> pieces;
	
	/**
	 * history of CommandSequences performed on the board 
	 */
	protected MoveHistory commandHistory;
	
	/**
	 * used to locally store moves that need to clear their data off of the board.
	 */
	protected List<MovePattern> movesToClear;
	
	/**
	 * used to locally store moves that need to build their data onto the board.
	 */
	protected List<MovePattern> movesToBuild;
	
	/**
	 * default ChessBoard constructor, builds in shape of rectangle,
	 * @param xLength -length of board
	 * @param yLength -height of board
	 * @throws Exception -thrown if length or height is negative
	 */
	public ChessBoard( int xLength, int yLength ) throws Exception{
		if( xLength <= 0 || yLength <= 0 )
			throw new Exception("invalid Size");
		
		commandHistory= new MoveHistory();
		pieces= new ArrayList< List<ChessPiece>>();
		board = new ChessSpace[ xLength ][ yLength ];
		movesToClear= new ArrayList<MovePattern>();
		movesToBuild= new ArrayList<MovePattern>();
		
		for(int x=0; x<  board.length; x++)
			for(int y=0; y< board[0].length; y++)
				this.board[x][y]= new ChessSpace(x, y);
	}
	
	public MoveHistory getCommandHistory(){
		return commandHistory;
	}
	
	public int getYLength(){
		return board[0].length;
	}
	
	public int getXLength(){
		return board.length;
	}
	
	/**
	 * get ChessSpace by coordinate
	 * @return null -coordinate is silly/out of bounds/not apart of "active board"
	 */
	public ChessSpace getChessSpace(int ...coords){
		if(coords[0] < 0 || coords[1]< 0 || coords[0]>= board.length || coords[1] >= board[0].length)
			return null;
		return board[ coords[0] ][ coords[1] ];
	}
	
	/**
	 * get occupant of ChessSpace by coordinate
	 * @param coords -coordinates of space
	 * @return null -no occupant exists OR space does not exist at given coordinate
	 */
	public ChessPiece getOccupant(int...coords){
		ChessSpace space= getChessSpace(coords);
		if(space == null)
			return null;
		return space.getOccupant();
	}

	public final int getTurnNumber() {
		return commandHistory.size();
	}
	
	/**
	 * @return which player's turn it is
	 */
	public int getPlayerTurn(){
		if(pieces.size() == 0)
			return 0;
		return (commandHistory.size()) % pieces.size();
	}
	
	/**
	 * Checks to see if the player has a legal move they can perform
	 * @param -player that we are checking
	 * @return -can they perform a legal move?
	 * @throws Exception -player does not have a king
	 */
	public final boolean hasLegalMove(int player) throws Exception{
		List<ChessPiece> playerPieces= pieces.get(player);
		for(ChessPiece piece : playerPieces)
			if( hasLegalMove(piece))
				return true;
		return false;
	}
	
	/**
	 * Checks to see if the piece has a legal move they can perform
	 * @param piece -piece to check
	 * @return -can the piece perform a legal move?
	 * @throws Exception -player of piece does not have a king
	 */
	private final boolean hasLegalMove(ChessPiece piece) throws Exception{
		List<MoveSequence> commandSequences= piece.getAllCommandSequences(this);
		for(MoveSequence commandSequence : commandSequences){
			if( isLegalMove(commandSequence)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param commandSequence -CommandSequence defining the move
	 * @return is the move legal?
	 * @throws Exception -player of piece performing move does not have a king
	 */
	protected boolean isLegalMove(MoveSequence commandSequence) throws Exception{
		boolean isLegal=true;
		
		commandHistory.doMove( commandSequence, this);
		updateAllMoves();
		if( isKingThreatened(commandSequence.getPerformingPiece().getPlayer() ) )
			isLegal=false;
		reverseMoves(1);
		return isLegal;
	}
	
	/**
	 * checks to see if the given space can threaten the given piece (piece does NOT have to be on the space)
	 * NOTE: this is Non-temporal, moves have no knowledge of what move WILL be performed 
	 * @param piece -piece we wish to check if it can be threatened
	 * @param coords -coordinate of space we wish to check if it can threaten
	 * @return -does the space threaten the piece?
	 */
	public boolean canThreaten(ChessPiece piece, ChessSpace spaceToCheck){
		List<MovePattern> moveRefs= spaceToCheck.getDependentMoves();
		for(MovePattern move : moveRefs)
			if( move.canThreaten(piece))
				return true;
		return false;
	}
	
	/**
	 * Checks to see if any move is threatening the king of the given player
	 * @param player -player to check if king is threatened for
	 * @return -is the king threatened?
	 * @throws Exception player does not have a king
	 */
	public final boolean isKingThreatened( int player) throws Exception {
		for(ChessPiece piece : pieces.get(player))
			if( piece.getClass() == King.class )
				return canThreaten(piece, piece.getPosition() );
		throw new Exception("player "+(player+1)+" has no king");
	}
	
	/**
	 * perfom move
	 * @param commandSequence -JSONObject used to create a CommandSequence from
	 * @return -was the move successfully performed?
	 * @throws ClassNotFoundException -JSONObject describes moves not found
	 * @throws JSONException -JSONObject is not of a form a CommandSequence can interpret
	 * @throws Exception -player of piece performing move does not have a king
	 */
	public boolean performMovePermanent(JSONObject commandSequence) throws ClassNotFoundException, JSONException, Exception{
		return performMovePermanent( new MoveSequence(this, commandSequence));
	}
	
	/**
	 * performs a move if it is legal, and updates all necessary moves.
	 * @param piece -piece performing the move
	 * @param commandSequence- CommandSequence defining the move
	 * @return -was the move successfully performed?
	 * @throws Exception -player of piece performing move does not have a king
	 */
	public boolean performMovePermanent( MoveSequence commandSequence) throws Exception{
		if(!isLegalMove(commandSequence))
			return false;
		commandHistory.doMove(commandSequence, this);
		updateAllMoves();
		return true;
	}
	
	/**
	 * reverses the effects of *performed* moves
	 * @param turnsToGoBack -number of turns/moves to reverse
	 */
	public void reverseMoves(int turnsToGoBack){
		commandHistory.undoMoves(turnsToGoBack, this);
		updateAllMoves();
	}
	
	/**
	 * place piece by coordinate
	 * @param piece -piece to place
	 * @param coords -coordinate to place at
	 */
	public void placePiece(ChessPiece piece, int...coords){
		placePiece( piece, getChessSpace(coords)  );
	}

	/**
	 * if spaceToPlace is not null, set occupant of spaceToPlace and position of piece to eachother
	 * and add all moves to movesToBuild/Clear that should be built/cleared
	 * @param piece -ChessPiece to place
	 * @param spaceToPlace -ChessSpace to place at
	 * @param movesToUpdate -list to add moves to update to
	 */
	public final void placePiece(ChessPiece piece, ChessSpace spaceToPlace) {
		if(spaceToPlace != null){
			spaceToPlace.setOccupant(piece);
			piece.setPosition(spaceToPlace);
			addToList(piece.getMoves(), movesToBuild);
			addToList(spaceToPlace.getDependentMoves(), movesToClear);
			addToList(spaceToPlace.getDependentMoves(), movesToBuild);
		}
	}

	/**
	 * set occupant and position of spaceToLift and its occupant to null,
	 * and add all moves to movesToBuild/Clear that should be built/cleared
	 * @param spaceToLift -space to lift from
	 * @param movesToUpdate -list to add all moves to update to
	 * @return lifted piece
	 */
	public final void liftPiece(ChessSpace spaceToLift){
		ChessPiece toReturn= spaceToLift.getOccupant();
		spaceToLift.setOccupant(null);
		toReturn.setPosition(null);
		
		addToList(toReturn.getMoves(), movesToClear);
		addToList(spaceToLift.getDependentMoves(), movesToClear);
		addToList(spaceToLift.getDependentMoves(), movesToBuild);
	}

	/**
	 * clears path data of all moves in movesToClear, and build path data of all moves in movesToBuild,
	 * then clears both of those lists
	 */
	private final void updateAllMoves() {
		for(MovePattern move : movesToClear)
			clearMoveData(move);
		
		for(MovePattern move: movesToBuild)
			buildMoveData(move);
		
		movesToClear.clear();
		movesToBuild.clear();
	}
	
	/**
	 * @param move to clear data from
	 */
	protected void clearMoveData(MovePattern move){
		move.clearMoveData(this);
	}
	
	/**
	 * @param move to build data from
	 */
	protected void buildMoveData(MovePattern move){
		move.buildMoveData(this);
	}
	
	/**
	 * initializes all data of all moves. This should be called only once after all pieces have been placed on the board and the game will begin
	 */
	public void initializeMoves(){
		for(MovePattern move : movesToBuild)
			move.initializeMoveData(this);
		movesToBuild.clear();
	}
	
	public BoardPanel getRepresentation(){
		return new BoardPanel( this);
	}
	
	public void removePiece(ChessPiece piece){
		pieces.get(piece.getPlayer()).remove(piece);
		liftPiece(piece.getPosition());
	}
	
	public void setPiece(ChessPiece piece, int... coords){
		placePiece(piece, coords);
		while(pieces.size() < piece.getPlayer()+1)
			pieces.add( new ArrayList<ChessPiece>());
		
		pieces.get( piece.getPlayer()).add(piece);
	}
	
	private void addToList(List<MovePattern> toAdd, List<MovePattern> moveList){
		for(MovePattern move: toAdd)
			if( moveList.indexOf(move) == -1)
				moveList.add(move);
	}
}
