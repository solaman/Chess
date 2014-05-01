package main.chessMoves;

import main.ChessMove;
import main.ChessPiece;

/**
 * Pawns have a vertical movement dependent on their owner.
 * store yDelta to easily modify the behavior
 * without redefining in the subclasses
 * @author Solaman
 */
public abstract class PawnMove extends ChessMove {

	protected int yDelta ;
	
	/**
	 * @param owner -owner of PawnMove
	 */
	public PawnMove(ChessPiece owner) {
		super(owner);
		yDelta = 1;
		if(owner.getPlayer() == 1)
			yDelta = -1;
	}

}
