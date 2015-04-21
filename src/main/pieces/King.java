package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.chessGUI.ChessPieceRepresentations;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;
import main.movePatterns.KingMovePattern;
import main.movePatterns.CastlingMovePattern;

/**
 * Class used to build a King Piece
 * @author Solaman
 *
 */
public class King extends ChessPiece {
	
	/**
	 * Constructs a King with no move loadout
	 * @param player
	 */
	public King(int player){
		super(player);
	}
	
	@Override
	public ChessPiece copy(){
		return new King(player);
	}
	
	/**
	 * Construct a King with moves for standard play
	 * @param game -game to build king for
	 * @param player -player to build king for
	 */
	public King(StandardChess game, int player){
		super(player);
		new KingMovePattern(this);
		new CastlingMovePattern(this);
	}
	
	public King(GlinskisChess game, int player){
		super(player);
		new KingMovePattern(this);
	}
	
	public King(ShafransChess game, int player){
		super(player);
		new KingMovePattern(this);
		new CastlingMovePattern(this);
	}
	
	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}
	

}
