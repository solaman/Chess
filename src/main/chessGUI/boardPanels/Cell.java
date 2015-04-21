package main.chessGUI.boardPanels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.boards.ChessSpace;
import main.chessGUI.ChessPieceRepresentations;
import main.pieces.ChessPiece;

/**
 * @author Solaman
 * contains relevant information used to display Chess Spaces in a JFrame
 */
public class Cell {

	/**
	 * used to draw the Chess Space itself
	 */
	protected Polygon cellShape;
	
	/**
	 * use this cell when drawing on the opponents turn
	 */
	protected Polygon reverseShape;
	
	/**
	 * use this coordinate when drawing on opposite turn
	 */
	int xReverse;
	int yReverse;
	
	/**
	 * default color of the Chess Space
	 */
	protected Color defaultColor;
	
	/**
	 * used to define the current color of the Space
	 */
	protected Color currentColor;
	
	/**
	 * defined highlight color 
	 */
	public final static Color HIGHLIGHTCOLOR = Color.YELLOW;
	
	/**
	 * outline color
	 */
	protected final static Color OUTLINECOLOR = Color.BLACK;
	
	/**
	 * coordinates of the Space
	 */
	protected int xCoord;
	protected int yCoord;
	
	/**
	 * sideLength of chessSpace
	 */
	protected int sideLength;
	
	/**
	 * ChessSpace that the cell draws
	 */
	public ChessSpace spaceReference;
	
	/**
	 * @param xCoord -x coordinate of chessSpace
	 * @param yCoord -y coordinate of chessSpace
	 * @param sideLength -length of ChessSpace side
	 * @param defaultColor -color to set the cell's default to
	 * @param board -board the ChessSpace is contained in
	 */
	public Cell() {
	}
	
	/**
	 * @param space to hook up cell with
	 */
	public void setSpaceReference(ChessSpace space){
		spaceReference= space;
	}
	
	/**
	 * @param xCoord of clicked cell (by index)
	 * @param yCoord of clicked cell (by index)
	 * @return cell that was clicked
	 */
	public Cell getClickedCell(int xIndex, int yIndex){
		return this;
	}
	
	/**
	 * create the shape of the cell
	 * @param defaultColor -default color of cell shape
	 * @param sideLength -side length of shape
	 * @param xCoord -starting xCoordinate of shape
	 * @param yCoord -starting y coordinate of shape
	 */
	public void createCellShape(Color defaultColor, int sideLength, int xCoord, int yCoord){
		this.xCoord= xCoord;
		this.yCoord= yCoord;
		this.sideLength= sideLength;
		cellShape = createSquare(xCoord, yCoord);
		this.defaultColor= defaultColor;
		this.currentColor= defaultColor;
	}
	
	/**
	 * should be called after createCellShape, this creates a shape at the reverse position
	 * to display on opposite turns
	 * @param xLength -x length of board
	 * @param yLength -y length of board
	 */
	public void createReverseShape( int xLength, int yLength){
		xReverse= xLength-xCoord-1;
		yReverse= yLength- yCoord-1;
		reverseShape= createSquare(xReverse, yReverse);
	}
	
	/**
	 * create a square shape from given coordinates
	 * @param xStart -starting x coordinate
	 * @param yStart -starting y coordinate
	 * @return -drawn shape
	 */
	private Polygon createSquare( int xStart, int yStart){
		Polygon square= new Polygon();
		square.addPoint(xStart*sideLength, yStart*sideLength);
		square.addPoint(xStart*sideLength, yStart*sideLength+sideLength);
		square.addPoint(xStart*sideLength+sideLength, yStart*sideLength+sideLength);
		square.addPoint(xStart*sideLength+sideLength, yStart*sideLength);
		return square;
	}

	/**
	 * Draws Chess Space that the cell refers to
	 * @param g2 graphic to draw cell in
	 * @param player TODO
	 */
	public void drawCell(Graphics2D g2, int player) {
		g2.setColor(currentColor);
		if(player== 0)
			g2.fillPolygon(cellShape);
		else
			g2.fillPolygon(reverseShape);
		setColor( defaultColor);
		
		g2.setColor( OUTLINECOLOR);
		if(player == 0){
			g2.drawPolygon( cellShape);
			fillCell(g2, xCoord, yCoord);
		}else
		{
			g2.drawPolygon(reverseShape);
			fillCell(g2, xReverse, yReverse);
		}
	}
	
	/**
	 * Draws the occupant of the Chess Space (if one exists)
	 * @param g2 graphic to draw cell in 
	 * @param xCoord1 TODO
	 * @param yCoord1 TODO
	 */
	protected void fillCell(Graphics2D g2, int xCoord1, int yCoord1) {
		ChessPiece occupant= spaceReference.getOccupant();
		if(occupant == null) return;
		ChessPieceRepresentations reps= new ChessPieceRepresentations();
		try {
			Image occupantImage= scaleOccupant(occupant.getRepresentation( reps)) ;
			drawOccupant(g2, occupantImage, xCoord1, yCoord1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	protected void drawOccupant(Graphics2D g2, Image occupantImage, int xCoord1, int yCoord1){
		g2.drawImage( occupantImage, xCoord1*sideLength, yCoord1*sideLength, null);
	}
	
	/**
	 * draws occupant of the Chess Space
	 * @param g -graphic to draw occupant in
	 * @param image -image used to represent the occupant
	 * @param xStart -x coordinate to place the image
	 * @param yStart -y coordinate to place the image
	 */
	public Image scaleOccupant(BufferedImage image) {

	    double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()),
	    		new Dimension(sideLength, sideLength)));

	    int scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
	    int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

	    return image.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

	}
	
	/**
	 * 
	 * @param iMasterSize
	 * @param iTargetSize
	 * @return
	 */
	protected double getScaleFactor(int iMasterSize, int iTargetSize) {
	    double dScale = 1;
	    if (iMasterSize > iTargetSize)
	        dScale = (double) iTargetSize / (double) iMasterSize;
	    else 
	        dScale = (double) iTargetSize / (double) iMasterSize;

	    return dScale;

	}

	private double getScaleFactorToFit(Dimension original, Dimension toFit) {

	    double dScale = 1d;

	    if (original != null && toFit != null) {

	        double dScaleWidth = getScaleFactor(original.width, toFit.width);
	        double dScaleHeight = getScaleFactor(original.height, toFit.height);

	        dScale = Math.min(dScaleHeight, dScaleWidth);

	    }

	    return dScale;

	}

	public void setColor(Color highlightcolor2) {
		currentColor= highlightcolor2;
		
	}

}
