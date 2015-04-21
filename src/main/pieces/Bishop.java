package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.chessGUI.ChessPieceRepresentations;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;
import main.movePatterns.BishopMovePattern;

/**
 * class used to build a bishop piece for play
 * @author Solaman
 *
 */
public class Bishop extends ChessPiece {

	/**
	 * Constructs a bishop with no move loadout
	 * @param player
	 */
	public Bishop(int player){
		super(player);
	}
	
	@Override
	public ChessPiece copy(){
		return new Bishop(player);
	}
	
	/**
	 * Constructs a bishop with move loadout for a Standard game
	 * @param game -game to build the piece for
	 * @param player -player to build the piece for
	 */
	public Bishop(StandardChess game, int player){
		super(player);
		new BishopMovePattern(this);
		
	}
	
	public Bishop(GlinskisChess game, int player){
		super(player);
		new BishopMovePattern(this);
	}
	
	public Bishop(ShafransChess game, int player){
		super(player);
		new BishopMovePattern(this);
	}
	
	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}
}
