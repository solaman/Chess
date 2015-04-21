package main.chessGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.boards.ChessSpace;
import main.pieces.ChessPiece;

/**
 * @author Solaman
 * contains relevant information used to display Chess Spaces in a JFrame
 */
public class BoardCell {

	/**
	 * used to draw the Chess Space itself
	 */
	protected Polygon cellShape;
	
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
	public BoardCell() {
	}
	
	public void setSpaceReference(ChessSpace space){
		spaceReference= space;
	}
	
	public BoardCell getClickedCell(int xCoord, int yCoord){
		return this;
	}
	
	public void drawCellShape(Color defaultColor, int sideLength, int xCoord, int yCoord){
		cellShape = new Polygon();
		this.xCoord= xCoord;
		this.yCoord= yCoord;
		this.sideLength= sideLength;
		cellShape.addPoint(xCoord*sideLength, yCoord*sideLength);
		cellShape.addPoint(xCoord*sideLength, yCoord*sideLength+sideLength);
		cellShape.addPoint(xCoord*sideLength+sideLength, yCoord*sideLength+sideLength);
		cellShape.addPoint(xCoord*sideLength+sideLength, yCoord*sideLength);
		this.defaultColor= defaultColor;
		this.currentColor= defaultColor;
	}
	

	/**
	 * Draws Chess Space that the cell refers to
	 * @param g2 graphic to draw cell in
	 */
	public void drawCell(Graphics2D g2) {
		g2.setColor(currentColor);
		g2.fillPolygon(cellShape);
		setColor( defaultColor);
		
		g2.setColor( OUTLINECOLOR);
		g2.drawPolygon( cellShape);
		
		fillCell(g2);
	}

	/**
	 * Draws the occupant of the Chess Space (if one exists)
	 * @param g2 graphic to draw cell in 
	 */
	protected void fillCell(Graphics2D g2) {
		ChessPiece occupant= spaceReference.getOccupant();
		if(occupant == null) return;
		ChessPieceRepresentations reps= new ChessPieceRepresentations();
		try {
			drawOccupant(g2, occupant.getRepresentation( reps),xCoord*sideLength, yCoord*sideLength) ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * draws occupant of the Chess Space
	 * @param g -graphic to draw occupant in
	 * @param image -image used to represent the occupant
	 * @param xStart -x coordinate to place the image
	 * @param yStart -y coordinate to place the image
	 */
	protected void drawOccupant(Graphics2D g, BufferedImage image, int xStart,int yStart) {


	    double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()),
	    		new Dimension(sideLength, sideLength)));

	    int scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
	    int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

	    Image scaled = image.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

	    g.drawImage(scaled, xStart, yStart, null);

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
