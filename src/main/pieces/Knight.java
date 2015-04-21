package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.chessGUI.ChessPieceRepresentations;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;
import main.movePatterns.KnightMovePattern;

/**
 * Class used to build a Knight for play
 * @author Solaman
 *
 */
public class Knight extends ChessPiece{

	/**
	 * Constructs a Knight with no move loadout
	 * @param player
	 */
	public Knight(int player){
		super(player);
	}
	
	@Override
	public ChessPiece copy(){
		return new Knight(player);
	}
	
	/**
	 * Construct a knight with moves for standard play
	 * @param game- game to build knight for
	 * @param player- player to build knight for
	 */
	public Knight(StandardChess game, int player){
		super(player);
		new KnightMovePattern(this);
	}
	
	public Knight(GlinskisChess glinskisChess, int player) {
		super(player);
		new KnightMovePattern(this);
	}
	
	public Knight(ShafransChess game, int player){
		super(player);
		new KnightMovePattern(this);
	}

	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}

}
