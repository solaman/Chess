package test;

import static org.junit.Assert.*;
import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.games.StandardChess;
import main.movePatterns.RookMovePattern;
import main.pieces.King;
import main.pieces.Rook;

import org.junit.Before;
import org.junit.Test;

public class ChessBoardTest {

	ChessBoard board;
	StandardChess game;
	King king;
	Rook rook1;
	Rook rook2;
	
	@Before
	public void setUp() throws Exception {
			board= new ChessBoard( 8, 8);
			rook1= new Rook( 1);
			king= new King(0);
			rook2= new Rook(1);
			
	}
	
	/**
	 * assert that null is returned when passed coordinate is out of bounds
	 */
	@Test
	public void testGetChessSpaceOutOfBounds() {
		assertNull(board.getChessSpace(-1, 0));
		assertNull(board.getChessSpace(0, -1));
		assertNull(board.getChessSpace(9, 1));
		assertNull(board.getChessSpace(1, 9));
	}
	
	/**
	 * assert that a space is returned when passed coordinate is in bounds
	 */
	@Test
	public void testGetChessSpace(){
		assertNotNull(board.getChessSpace(1,1));
	}
	
	/**
	 * asserts that a piece is threatened at a space when a piece
	 * can capture it in one move
	 */
	@Test
	public void testCanThreatenTrue(){
		king.setPosition(board.getChessSpace(0,0));
		rook1.setPosition(board.getChessSpace(1,0));
		RookMovePattern rookMove= new RookMovePattern(rook1);
		rook1.addMoves(rookMove);
		rookMove.buildMoveData(board);
		assertTrue(board.canThreaten(king, king.getPosition()));
	}
	
	/**
	 * asserts that a piece is not threatened at a space when no 
	 * piece can capture it in one move
	 */
	@Test
	public void testCanThreatenFalse(){
		king.setPosition(board.getChessSpace(0,0));
		rook1.setPosition(board.getChessSpace(1,1));
		RookMovePattern rookMove= new RookMovePattern(rook1);
		rook1.addMoves(rookMove);
		rookMove.buildMoveData(board);
		assertFalse(board.canThreaten(king, king.getPosition()));
	}
	
	/**
	 * asserts that the correct occupant is returned for getOccupant when occupant is not null
	 */
	@Test
	public void testGetOccupantNotNull(){
		board.getChessSpace(0,0).setOccupant(king);
		assertTrue( king == board.getOccupant(0,0));
	}
	
	/**
	 * assert that null is returned when no occupant exists
	 */
	@Test
	public void testGetOccupantNullOccupant(){
		ChessSpace toCheck=board.getChessSpace(0,0);
		toCheck.setOccupant(null);
		assertNull( board.getOccupant(0,0));
	}
	
	/**
	 * assert that null is returned when space does not exist
	 */
	@Test
	public void testGetOccupantNullSpace(){
		assertNull( board.getOccupant(-1,-1));
	}
	
	/**
	 * assert that placePiece sets correct occupant for space and position for piece
	 */
	@Test
	public void testPlacePieceSetOccupantAndPosition(){
		board.placePiece( rook1, board.getChessSpace(1,1));
		assertTrue("space doesn't have correct occupant after placePice", board.getChessSpace(1,1).getOccupant() == rook1);
		assertTrue("piece doesn't have correct position after placePiece", rook1.getPosition() == board.getChessSpace(1,1));
	}
	
	
	/**
	 *asserts that space's occupant and piece's position are set to null after liftPiece 
	 */
	@Test
	public void testLiftPieceSetOccupantAndPosition(){
		rook1.setPosition(board.getChessSpace(1,0));
		board.getChessSpace(1,0).setOccupant(rook1);
		board.liftPiece(board.getChessSpace(1,0));
		
		assertTrue("space has occupant after liftPiece", board.getChessSpace(1,0).getOccupant() == null);
		assertTrue("piece has position after liftPiece", rook1.getPosition() == null);
	}
	
	
	

}

