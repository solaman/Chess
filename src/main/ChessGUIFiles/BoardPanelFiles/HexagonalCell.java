package main.ChessGUIFiles.BoardPanelFiles;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.ChessSpace;

public class HexagonalCell extends Cell {
	
	/**
	 * ratio of a cells height to the sidelength of a hexagon
	 */
	private static final double BOXHEIGHTRATIO = 0.86602540378;
	
	/**
	 * ratio of a cell's length to the sidelength of a hexagon
	 */
	private static final double BOXLENGTHRATIO= 1.5;
	
	/**
	 * list of cells this cell references (used for getClickedCell)
	 */
	List<HexagonalCell> referenceCells;
	
	public HexagonalCell() {
		referenceCells= new ArrayList<HexagonalCell>();
	}
	
	public void setSpaceReference(ChessSpace space){
		spaceReference= space;
	}
	
	/**
	 * @param cell to add to references
	 */
	public void addCellReference(HexagonalCell cell){
		if( cell.cellShape != null)
			referenceCells.add(cell);
	}
	
	private boolean contains(int xCoord, int yCoord){
			return cellShape.contains(xCoord, yCoord);
	}
	
	/* (non-Javadoc)
	 * @see main.ChessGUIFiles.BoardPanelFiles.Cell#getClickedCell(int, int)
	 * checks to see if this cell contains the point
	 * if not, checks to see if its references contains the point
	 * (note: super uses index, this uses coordinates)
	 */
	@Override
	public Cell getClickedCell(int xCoord, int yCoord){
		if( xCoord < 0 || yCoord <0)
			System.out.println("negative flag");
		if( cellShape != null && cellShape.contains(xCoord, yCoord)){
			return this;
		}
		for(HexagonalCell cell : referenceCells)
			if( cell.contains(xCoord, yCoord)){
				return cell;
			}
		return null;
	}
	
	@Override
	public void createCellShape(Color defaultColor, int sideLength, int xCoord, int yCoord){
		this.sideLength= sideLength;
		this.xCoord= (int) (xCoord* (double)(sideLength*BOXLENGTHRATIO) );
		this.yCoord= (int) (yCoord*(double)(sideLength*BOXHEIGHTRATIO) );
		cellShape= createHexagon(this.xCoord, this.yCoord);
		this.defaultColor= defaultColor;
		this.currentColor= defaultColor;
	}
	
	@Override
	public void createReverseShape(int xLength, int yLength){
		xReverse= (int)((xLength-2)*(double)(sideLength*BOXLENGTHRATIO))-xCoord;
		yReverse= (int)((yLength-2)*(double)(sideLength*BOXHEIGHTRATIO))-yCoord;
		reverseShape= createHexagon(xReverse, yReverse);
	}
	/**
	 * create Polygon in the shape of a hexagon
	 * @param xCoord1 -top rightmost corner's x coordinate
	 * @param yCoord1 -top rightmost corner's y coordinate
	 * @param sideLength -length of sides
	 * @return
	 */
	private Polygon createHexagon( int xCoord1, int yCoord1) {
		Polygon hexagon= new Polygon();
		hexagon.addPoint(xCoord1+(int)(.5*sideLength), yCoord1);
		hexagon.addPoint(xCoord1+(int)(1.5*sideLength), yCoord1);
		hexagon.addPoint(xCoord1+(int)(2*sideLength), yCoord1+(int)(sideLength*BOXHEIGHTRATIO));
		hexagon.addPoint(xCoord1+(int)(1.5*sideLength), yCoord1+(int)(sideLength*BOXHEIGHTRATIO*2));
		hexagon.addPoint(xCoord1+(int)(.5*sideLength), yCoord1+(int)(sideLength*BOXHEIGHTRATIO*2));
		hexagon.addPoint(xCoord1, yCoord1+(int)(sideLength*BOXHEIGHTRATIO));
	
		return hexagon;
	}

	/**
	 * Draw Cell as if there were no contained objects
	 * @param g2
	 */
	@Override
	public void drawCell(Graphics2D g2, int player) {
		if( cellShape == null)
			return;
		super.drawCell(g2, player);
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

	@Override
	public Image scaleOccupant(BufferedImage image) {

	    double scaleFactor = Math.min(1d, getScaleFactorToFit(new Dimension(image.getWidth(), image.getHeight()),
	    		new Dimension( (int)(sideLength), (int)(sideLength*BOXHEIGHTRATIO*2) )));

	    int scaleWidth = (int) Math.round(image.getWidth() * scaleFactor);
	    int scaleHeight = (int) Math.round(image.getHeight() * scaleFactor);

	    return image.getScaledInstance(scaleWidth, scaleHeight, Image.SCALE_SMOOTH);

	}

	@Override
	protected void drawOccupant(Graphics2D g, Image occupantImage, int xCoord1, int yCoord1){
		g.drawImage(occupantImage, xCoord1+(int)(sideLength*.5), yCoord1+(int)(sideLength*BOXHEIGHTRATIO*.5), null );
	}

}
