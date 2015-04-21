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
 * used to set up a board for Shafran's Chess, refer to http://en.wikipedia.org/wiki/Hexagonal_chess to learn more
 */
public class ShafransChess extends ChessGame {
	
	public static final String NAME= "Shafran's Chess";

	@Override
	public HexagonalBoard setUp() {
		try {
			HexagonalBoard board= new HexagonalBoard( 9, 10);
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
		
		for(int i=0; i< 2; i++){
			column=0;
			yDelta=1;
			List<ChessPiece> playerPieces=  new ArrayList<ChessPiece>();
			if(i == 1){
				column=18;
				yDelta=-1;
			}
			int middle= 4;
			King k= new King(this, i);
			board.placePiece(k, middle, column);
			playerPieces.add(k);
			
			Queen q= new Queen(this, i);
			board.placePiece( q, middle+yDelta*-1, column+1*yDelta);
			playerPieces.add(q);
			
			Bishop b1= new Bishop(this, i);
			board.placePiece( b1, middle+yDelta*1, column+1*yDelta);
			playerPieces.add(b1);
			
			Bishop b2= new Bishop(this, i);
			board.placePiece( b2, middle+yDelta*-2, column+2*yDelta);
			playerPieces.add(b2);
			
			Knight k1= new Knight(this, i);
			board.placePiece(k1, middle+yDelta*2, column+2*yDelta);
			playerPieces.add(k1);
			
			Knight k2= new Knight(this, i);
			board.placePiece(k2, middle+yDelta*-3, column+3*yDelta);
			playerPieces.add(k2);
			
			Bishop b3= new Bishop(this, i);
			board.placePiece(b3, middle+yDelta*3, column+3*yDelta);
			playerPieces.add(b3);
			
			Rook rook1= new Rook(this, i);
			Rook rook2= new Rook(this, i);
			board.placePiece(rook1, 0, column+4*yDelta);
			board.placePiece(rook2, 8, column+4*yDelta);
			playerPieces.add(rook1);
			playerPieces.add(rook2);	
			
			placePawns(i, board, playerPieces);
			board.pieces.add(playerPieces);
		}
		board.initializeMoves();
		
	}

	private void placePawns(int player, ChessBoard board, List<ChessPiece> playerPieces){
		Pawn middle= new Pawn(this, player);
		board.placePiece(middle, 4, column+2*yDelta);
		playerPieces.add(middle);
		for(int i=1; i< 5; i++){
			Pawn pawn= new Pawn(this, player);
			board.placePiece(pawn, 4+i, column+(2+i)*yDelta);
			playerPieces.add(pawn);
			Pawn pawn2= new Pawn(this, player);
			board.placePiece(pawn2, 4-i, column+(2+i)*yDelta);
			playerPieces.add(pawn2);
			
		}
			
	}

}
