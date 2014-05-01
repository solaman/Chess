package main.chessPieces;

import java.awt.image.BufferedImage;
import java.io.IOException;

import main.ChessPiece;
import main.ChessGUIFiles.ChessPieceRepresentations;
import main.chessGames.GlinskisChess;
import main.chessGames.ShafransChess;
import main.chessGames.StandardChess;
import main.chessMoves.pawnMoves.PawnBasic;
import main.chessMoves.pawnMoves.PawnCapture;
import main.chessMoves.pawnMoves.PawnCaptureMcCooeys;
import main.chessMoves.pawnMoves.PawnEnPassant;
import main.chessMoves.pawnMoves.PawnEnPassantMcCooeys;
import main.chessMoves.pawnMoves.PawnFirstMove;
import main.chessMoves.pawnMoves.firstMoves.PawnFirstMoveGlinskis;
import main.chessMoves.pawnMoves.firstMoves.PawnFirstMoveShafrans;

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
