package main.games;

import java.util.ArrayList;
import java.util.List;

import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.pieces.Bishop;
import main.pieces.ChessPiece;
import main.pieces.King;
import main.pieces.Knight;
import main.pieces.Pawn;
import main.pieces.Queen;
import main.pieces.Rook;

/**
 * @author Solaman
 * used to build a board set up for Glinski's Chess
 * refer to http://en.wikipedia.org/wiki/Hexagonal_chess to learn more
 */
public class GlinskisChess extends ChessGame {
	
	public static final String NAME = "Glinski's Chess";
	
	@Override
	public HexagonalBoard setUp() {
		try {
			HexagonalBoard board= new HexagonalBoard( 11, 11);
			setUpPieces(board);
			return board;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	@Override
	public String getName(){
		return NAME;
	}
	
	private void setUpPieces(HexagonalBoard board) {
		 column=0;
		 yDelta=1;
		for(int i=0; i< 2; i++){
			List<ChessPiece> playerPieces=  new ArrayList<ChessPiece>();
			if(i == 1){
				column=20;
				yDelta=-1;
			}
			Rook rook1= new Rook(this, i);
			Rook rook2= new Rook(this, i);
			board.placePiece(rook1, 2, column+3*yDelta);
			board.placePiece(rook2, 8, column+3*yDelta);
			playerPieces.add(rook1);
			playerPieces.add(rook2);
			
			Knight k1= new Knight(this, i);
			Knight k2= new Knight(this, i);
			board.placePiece(k1, 3, column+2*yDelta);
			board.placePiece(k2, 7, column+2*yDelta);
			playerPieces.add(k1);
			playerPieces.add(k2);
			
			Bishop b1= new Bishop(this, i);
			Bishop b2= new Bishop(this, i);
			Bishop b3= new Bishop(this, i);
			board.placePiece( b1, 5, column);
			board.placePiece( b2, 5, column+2*yDelta);
			board.placePiece( b3, 5, column+4*yDelta);
			playerPieces.add(b1);
			playerPieces.add(b2);
			playerPieces.add(b3);
			
			King k= new King(this, i);
			Queen q= new Queen(this, i);
			board.placePiece(k, 6, column+1*yDelta);
			board.placePiece( q, 4, column+1*yDelta);
			playerPieces.add(k);
			playerPieces.add(q);
			
			placePawns(i, board, playerPieces);
			board.pieces.add(playerPieces);
		}
		board.initializeMoves();
		
	}

	private void placePawns(int player, ChessBoard board, List<ChessPiece> playerPieces){
		Pawn middle= new Pawn(this, player);
		board.placePiece(middle, 5, column+8*yDelta);
		playerPieces.add(middle);
		for(int i=1; i< 5; i++){
			Pawn pawn= new Pawn(this, player);
			board.placePiece(pawn, 5+i, column+(8-i)*yDelta);
			playerPieces.add(pawn);
			Pawn pawn2= new Pawn(this, player);
			board.placePiece(pawn2, 5-i, column+(8-i)*yDelta);
			playerPieces.add(pawn2);
			
		}
			
	}


}
