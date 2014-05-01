package main.chessPieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.ChessPiece;
import main.ChessGUIFiles.ChessPieceRepresentations;
import main.chessGames.GlinskisChess;
import main.chessGames.ShafransChess;
import main.chessGames.StandardChess;
import main.chessMoves.BishopBasic;

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
		new BishopBasic(this);
		
	}
	
	public Bishop(GlinskisChess game, int player){
		super(player);
		new BishopBasic(this);
	}
	
	public Bishop(ShafransChess game, int player){
		super(player);
		new BishopBasic(this);
	}
	
	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}
}
