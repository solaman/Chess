package main.chessGames;

import java.util.ArrayList;
import java.util.List;

import main.ChessBoard;
import main.ChessGame;
import main.ChessPiece;
import main.chessPieces.Bishop;
import main.chessPieces.King;
import main.chessPieces.Knight;
import main.chessPieces.Pawn;
import main.chessPieces.Queen;
import main.chessPieces.Rook;

/**
 * Prepares a game for standard chess,
 * Includes building an 8x8 Chess Board
 * and laying out the Chess Pieces with their
 * StandardGame loadout
 * @author Solaman
 *
 */
public class StandardChess extends ChessGame {
	
	public static final String NAME= "Standard Chess";
	
	@Override
	public ChessBoard setUp(){
		try {
			ChessBoard board= new ChessBoard(8, 8);
			setUpPieces(board);
			return board;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String getName(){
		return NAME;
	}
	
	private void setUpPieces(ChessBoard board) {
		int column=0;
		for(int i=0; i< 2; i++){
			List<ChessPiece> playerPieces=  new ArrayList<ChessPiece>();
			if(i == 1) column=7;
			Rook rook1= new Rook(this, i);
			Rook rook2= new Rook(this, i);
			board.placePiece(rook1, 0, column);
			board.placePiece(rook2, 7, column);
			playerPieces.add(rook1);
			playerPieces.add(rook2);
			
			Knight k1= new Knight(this, i);
			Knight k2= new Knight(this, i);
			board.placePiece(k1, 1, column);
			board.placePiece(k2, 6, column);
			playerPieces.add(k1);
			playerPieces.add(k2);
			
			Bishop b1= new Bishop(this, i);
			Bishop b2= new Bishop(this, i);
			board.placePiece( b1, 2, column);
			board.placePiece( b2, 5, column);
			playerPieces.add(b1);
			playerPieces.add(b2);
			
			King k= new King(this, i);
			Queen q= new Queen(this, i);
			board.placePiece(k, 4, column);
			board.placePiece( q, 3, column);
			playerPieces.add(k);
			playerPieces.add(q);
			
			placePawns(board, playerPieces, i);
			board.pieces.add(playerPieces);
		}
		board.initializeMoves();
		
	}

	private void placePawns(ChessBoard board, List<ChessPiece> playerPieces, int player){
		int column=1;
		if(player == 1)
			column=6;
		for(int i=0; i< 8; i++){
			Pawn pawn= new Pawn(this, player);
			board.placePiece(pawn, i, column);
			playerPieces.add(pawn);
		}
			
	}

}
