package test;

import static org.junit.Assert.*;

import org.junit.Test;

import main.chessBoards.HexagonalBoard;

public class HexagonalBoardTest {

	HexagonalBoard board;

	@Test(expected= Exception.class)
	public void testBoardEvenX() throws Exception {
		board= new HexagonalBoard( 6, 4);
	}
	
	@Test(expected= Exception.class)
	public void testBoardShortY() throws Exception{
		 board= new HexagonalBoard( 7, 2);
	}
	
	@Test
	public void testBoardAlmostSmall() throws Exception{
		board= new HexagonalBoard( 3, 3);
		assertNull(board.getChessSpace(1,5));
		assertNull(board.getChessSpace(2, 4));
	    assertNull("Error: active space should be inactive", board.getChessSpace(2, 5) );
	    assertNull("Error: active space should be inactive", board.getChessSpace(0, 5) );
		assertNotNull( "Error: inactive space should be active", board.getChessSpace(1, 4) );
		assertNotNull(board.getChessSpace(1,2));
	}
	
	@Test
	public void testBoardSmallest() throws Exception{
		board= new HexagonalBoard( 3, 2);
		assertNull("Error: active space should be inactive", board.getChessSpace(0, 2) );
		assertNull("Error: active space should be inactive", board.getChessSpace(2, 2) );
		assertNotNull( "Error: inactive space should be active", board.getChessSpace(1, 2) );
	}
	
	@Test
	public void testBoardRightShape()throws Exception{
		board = new HexagonalBoard(11, 11);
		assertNull(board.getChessSpace(0, 17));
		assertNull(board.getChessSpace(0, 16));
		assertNotNull(board.getChessSpace(0,15));
		assertNull(board.getChessSpace(10, 17));
		assertNull(board.getChessSpace(10, 16));
		assertNotNull(board.getChessSpace(10,15));
	}

}
