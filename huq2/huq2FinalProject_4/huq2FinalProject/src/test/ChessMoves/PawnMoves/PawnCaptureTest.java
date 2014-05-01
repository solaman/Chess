package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;
import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;
import main.chessMoves.pawnMoves.PawnCapture;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;

public class PawnCaptureTest {

	ChessBoard board;
	ChessPiece pawnOfJustice;
	ChessPiece pawnOfEvil;
	ChessPiece friendOfJustice;
	PawnCapture pawnMove;
	
	ChessSpace current;
	
	
	@Before
	public void setUp() throws Exception {
		board=new ChessBoard( 3,2);
		pawnOfJustice=new ChessPiece(0);
		friendOfJustice=new ChessPiece(0);
		pawnOfEvil= new ChessPiece(1);
		int[] s={1,0};
		pawnOfJustice.setPosition(board.getChessSpace(s));
		int[] s1={0,1};
		friendOfJustice.setPosition(board.getChessSpace(s1));
		pawnOfEvil.setPosition(board.getChessSpace(s1));
	}
	
	/**
	 * asserts that a liftPlaceSequence is properly changed when the move is for 
	 * a player other than player 0
	 */
	@Test
	public void testUpdatePathDataChangeDirection(){
		int[] s1={1,1};
		pawnOfEvil.setPosition(board.getChessSpace(s1));
		pawnOfEvil.setPosition( board.getChessSpace(1,1));
		board.getChessSpace(1,1).setOccupant(pawnOfEvil);
		pawnMove= new PawnCapture(pawnOfEvil);
		pawnMove.buildMoveData(board);
		assertTrue( board.getChessSpace(0,0).getDependentMoves().indexOf(pawnMove) != -1);
	}

	/**
	 * Asserts that a proper liftPlaceSequence is made when a player can capture
	 * a piece, and asserts that the other exists on the other side
	 */
	@Test
	public void testUpdatePathDataEnemyOccupant(){
		board.getChessSpace(1,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnCapture(pawnOfJustice);
		board.getChessSpace(0,1).setOccupant(pawnOfEvil);
		board.getChessSpace(2,1).setOccupant(pawnOfEvil);
		pawnOfJustice.setPosition( board.getChessSpace(1,0));
		pawnMove.buildMoveData(board);
		assertTrue(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
		assertTrue(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(2,1)) );
		
	}
	
	/**
	 * asserts that all data is cleared by performing updatePathData when the piece of the move
	 * is no longer on the board
	 */
	@Test
	public void testClearPathData(){
		board.getChessSpace(1,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnCapture(pawnOfJustice);
		pawnMove.buildMoveData(board);
		board.getChessSpace(1,0).setOccupant(null);
		pawnOfJustice.setPosition(null);
		pawnMove.clearMoveData(board);
		pawnMove.clearMoveData(board);
		assertTrue( board.getChessSpace(0,1).getDependentMoves().indexOf(pawnMove) == -1);
	}
	
	/**
	 * assert that no liftPlaceSequence is available when a capture space is either
	 * unoccupied or occupied by a friend. asserts that proper spaceDependencies exist.
	 */
	@Test
	public void testBuildPathDataNoMoveAvailable(){
		pawnOfJustice.setPosition( board.getChessSpace(1,0));
		board.getChessSpace(1,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnCapture(pawnOfJustice);
		board.getChessSpace(0,1).setOccupant(friendOfJustice);
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(2,1)) );
		assertTrue( board.getChessSpace(0,1).getDependentMoves().indexOf(pawnMove) != -1);
		assertTrue( board.getChessSpace(2,1).getDependentMoves().indexOf(pawnMove) != -1);
		assertFalse( board.getChessSpace(1,1).getDependentMoves().indexOf(pawnMove) != -1);
	}

}
