package main.ChessGUIFiles.BoardPanelFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import main.ChessBoard;
import main.ChessSpace;

/**
 * used to define a JPanel for a ChessBoard 
 * @author Solaman
 *
 */
@SuppressWarnings("serial")
public class BoardPanel extends JPanel {

	/**
	 * 2d array of gridCells to reference how to draw each cell
	 */
	public Cell[][] grid;
	
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
	
	public ChessBoard board;
	
	public int player;
	
	/**
	 * ChessBoardJPanel constructor
	 * uses passed in board to define the x and y length of grid.
	 * uses the screen resolution to decide the cellSideLength.
	 * and build grid and each cell along with proper color starting from
	 * topleft most cell
	 * @param board -ChessBoard the ChessBoardJPanel is based on
	 */
	public BoardPanel(ChessBoard board) {          
		
		this.board= board;
		this.setGridDimensions();
		this.buildGrid();
		player=0;
	}
	
	public void setPlayer(int player){
		this.player= player;
	}
	
	protected void buildGrid(){
		grid= new Cell[xLength][ yLength];
		for( int xIndex=0 ; xIndex < xLength ; xIndex++)
			for( int yIndex=0 ; yIndex < yLength ; yIndex++){
				grid[xIndex][yIndex]= new Cell();
				Cell toModify= grid[xIndex][yIndex];
				toModify.createCellShape(pickColor(xIndex, yIndex), cellSideLength, xIndex, yLength-yIndex-1 );
				toModify.createReverseShape(xLength, yLength);
				toModify.setSpaceReference( board.getChessSpace(xIndex, yIndex));
			}
	}
	

	public Cell cellFromMouseEvent(MouseEvent e){
		int xCoord = e.getX()/cellSideLength;
		int yCoord = e.getY()/cellSideLength; 
		if ( xCoord <0 || yCoord <0 || xCoord >= xLength || yCoord >= yLength )
			return null;
		if( player == 0)
			return (grid[xCoord][yLength-yCoord-1]);
		else
			return (grid[xLength-xCoord-1][yCoord]);
	}
	
	public ChessSpace spaceFromMouseEvent(MouseEvent e){
		Cell cell= cellFromMouseEvent(e);
		if(cell == null)
			return null;
		return cell.spaceReference;
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
				grid[xIndex][yIndex].drawCell(graphics2D, player);
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
