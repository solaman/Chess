package main.movePatterns;

import main.pieces.ChessPiece;

/**
 * Pawns have a vertical movement dependent on their owner.
 * store yDelta to easily modify the behavior
 * without redefining in the subclasses
 * @author Solaman
 */
public abstract class PawnMovePattern extends MovePattern {

	protected int yDelta ;
	
	/**
	 * @param owner -owner of PawnMove
	 */
	public PawnMovePattern(ChessPiece owner) {
		super(owner);
		yDelta = 1;
		if(owner.getPlayer() == 1)
			yDelta = -1;
	}

}
