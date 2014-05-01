package main.chessPieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.ChessPiece;
import main.ChessGUIFiles.ChessPieceRepresentations;
import main.chessGames.GlinskisChess;
import main.chessGames.ShafransChess;
import main.chessGames.StandardChess;
import main.chessMoves.KingBasic;
import main.chessMoves.KingCastling;

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
		new KingBasic(this);
		new KingCastling(this);
	}
	
	public King(GlinskisChess game, int player){
		super(player);
		new KingBasic(this);
	}
	
	public King(ShafransChess game, int player){
		super(player);
		new KingBasic(this);
		new KingCastling(this);
	}
	
	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}
	

}
