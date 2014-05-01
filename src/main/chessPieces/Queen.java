package main.chessPieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.ChessPiece;
import main.ChessGUIFiles.ChessPieceRepresentations;
import main.chessGames.GlinskisChess;
import main.chessGames.ShafransChess;
import main.chessGames.StandardChess;
import main.chessMoves.BishopBasic;
import main.chessMoves.RookBasic;

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
		new RookBasic(this);
		new BishopBasic(this);
	}
	
	public Queen(GlinskisChess glinskisChess, int player) {
		super(player);
		new RookBasic(this);
		new BishopBasic(this);
	}
	
	public Queen(ShafransChess game, int player){
		super(player);
		new RookBasic(this);
		new BishopBasic(this);
	}

	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}

}
