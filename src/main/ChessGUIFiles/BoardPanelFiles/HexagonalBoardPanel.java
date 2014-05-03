package main.ChessGUIFiles.BoardPanelFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import main.chessBoards.HexagonalBoard;
import main.ChessSpace;

/**
 * @author Solaman
 * used to represent the Hexagonal Board in Swing,
 * see the Hexagonal Grid Model Structure to understand its structure
 */
public class HexagonalBoardPanel extends BoardPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ratio between a hexagonal cell's sidelength and the grid cell height used to contain them
	 */
	private static final double BOXHEIGHTRATIO = 0.86602540378;
	
	
	/**
	 * ratio between a hexagonal cell's sidelength and the grid cell length used to contain them
	 */
	private static final double BOXLENGTHRATIO= 1.5;
	
	public HexagonalBoardPanel(HexagonalBoard board) {
		super(board);
	}

	/* 
	 * get the grid cell that was clicked on and check if a hexagon representing a space was clicked by
	 * first checking a referenced hexagon (if one exists) or the current cell's referenced cells
	 */
	@Override
	public Cell cellFromMouseEvent(MouseEvent e){
		int xCoord= (int)((e.getX())/(cellSideLength*BOXLENGTHRATIO) );
		int yCoord= (int)((e.getY())/(cellSideLength*BOXHEIGHTRATIO) );
		if ( xCoord <0 || yCoord <0 || xCoord >= xLength || yCoord >= yLength )
			return null;
		if( player == 0)
			return ( (HexagonalCell)grid[xCoord][yLength-yCoord-1]).getClickedCell(e.getX(), e.getY());
		else
			return ( (HexagonalCell)grid[xLength-1-xCoord][yCoord]).getClickedCell(
					(int)((xLength-1)*cellSideLength*BOXLENGTHRATIO-e.getX()), 
					(int)((yLength)*cellSideLength*BOXHEIGHTRATIO-e.getY()));
	}
	
	@Override
	protected void buildGrid(){
		initializeGrid();
		connectBoardAndUI();
		buildReferences();	
	}
	
	/**
	 * because of a one to-one correspondence between the model and Panel representation
	 * we can link each ChessSpace to the Cell contained at the same coordinates
	 */
	private void connectBoardAndUI(){
		HexagonalCell cell;
		ChessSpace space;
		for(int x=0; x< board.getXLength() ; x++)
			for(int y=0; y< board.getYLength(); y++){
				space= board.getChessSpace(x, y);
				if( space != null){
					cell= (HexagonalCell)grid[x][y];
					cell.setSpaceReference( space );
					cell.createCellShape(pickColor(x, y ), cellSideLength, x, yLength-y-2 );
					cell.createReverseShape(xLength, yLength);
				}
			}
	}
	
	
	/**
	 * build the references to the cells starting from the middle of the board spanning outward.
	 * this is not symmetric, because an extra row is included in the View for protruding edges of
	 * the Hexagons. so this is done outside of the main loop. See the Hexagonal Grid Structure
	 * in the Hexagonal Model Structures.pdf to see exactly how they are laid out.
	 */
	private void buildReferences(){
		int half= (xLength-1)/2;
		int xShift;
		for(xShift=0; xShift<= half; xShift++){
			setReference(half+xShift+1, xShift, half+xShift, xShift);
			for( int y= xShift; y<yLength-xShift; y+=2){
				setReference(half-xShift, y, half-xShift-1, y-1);
				
				setReference(half-xShift, y+1, half-xShift, y);
				setReference(half-xShift, y+1, half-xShift-1, y+1);
				
				
				setReference(half+xShift, y, half+xShift-1, y-1);
				
				setReference(half+xShift, y+1, half+xShift-1, y+1);
				setReference(half+xShift, y+1, half+xShift, y);
			}
		}
		for( int y= xShift-1; y< yLength-xShift; y+=2){
			setReference(half+xShift, y, half+xShift, y-1);
			setReference(half+xShift, y, half+xShift-1, y);
			
			setReference(half+xShift, y+1, half+xShift-1, y);
		}
		
	}
	
	/**
	 * set a reference to a space if one exists at the given coordinates
	 * @param x1 -x coordinate of cell to receive reference
	 * @param y1 -y coordinate of cell to receive reference
	 * @param x2 -x coordinate of referenced cell
	 * @param y2 -y coordinate of referenced cell
	 */
	private void setReference(int x1, int y1, int x2, int y2){
		if( x2 < xLength && x2>= 0 && y2 < yLength && y2 >= 0)
			((HexagonalCell)grid[x1][y1]).addCellReference( (HexagonalCell)grid[x2][y2]);
	}
	
	/**
	 * instantiate all cells of the grid
	 */
	private void initializeGrid(){
		grid= new HexagonalCell[xLength][yLength];
		for( int x=0; x< xLength; x++)
			for( int y=0; y<yLength; y++){
				grid[x][y]= new HexagonalCell();
			}
	}
	
	
	@Override
	public Dimension getPreferredSize(){
		return new Dimension( (int)(cellSideLength*(xLength-.6)*BOXLENGTHRATIO) , (int)(cellSideLength*yLength*BOXHEIGHTRATIO) );
	}
	
	@Override
	protected void setGridDimensions(){
		xLength = board.getXLength()+1;
		yLength= (board.getYLength());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth()/2;
		double screenHeight = screenSize.getHeight()/2;
		
		setSideLength(screenWidth, screenHeight);
	}
	
	/**
	 * set side length of cells based on the maximums possible for a screensize/2
	 * @param width -width of panel
	 * @param height -height of panel
	 */
	private void setSideLength(double width, double height){
		double xMax= width/((xLength)*BOXLENGTHRATIO);
		double yMax= height/((yLength)*BOXHEIGHTRATIO);
		cellSideLength= (int) Math.min(xMax, yMax);
	}
	
	/* 
	 * set hexagon colors based on their shift from the center (colors reflect symmetrically on board)
	 */
	@Override
	protected Color pickColor(int xIndex, int yIndex){
		int xShift= ( (xLength-1)/2-xIndex)*( (xLength-1)/2-xIndex);
		int yShift= yIndex-xShift;
		int colorIndex = ((yShift % 3) +xShift) % 3;
		if( colorIndex == 0)
			return Color.BLACK;
		if( colorIndex == 1)
			return Color.GRAY;
		return Color.WHITE;
	}

}
