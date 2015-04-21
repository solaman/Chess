package main.pieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.chessGUI.ChessPieceRepresentations;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;
import main.movePatterns.pawnMovePatterns.PawnBasic;
import main.movePatterns.pawnMovePatterns.PawnCapture;
import main.movePatterns.pawnMovePatterns.PawnCaptureMcCooeys;
import main.movePatterns.pawnMovePatterns.PawnEnPassant;
import main.movePatterns.pawnMovePatterns.PawnEnPassantMcCooeys;
import main.movePatterns.pawnMovePatterns.PawnFirstMove;
import main.movePatterns.pawnMoves.firstMovePatterns.PawnFirstMoveGlinskis;
import main.movePatterns.pawnMoves.firstMovePatterns.PawnFirstMoveShafrans;

/**
 * Class used to build a pawn for play
 * @author Solaman
 *
 */
public class Pawn extends ChessPiece {

	/**
	 * Pawn constructor with no move load out
	 * @param player
	 */
	public Pawn(int player) {
		super(player);
	}
	
	@Override
	public ChessPiece copy(){
		return new Pawn(player);
	}
	
	/**
	 * Construct Pawn with moves for standard play
	 * @param game- game to built piece for
	 * @param player- player to built piece for
	 */
	public Pawn(StandardChess game, int player){
		super(player);
		new PawnBasic(this);
		new PawnCapture(this);
		new PawnFirstMove(this);
		new PawnEnPassant(this);
	}
	
	public Pawn(GlinskisChess glinskisChess, int player) {
		super(player);
		new PawnBasic(this);
		new PawnCapture(this);
		new PawnEnPassant(this);
		new PawnFirstMoveGlinskis(this);
	}
	
	public Pawn(ShafransChess game, int player){
		super(player);
		new PawnBasic(this);
		new PawnCaptureMcCooeys(this);
		new PawnFirstMoveShafrans(this);
		new PawnEnPassantMcCooeys(this);
	}

	@Override
	public BufferedImage getRepresentation(ChessPieceRepresentations reps) throws IOException{
		return reps.getRepresentation(this);
	}

	
}
