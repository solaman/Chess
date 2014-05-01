package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;
import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.pawnMoves.PawnBasic;

public class PawnBasicTest {

	ChessBoard board;
	ChessPiece pawnOfJustice;
	ChessPiece pawnOfEvil;
	ChessPiece friendOfJustice;
	PawnBasic pawnMove;
	
	ChessSpace current;
	
	
	@Before
	public void setUp() throws Exception {
		board=new ChessBoard( 1,2);
		pawnOfJustice=new ChessPiece(0);
		friendOfJustice=new ChessPiece(0);
		pawnOfEvil= new ChessPiece(1);
		int[] s={0,0};
		pawnOfJustice.setPosition(board.getChessSpace(s));
		int[] s1={0,1};
		friendOfJustice.setPosition(board.getChessSpace(s1));
		pawnOfEvil.setPosition(board.getChessSpace(s1));
		current= board.getChessSpace(0,0);
		current.setOccupant(pawnOfJustice);
		
	}
	
	/**
	 * test to see if direction is changed when used by a different player
	 */
	@Test
	public void testUpdatePathDataChangeDirection(){
		board.getChessSpace(0,1).setOccupant(pawnOfEvil);
		pawnOfEvil.setPosition( board.getChessSpace(0,1));
		pawnMove= new PawnBasic(pawnOfEvil);
		pawnMove.buildMoveData(board);
		assertTrue( board.getChessSpace(0,0).getDependentMoves().indexOf(pawnMove) != -1);
	}
	
	/**
	 * Checks to see if correct liftPlaceSequence is made when path is cleared
	 */
	@Test
	public void testUpdatePathLiftPlaceSequence(){
		board.getChessSpace(0,0).setOccupant(pawnOfJustice);
		pawnOfJustice.setPosition(board.getChessSpace(0,0));
		pawnMove= new PawnBasic(pawnOfJustice);
		pawnMove.buildMoveData(board);
		assertTrue(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
	}

	/**
	 * Checks to see if no liftPlaceSequence exists when space is blocked by
	 * enemy for friend, but a space dependency is at least placed.
	 */
	@Test
	public void testUpdatePathDataOccupied(){
		board.getChessSpace(0,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnBasic(pawnOfJustice);
		pawnOfJustice.setPosition( board.getChessSpace(0,0));
		board.getChessSpace(0,1).setOccupant(friendOfJustice);
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
		board.getChessSpace(0,1).setOccupant(pawnOfEvil);
		pawnMove.clearMoveData(board);
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
		assertTrue(board.getChessSpace(0,1).getDependentMoves().indexOf(pawnMove) != -1);
	}
	
	/**
	 * checks to see that data is cleared after the piece is lifted
	 * (should be cleared in this case)
	 */
	@Test
	public void testClearPathData(){
		board.getChessSpace(0,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnBasic(pawnOfJustice);
		pawnOfJustice.setPosition( board.getChessSpace(0,0));
		board.getChessSpace(0,1).setOccupant(friendOfJustice);
		pawnMove.buildMoveData(board);
		board.getChessSpace(0,0).setOccupant(null);
		pawnOfJustice.setPosition( null);
		pawnMove.clearMoveData(board);
		pawnMove.buildMoveData(board);
		assertTrue( board.getChessSpace(0,1).getDependentMoves().indexOf(pawnMove) == -1);
	}
	
	/**
	 * asserts that the move cannot threaten any piece
	 */
	@Test
	public void testCanThreaten(){
		pawnMove= new PawnBasic(pawnOfJustice);
		assertFalse (pawnMove.canThreaten(pawnOfEvil) );
		assertFalse (pawnMove.canThreaten(pawnOfJustice));
	}
	
	@Test
	public void testHexagonalBoard() throws Exception{
		HexagonalBoard boardH= new HexagonalBoard(3,3);
		pawnOfJustice.setPosition( boardH.getChessSpace(1,0));
		boardH.getChessSpace(1,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnBasic(pawnOfJustice);
		pawnMove.buildMoveData(boardH);
		assertTrue(boardH.getChessSpace(1,2).getDependentMoves().indexOf(pawnMove) != -1);
		
	}
}
