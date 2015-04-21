package main.ChessGUIFiles.MouseAdapters;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.SwingUtilities;

import DragnGhost.GhostGlassPane;
import DragnGhost.GhostMotionAdapter;
import main.ChessPiece;
import main.ChessSpace;
import main.ChessGUIFiles.ChessPieceRepresentations;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.BoardPanelFiles.Cell;

/**
 * much of this code is borrowed from http://www.jroller.com/gfx/entry/drag_with_style_in_swing.
 * Because the original code is built for components, and all the data to interact with is contained
 * mostly inside a single component, much of the code is adapted to reflect these requirements
 * @author Solaman
 * 
 * Used for creating games. Allows users to "drag and drop" piece icons from one board (list of available pieces)
 * to another (the actual board of play)
 */
public class CreateMouseAdapter extends PlayMouseAdapter {

	/**
	 * used to draw piece Images when "dragging"
	 */
	protected GhostGlassPane glassPane;
	
	/**
	 * used to retrieve image files for various pieces
	 */
	protected ChessPieceRepresentations reps;
	
	/**
	 * stores the image of a piece when being "dragged"
	 */
	protected BufferedImage pieceImage;
	
	public BoardPanel boardToCreate;
	public GhostMotionAdapter ghostAdapter;
	/**
	 * @param panel -panel of board to drag pieces from
	 * @param glassPane -pane used to draw images of pieces when being "dragged"
	 * TODO avoid ChessPieceRepresentations from being hardcoded?
	 */
	public CreateMouseAdapter(BoardPanel panel, GhostGlassPane glassPane) {
		super(panel);
		this.glassPane= glassPane;
		reps = new ChessPieceRepresentations();
		pieceImage= null;
		ghostAdapter=new GhostMotionAdapter(glassPane);
		
		panel.addMouseMotionListener(ghostAdapter);
	}
	
	 /* 
	  * when a piece is found, draw its ghost image and store data to 
	  * "drag" the piece
	 */
	@Override
	public void mousePressed(MouseEvent e)
	    {
		 	setData(e);
			Component c = e.getComponent();

	        glassPane.setVisible(true);

	        Point p = (Point) e.getPoint().clone();
	        SwingUtilities.convertPointToScreen(p, c);
	        SwingUtilities.convertPointFromScreen(p, glassPane);

	        glassPane.setPoint(p);
	        glassPane.setImage( pieceImage);
	        glassPane.repaint();
	    }
		 	
	 /**
	  * if pieceImage is not null, a piece is being dragged, so skip.
	  * else set data necessary to drag a piece from panel to board, 
	  * as well as display the image of the piece
	  * @param e -mouse event to retrieve the image and piece with
	 */
	protected void setData(MouseEvent e){
		 if(pieceImage != null)
			 return;
		 
		Cell pressedCell = panel.cellFromMouseEvent(e);
        if( pressedCell == null){
        	pressedCell= boardToCreate.cellFromMouseEvent( SwingUtilities.convertMouseEvent(e.getComponent(), e, boardToCreate));
        	if(pressedCell == null)
        		return;
        	ChessSpace clickedSpace= pressedCell.spaceReference;
        	if(clickedSpace == null)
        		return;
        	if(clickedSpace.getOccupant() == null) 
        		return;
        	activePiece= clickedSpace.getOccupant();
        	boardToCreate.board.removePiece(activePiece);
        	
        } else{
			ChessSpace clickedSpace= pressedCell.spaceReference;
			if(clickedSpace == null) return;
			ChessPiece occupant= clickedSpace.getOccupant();
			if(occupant == null) return;
			
			activePiece= occupant.copy();
        }
		try {
			pieceImage = toBufferedImage( 
					pressedCell.scaleOccupant( activePiece.getRepresentation( reps ) ) );
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 }
	 
	/* 
	 * currently, this just sets pieceImage and activePiece to null, and repaints the glassPane
	 */
	@Override
	public void mouseReleased(MouseEvent e){
		ChessSpace hurrdurr = panel.spaceFromMouseEvent(e);
		if( hurrdurr == null && boardToCreate != null){
			hurrdurr= boardToCreate.spaceFromMouseEvent( SwingUtilities.convertMouseEvent(e.getComponent(), e, boardToCreate));
			if( hurrdurr != null){
				boardToCreate.board.setPiece( activePiece, hurrdurr.getXCoord(), hurrdurr.getYCoord());
			 }
		}
		pieceImage= null;
		activePiece= null;
			 
		Component c = e.getComponent();
		
		Point p = (Point) e.getPoint().clone();
		SwingUtilities.convertPointToScreen(p, c);
		
		SwingUtilities.convertPointFromScreen(p, glassPane);
		
		glassPane.setPoint(p);
		glassPane.setVisible(false);
		glassPane.setImage(null);
	}
	
	@Override
	protected void addListenersToPanel(){
		panel.addMouseListener(this);
	}
	
	/**
	 * converts an Image to a BufferedImage
	 * @param img -image to convert
	 * @return -converted image
	 */
	protected static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage)
	    {
	        return (BufferedImage) img;
	    }

	    // Create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	    // Draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics();
	    bGr.drawImage(img, 0, 0, null);
	    bGr.dispose();

	    // Return the buffered image
	    return bimage;
	}

}
