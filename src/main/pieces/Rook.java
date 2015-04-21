package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.chessGUI.ChessPieceRepresentations;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;
import main.movePatterns.RookMovePattern;

/**
 * Class used to build a Rook piece for play
 * @author Solaman
 *
 */
public class Rook extends ChessPiece { 
 
	/**
	 * Constructs a rook with no move loadout
	 * @param player
	 */
	public Rook(int player) {
		super(player);
	}
	
	@Override
	public ChessPiece copy(){
		return new Rook(player);
	}

	/**
	 * Construct a Rook with moves for standard play
	 * @param standardChess -game to build piece for
	 * @param player -player to build piece for
	 */
	public Rook(StandardChess standardChess, int player) {
		super(player);
		new RookMovePattern(this);
	}
	
	public Rook(GlinskisChess glinskisChess, int player) {
		super(player);
		new RookMovePattern(this);
	}
	
	public Rook(ShafransChess game, int player){
		super(player);
		new RookMovePattern(this);
	}

	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}

}
