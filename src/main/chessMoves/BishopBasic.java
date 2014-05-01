package main.chessMoves;

import main.chessBoards.HexagonalBoard;
import main.ChessBoard;
import main.ChessMove;
import main.ChessPiece;


/**
 * defines behavior for the basic Bishop move
 * @author Solaman
 */
public final class BishopBasic extends ChessMove {
	
	/**
	 * @param owner- owner of move
	 */
	public BishopBasic(ChessPiece owner) {
		super(owner);
	}

	@Override
	public void buildMoveData(ChessBoard board) {
		buildMoveDataVector(board, 1, 1, -2);
		buildMoveDataVector(board, -1, -1, -2);
		buildMoveDataVector(board, 1, -1, -2);
		buildMoveDataVector(board, -1, 1, -2);
	}
	
	/*
	 * TODO requires testing
	 */
	@Override
	public void buildMoveData(HexagonalBoard board){
		buildMoveDataVector(board, 1, 3, -2);
		buildMoveDataVector(board, -1, 3, -2);
		buildMoveDataVector(board, 2, 0, -2);
		buildMoveDataVector(board, -2, 0, -2);
		buildMoveDataVector(board, 1, -3, -2);
		buildMoveDataVector(board, -1, -3, -2);
	}

	@Override
	public ChessMove copy(ChessPiece owner) {
		return new BishopBasic(owner);
	}
	
}
