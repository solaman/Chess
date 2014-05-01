package test.ChessMoves;

import static org.junit.Assert.*;

import java.util.List;

import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.KingBasic;

import org.junit.Before;
import org.junit.Test;

public class KingBasicTest {

	ChessBoard board;
	ChessPiece king;
	KingBasic kingmove;
	
	List<ChessSpace> toMove;
	@Before
	public void setUp() throws Exception {
		board= new ChessBoard( 5,5);
		king= new ChessPiece(1);
		kingmove= new KingBasic(king);
		
		board.getChessSpace(2,2).setOccupant( king);	
		
		king.setPosition(board.getChessSpace(2,2));
		kingmove.buildMoveData(board);
		
	}
	
	/**
	 * functionality of the abstract move class has been tested in TestMoveWithBishop,
	 * only check to see if the correct spaces are affected, as KingBasic uses
	 * the ChessMove functions almost exclusively
	 */
	@Test
	public void TestUpdateMove(){
		assertTrue(board.getChessSpace(3,2).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(3,3).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(2,3).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(1,2).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(2,1).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(1,1).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(3,1).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue(board.getChessSpace(1,3).getDependentMoves().indexOf(kingmove) != -1);
		assertFalse(board.getChessSpace(4,2).getDependentMoves().indexOf(kingmove) != -1);
		assertFalse(board.getChessSpace(2,2).getDependentMoves().indexOf(kingmove) != -1);
	}
	
	/**
	 * assert that correct dependencies are laid for HexagonalBoards;
	 * @throws Exception 
	 */
	@Test
	public void TestHexagonalBoard() throws Exception{
		HexagonalBoard hBoard= new HexagonalBoard(9,10);
		hBoard.placePiece(king, hBoard.getChessSpace(6, 6));
		kingmove.clearMoveData(board);
		kingmove.buildMoveData(hBoard);
		assertTrue(kingmove.getCommandSequences(hBoard).size() == 12);
		assertTrue( hBoard.getChessSpace(8,6).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(7, 3).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(5, 3).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(4, 6).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(5, 9).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(6, 8).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(7, 7).getDependentMoves().indexOf(kingmove) != -1);
		
		assertTrue( hBoard.getChessSpace(7,5).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(6, 4).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(5, 5).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(5, 7).getDependentMoves().indexOf(kingmove) != -1);
		assertTrue( hBoard.getChessSpace(6, 10).getDependentMoves().indexOf(kingmove) == -1);
	}
}

