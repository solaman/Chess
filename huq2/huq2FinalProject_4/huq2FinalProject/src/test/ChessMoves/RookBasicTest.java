package test.ChessMoves;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.RookBasic;

import org.junit.Before;
import org.junit.Test;

public class RookBasicTest {

	ChessBoard board;
	ChessPiece rook;
	RookBasic rookMove;
	
	ChessSpace current;
	List<ChessSpace> toMove;
	
	@Before
	public void setUp() throws Exception {
		board=new ChessBoard( 5,5);
		current=board.getChessSpace(2,2);
		rook=new ChessPiece(1);
		
		current.setOccupant(rook);	
		
		rookMove=new RookBasic(rook);
		rook.setPosition( board.getChessSpace(2,2));
		rookMove.buildMoveData(board);
	}
	
	/**
	 * Rook uses mostly function of ChessMove class, which was tested in TestMoveWithBishop,
	 * only test that the proper directions of the path exist.
	 */
	@Test
	public void testUpdatePathData() {
		assertTrue(board.getChessSpace(2,3).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue(board.getChessSpace(3,2).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue(board.getChessSpace(1,2).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue(board.getChessSpace(2,1).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue(board.getChessSpace(2,4).getDependentMoves().indexOf(rookMove) != -1);
		assertFalse(board.getChessSpace(3,3).getDependentMoves().indexOf(rookMove) != -1);
	}
	
	@Test
	public void testUpdateMoveDataHexagonal() throws Exception{
		HexagonalBoard hBoard= new HexagonalBoard(5,5);
		hBoard.placePiece(rook, hBoard.getChessSpace(2, 4));
		rookMove.clearMoveData(board);
		rookMove.buildMoveData(hBoard);
		assertTrue(rookMove.getCommandSequences(hBoard).size() == 12);
		assertTrue( hBoard.getChessSpace(2,6).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue( hBoard.getChessSpace(3, 5).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue( hBoard.getChessSpace(3, 3).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue( hBoard.getChessSpace(2, 2).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue( hBoard.getChessSpace(1, 3).getDependentMoves().indexOf(rookMove) != -1);
		assertTrue( hBoard.getChessSpace(1, 5).getDependentMoves().indexOf(rookMove) != -1);
	}

}
