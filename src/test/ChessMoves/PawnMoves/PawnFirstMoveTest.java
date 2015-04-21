package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;
import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.games.ChessGame;
import main.movePatterns.pawnMovePatterns.PawnFirstMove;
import main.pieces.ChessPiece;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;

public class PawnFirstMoveTest {

	ChessBoard board;
	ChessGame game;
	ChessPiece pawnOfJustice;
	ChessPiece pawnOfEvil;
	ChessPiece friendOfJustice;
	PawnFirstMove pawnMove;
	
	ChessSpace current;
	
	
	@Before
	public void setUp() throws Exception {
		board=new ChessBoard( 1,3);
		pawnOfJustice=new ChessPiece(0);
		friendOfJustice=new ChessPiece(0);
		pawnOfEvil= new ChessPiece(1);
		int[] s={0,0};
		pawnOfJustice.setPosition(board.getChessSpace(s));
		int[] s1={0,2};
		friendOfJustice.setPosition(board.getChessSpace(s1));
		int[] s2={0,2};
		pawnOfEvil.setPosition(board.getChessSpace(s2));
		current= board.getChessSpace(0,0);
		current.setOccupant(pawnOfJustice);
		
	}
	
	/**
	 * assert that direction is changed when the move is owned by a piece of player 1
	 */
	@Test
	public void testUpdatePathDataChangeDirection(){
		board.getChessSpace(0,2).setOccupant(pawnOfEvil);
		pawnMove= new PawnFirstMove(pawnOfEvil);
		pawnOfEvil.setPosition( board.getChessSpace(0,2));
		pawnMove.buildMoveData(board);
		assertTrue( board.getChessSpace(0,0).getDependentMoves().indexOf(pawnMove) != -1);
	}
	
	/**
	 * assets that proper space dependencies and liftPlaceSequence exists when the move
	 * is available
	 */
	@Test
	public void testBuildPath(){
		board.getChessSpace(0,0).setOccupant(pawnOfJustice);
		pawnOfJustice.setPosition(board.getChessSpace(0,0));
		pawnMove= new PawnFirstMove(pawnOfJustice);
		pawnMove.buildMoveData(board);
		assertTrue(board.getChessSpace(0,1).getDependentMoves().indexOf(pawnMove) != -1);
		assertTrue(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,2)) );
		//game.updateTurnNumber();
		pawnOfJustice.incrementMovesMade();//TODO check if functionality is broken
		pawnMove.clearMoveData(board);
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,2)) );
		
	}

	/**
	 * asserts that spaceDependencies are laid, but the liftPlaceSequence for the move
	 * is not available when either space the move goes passed is occupied,
	 * alternatively checking for friends and enemies (but not both for both spaces)
	 */
	@Test
	public void testUpdatePathDataOccupied(){
		board.getChessSpace(0,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnFirstMove(pawnOfJustice);
		board.getChessSpace(0,1).setOccupant(friendOfJustice);
		pawnOfJustice.setPosition( board.getChessSpace(0,0));
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
		board.getChessSpace(0,1).setOccupant(null);
		board.getChessSpace(0,2).setOccupant(pawnOfEvil);
		pawnMove.clearMoveData(board);
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,2)) );
		assertTrue(board.getChessSpace(0,1).getDependentMoves().indexOf(pawnMove) != -1);
		assertTrue(board.getChessSpace(0,2).getDependentMoves().indexOf(pawnMove) != -1);
	}
	
	/**
	 * tests that all data is cleared by performing updatePathData when
	 * piece of the move is not on the board
	 */
	@Test
	public void testClearPathData(){
		board.getChessSpace(0,0).setOccupant(pawnOfJustice);
		pawnMove= new PawnFirstMove(pawnOfJustice);
		board.getChessSpace(0,1).setOccupant(friendOfJustice);
		pawnMove.buildMoveData(board);
		board.getChessSpace(0,1).setOccupant(null);
		pawnOfJustice.setPosition(null);
		pawnMove.clearMoveData(board);
		pawnMove.buildMoveData(board);
		assertFalse(FindSpace.doIt(pawnMove.getCommandSequences(board), board.getChessSpace(0,1)) );
	}
	
	/**
	 * asserts that the move cannot threaten a friend or an enemy piece
	 */
	@Test
	public void testCanThreaten(){
		pawnMove= new PawnFirstMove(pawnOfJustice);
		assertFalse (pawnMove.canThreaten(pawnOfEvil) );
		assertFalse (pawnMove.canThreaten(pawnOfJustice));
	}

}
