package test;

import static org.junit.Assert.*;
import main.boards.ChessBoard;
import main.games.ChessGame;
import main.pieces.ChessPiece;

import org.junit.Before;
import org.junit.Test;

public class ChessPieceTest {
	
	ChessBoard board;
	ChessPiece piece;
	ChessGame game;

	@Before
	public void setUp() throws Exception {
		board=new ChessBoard( 5,5);
		piece=new ChessPiece(0);
		piece.setPosition(board.getChessSpace(0,0));
		board.getChessSpace(0,0).setOccupant(piece);
	}

	/**
	 * assert functionality of getters and setters
	 */
	@Test
	public void TestChessPieceGettersSetters() {
		assertTrue("getPlayer returns incorrect value", piece.getPlayer() == 0);
		assertTrue("getXCoord returns incorrect value after setPosition", piece.getXCoord() == 0);
		assertTrue("getYCoord returns incorrect value after setPosition", piece.getYCoord() == 0);
		assertTrue("getPosition returns incorrect value after setPosition", piece.getPosition() == board.getChessSpace(0,0));
		piece.setPosition( null);
		assertTrue("setPosition sets wrong ChessSpace for null", piece.getPosition() == null);
		assertTrue("setPosition sets wrong coords for null", piece.getXCoord() == -1 && piece.getYCoord() == -1);
	}
	
	/**
	 * asserts that hasMoved returns true when setFirstTurnMoved is set once
	 */
	@Test
	public void testHasMovedAfterSet(){
		assertFalse(piece.hasMoved());
		piece.incrementMovesMade();
		assertTrue(piece.hasMoved());
	}
	

}
