package main.ChessGUIFiles.BoardPanelFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;

import main.chessBoards.HexagonalBoard;
import main.ChessSpace;

public class HexagonalBoardPanel extends BoardPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final double BOXHEIGHTRATIO = 0.86602540378;
	private static final double BOXLENGTHRATIO= 1.5;
	
	public HexagonalBoardPanel(HexagonalBoard board) {
		super(board);
	}

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
	
	private void setReference(int x1, int y1, int x2, int y2){
		if( x2 < xLength && x2>= 0 && y2 < yLength && y2 >= 0)
			((HexagonalCell)grid[x1][y1]).addCellReference( (HexagonalCell)grid[x2][y2]);
	}
	
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
	
	private void setSideLength(double width, double height){
		double xMax= width/((xLength)*BOXLENGTHRATIO);
		double yMax= height/((yLength)*BOXHEIGHTRATIO);
		cellSideLength= (int) Math.min(xMax, yMax);
	}
	
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
