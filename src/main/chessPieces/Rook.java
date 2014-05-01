package main.chessPieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.ChessPiece;
import main.ChessGUIFiles.ChessPieceRepresentations;
import main.chessGames.GlinskisChess;
import main.chessGames.ShafransChess;
import main.chessGames.StandardChess;
import main.chessMoves.RookBasic;

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
		new RookBasic(this);
	}
	
	public Rook(GlinskisChess glinskisChess, int player) {
		super(player);
		new RookBasic(this);
	}
	
	public Rook(ShafransChess game, int player){
		super(player);
		new RookBasic(this);
	}

	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}

}
