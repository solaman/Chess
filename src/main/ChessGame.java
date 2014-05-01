package main;

/**
 * Sets up the chess game it represents by building a ChessBoard, setting up ChessPiece's (as well as their moves)
 * @author Solaman
 *
 */
public abstract class ChessGame {
	protected int column;
	protected int yDelta;
	
	public abstract ChessBoard setUp();

	public abstract String getName();
	
	
}
