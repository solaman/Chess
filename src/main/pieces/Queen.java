package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.chessGUI.ChessPieceRepresentations;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;
import main.movePatterns.BishopMovePattern;
import main.movePatterns.RookMovePattern;

/**
 * class to build a queen for play
 * @author Solaman
 *
 */
public class Queen extends ChessPiece {

	/**
	 * Constructs a queen with no move loadout
	 * @param player
	 */
	public Queen(int player){
		super(player);
	}
	
	@Override
	public ChessPiece copy(){
		return  new Queen( player);
	}
	
	/**
	 * Construct a queen with moves for standard play
	 * @param game -game to build queen for
	 * @param p -player to build queen for
	 */
	public Queen(StandardChess game, int p){
		super(p);
		new RookMovePattern(this);
		new BishopMovePattern(this);
	}
	
	public Queen(GlinskisChess glinskisChess, int player) {
		super(player);
		new RookMovePattern(this);
		new BishopMovePattern(this);
	}
	
	public Queen(ShafransChess game, int player){
		super(player);
		new RookMovePattern(this);
		new BishopMovePattern(this);
	}

	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}

}
