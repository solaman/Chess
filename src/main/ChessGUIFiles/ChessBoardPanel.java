package main.ChessGUIFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JPanel;

import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;
import main.CommandFiles.CommandSequence;
import main.chessBoards.HexagonalBoard;

/**
 * used to define a JPanel for a ChessBoard 
 * @author Solaman
 *
 */
@SuppressWarnings("serial")
public class ChessBoardPanel extends JPanel {

	/**
	 * 2d array of gridCells to reference how to draw each cell
	 */
	protected BoardCell[][] grid;
	
	/**
	 *length of cell side as an integer
	 */
	protected int cellSideLength;
	
	/**
	 * number of cells in the grid along the x axis
	 */
	protected int xLength;
	
	/**
	 * number of cells in the grid along the y axis
	 */
	protected int yLength;
	
	protected ChessBoard board;
	
	private ChessPiece activePiece;
	
	/**
	 * ChessBoardJPanel constructor
	 * uses passed in board to define the x and y length of grid.
	 * uses the screen resolution to decide the cellSideLength.
	 * and build grid and each cell along with proper color starting from
	 * topleft most cell
	 * @param board -ChessBoard the ChessBoardJPanel is based on
	 */
	public ChessBoardPanel(ChessBoard board) {
		
		activePiece= null;
		ChessMouseListener ml = new ChessMouseListener();            
		addMouseListener(ml);
		addMouseMotionListener(ml);
		
		this.board= board;
		this.setGridDimensions();
		this.buildGrid();
	}
	
	protected void buildGrid(){
		grid= new BoardCell[xLength][ yLength];
		for( int xIndex=0 ; xIndex < xLength ; xIndex++)
			for( int yIndex=0 ; yIndex < yLength ; yIndex++){
				grid[xIndex][yIndex]= new BoardCell();
				BoardCell toModify= grid[xIndex][yIndex];
				toModify.drawCellShape(pickColor(xIndex, yIndex), cellSideLength, xIndex, yLength-yIndex-1 );
				toModify.setSpaceReference( board.getChessSpace(xIndex, yIndex));
			}
	}
	
	public class ChessMouseListener extends MouseAdapter {
		
		public void mouseMoved( MouseEvent e){
			if(activePiece != null)
				return;
			BoardCell clickedCell= cellFromMouseEvent(e);
			if( clickedCell == null) return;
			ChessSpace clickedSpace= clickedCell.spaceReference;
			if(clickedSpace == null) return;
			ChessPiece occupant= clickedSpace.getOccupant();
			
			if(occupant != null && board.getPlayerTurn() == occupant.getPlayer()){
				clickedCell.setColor(BoardCell.HIGHLIGHTCOLOR);
				List< CommandSequence> toCheck= occupant.getAllCommandSequences(board);
				for(CommandSequence commandSequence : toCheck){
					ChessSpace toHighLight = commandSequence.getTargetSpace();
					grid[toHighLight.getXCoord()][toHighLight.getYCoord()].setColor(BoardCell.HIGHLIGHTCOLOR);
				}
			}
			repaint();
			
		}
		public void mouseClicked(MouseEvent e) { 
			BoardCell clickedCell= cellFromMouseEvent(e);
			
			if (clickedCell == null) return;
			ChessSpace clickedSpace= clickedCell.spaceReference;
			
			if(clickedSpace == null) return;

			if( activePiece != null){
				List< CommandSequence> toCheck= activePiece.getAllCommandSequences(board);
				for(CommandSequence commandSequence : toCheck)
					if( (clickedSpace == commandSequence.getTargetSpace())){
						try {
							board.performMovePermanent( commandSequence);
							activePiece = null;
							repaint();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						return;
					}
			}
			if(activePiece != null) activePiece = null;
			else if( clickedSpace.getOccupant() != null && board.getPlayerTurn() == clickedSpace.getOccupant().getPlayer()){
				clickedCell.setColor(BoardCell.HIGHLIGHTCOLOR);
				activePiece= clickedSpace.getOccupant();
				List< CommandSequence> toCheck= activePiece.getAllCommandSequences(board);
				for(CommandSequence commandSequence : toCheck){
					ChessSpace toHighLight = commandSequence.getTargetSpace();
					grid[toHighLight.getXCoord()][toHighLight.getYCoord()].setColor(BoardCell.HIGHLIGHTCOLOR);
				}
			}
			repaint();
			
		}		 
	} //end of MyMouseListe
	
	protected BoardCell cellFromMouseEvent(MouseEvent e){
		int xCoord = e.getX()/cellSideLength;
		int yCoord = e.getY()/cellSideLength; 
		if ( xCoord <0 || yCoord <0 || xCoord >= xLength || yCoord >= yLength )
			return null;
		
		return (grid[xCoord][yLength-yCoord-1]);
	}

	
	/**
	 * draws each individual cell of the grid
	 * @param graphics -Graphics to draw each cell on
	 */
	@Override
	public void paintComponent(Graphics graphics)
	{
		Graphics2D graphics2D = (Graphics2D)graphics;
		graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintComponent(graphics2D);
		//draw grid
		for (int xIndex=0; xIndex< xLength ; xIndex++) {
			for (int yIndex=0; yIndex < yLength; yIndex++) {
				grid[xIndex][yIndex].drawCell(graphics2D);
			}
		}
	}
	
	/**
	 * @return Dimension based on total pixel usage along width and length
	 */
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(xLength*cellSideLength, yLength*cellSideLength);
		
	}
	
	/**
	 * set grid's length along the x and y axis.
	 * set cell side length based off of screen resolution
	 * @param board
	 */
	protected void setGridDimensions() {
		xLength= board.getXLength();
		yLength= board.getYLength();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		cellSideLength= (int)Math.min(width, height)/ Math.max(xLength, yLength)/2;
		
	}

	/**
	 * decides between either White or Black color for a cell
	 * based on its distance cell wise from the topleft most cell
	 * @param xIndex -x index of the cell
	 * @param yIndex -y index of the cell
	 * @return -picked color for the cell
	 */
	protected Color pickColor(int xIndex, int yIndex){
		int colorIndex = ((xIndex % 2) +yIndex) % 2;
		if( colorIndex == 0)
			return Color.BLACK;
		return Color.WHITE;
	}
	
	
}
