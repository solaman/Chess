package main.chessMoves;


import main.chessBoards.HexagonalBoard;
import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;

/**
 * defines behavior for the basic King move
 * @author Solaman
 */
public final class KingBasic extends ChessMove {

	/**
	 * constructor
	 * @param owner -owner of move
	 */
	public KingBasic(ChessPiece owner) {
		super(owner);
	}

	@Override
	public void buildMoveData(ChessBoard board) {
		for( int i=-1; i<2; i++)
			for(int j=-1; j<2; j++)
				if(  i != 0 || j != 0 )
					buildMoveDataVector(board, i, j, 1);
	}
	
	/*
	 * TODO requires testing
	 */
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildMoveDataSpaces(board,  0, 2,  0, -2,  2, 0,  -2, 0,
				-1, 1,   -1, -1,    1, 1,   1, -1,   
				1, 3,   1, -3,    -1, 3,   -1, -3);
	}

	@Override
	public ChessMove copy(ChessPiece owner) {
		return new KingBasic(owner);
	}

}
