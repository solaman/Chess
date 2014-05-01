package main.chessBoards;

import main.ChessBoard;
import main.ChessMove;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.BoardPanelFiles.HexagonalBoardPanel;

/** 
 * @author Solaman
 * used for games with y-axis symmetric, side-on-floor hexagonal boards.
 * refer to "Hexagonal Model Structures.pdf" in resources to understand model structure.
 */
public class HexagonalBoard extends ChessBoard {
	
	/**
	 * Hexagonal Board Constructor
	 * @param xcoord max x coordinate of board
	 * @param ycoord max y coordinate of board
	 * @throws Exception -thrown if parameters can't be used to build board
	 */
	public HexagonalBoard( int xcoord, int ycoord) throws Exception{
		super( xcoord, ycoord*2);
		if( xcoord %2 == 0 )
			throw new Exception("x must be odd");
		if(  (xcoord/2)>(ycoord) )
			throw new Exception("y must be >= floor( xcoord/2)");
		
		for(int x=0; x<= xcoord/2 ; x++){
			clearAlternating(xcoord/2, x, ycoord*2);
			clearOutside(xcoord/2, x, ycoord*2);
		}
	}
	
	/**
	 * sets alternating spaces to null that would be considered "inside" the board
	 * @param middle -index of the middle column of the board
	 * @param xShift -x index shift from middle column
	 * @param yLength -y length of "active spaces" in column
	 */
	private void clearAlternating(int middle, int xShift, int yLength){
		for(int clearIndex=xShift+1; clearIndex<yLength-xShift; clearIndex+=2){
			board[middle+xShift][clearIndex]=null;
			board[middle-xShift][clearIndex]=null;
		}
	}
	
	/**
	 * sets space to null that would be considered "outside" the board
	 * @param middle -index of the middle column of the board
	 * @param xShift -x index shift from middle column
	 * @param yLength -y length of "active spaces" in column
	 */
	private void clearOutside(int middle, int xShift, int yLength){
		for(int clearIndex=0; clearIndex< xShift; clearIndex++ ){
			board[middle+xShift][clearIndex]=null;
			board[middle+xShift][yLength-1-clearIndex]=null;
			board[middle-xShift][clearIndex]=null;
			board[middle-xShift][yLength-1-clearIndex]=null;
			
		}
			
	}
	
	@Override
	protected void clearMoveData(ChessMove move){
		move.clearMoveData(this);
	}
	
	@Override
	protected void buildMoveData(ChessMove move){
		move.buildMoveData(this);
	}
	
	@Override
	public void initializeMoves(){
		
		for(ChessMove move : movesToBuild)
			move.initializeMoveData(this);
		movesToBuild.clear();
	}
	
	@Override
	public BoardPanel getRepresentation(){
		return new HexagonalBoardPanel(this);
	}
	

}
