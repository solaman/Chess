package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;
import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.moveHistory.MoveSequence;
import main.movePatterns.pawnMovePatterns.PawnEnPassant;
import main.movePatterns.pawnMovePatterns.PawnFirstMove;
import main.pieces.Pawn;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;

public class PawnEnPassantTest {

	Pawn pawn;
	Pawn pawnEvil;
	ChessBoard board;
	PawnEnPassant pep;
	PawnEnPassant pepe;
	ChessSpace pepS;
	ChessSpace pepeS;
	
	@Before
	public void setUp() throws Exception {
		board= new ChessBoard( 4,4);
		pawn= new Pawn(0);
		pawnEvil= new Pawn(1);
		pepS= board.getChessSpace(0,0);
		pepeS= board.getChessSpace(1,2);
		pawn.setPosition(pepS);
		pawnEvil.setPosition(pepeS);
		
		pep=new PawnEnPassant(pawn);
		pepe=new PawnEnPassant(pawnEvil);
		pepS.setOccupant(pawn);
		pepeS.setOccupant(pawnEvil);
		pep.buildMoveData((board));
		pepe.buildMoveData((board));
	}

	
	/**
	 * ensures a liftPlaceSequence is not available
	 * after a pawn of piecesToWatch does not move to the desired location
	 */
	@Test
	public void TestBuildPathFakeOut() {
		board.getChessSpace(1,2).setOccupant(null);
		board.getChessSpace(3,3).setOccupant(pawnEvil);
		pep.clearMoveData(board);
		pep.buildMoveData(board);
		assertTrue(board.getChessSpace(1,2).getDependentMoves().indexOf(pep) == -1);
		assertFalse(FindSpace.doIt( pep.getCommandSequences(board), board.getChessSpace(1,0)));
	}
	
	/**
	 * Ensures a liftPlaceSequence is available when a pawn of piecesToWatch
	 * moves to the desired location
	 */
	@Test
	public void TestBuildPathReal() {
		assertTrue( board.getChessSpace(1,0).getDependentMoves().indexOf(pep) != -1);
		ChessSpace target= board.getChessSpace(1,0);
		board.getChessSpace(1,2).setOccupant(null);
		target.setOccupant(pawnEvil);
		pawnEvil.setPosition( target);
		MoveSequence com= new MoveSequence(pawnEvil, new PawnFirstMove(pawnEvil), target, board.getChessSpace(1,2), target );
		board.getCommandHistory().add(com);
		pep.clearMoveData(board);
		pep.buildMoveData(board);
		assertTrue(FindSpace.doIt( pep.getCommandSequences(board), board.getChessSpace(1,0)) );
	}
	
	/**
	 * Ensures the move properly changes directions for an opposing player
	 */
	@Test
	public void TestBuildPathChangeDirection() {
		assertTrue( board.getChessSpace(0,2).getDependentMoves().indexOf(pepe) != -1);
		ChessSpace target= board.getChessSpace(0,2);
		board.getChessSpace(0,0).setOccupant(null);
		target.setOccupant(pawn);
		pawn.setPosition(target);
		MoveSequence com= new MoveSequence(pawn, new PawnFirstMove(pawn), target, board.getChessSpace(0,0), target );
		board.getCommandHistory().add(com);
		pepe.clearMoveData(board);
		pepe.buildMoveData(board);
		assertTrue(FindSpace.doIt( pepe.getCommandSequences(board), board.getChessSpace(0,2)));
	}
	
	/**
	 * ensures the liftPlaceSequence for the move exists when the move
	 * is still active despite any updates performed on the move
	 */
	@Test
	public void TestGetLiftPlaceSequenceSameTurn(){
		assertTrue( board.getChessSpace(1,0).getDependentMoves().indexOf(pep) != -1);
		ChessSpace target= board.getChessSpace(1,0);
		board.getChessSpace(1,2).setOccupant(null);
		target.setOccupant(pawnEvil);
		pawnEvil.setPosition( target);
		MoveSequence com= new MoveSequence(pawnEvil, new PawnFirstMove(pawnEvil), target, board.getChessSpace(1,2), target );
		board.getCommandHistory().add(com);
		pep.clearMoveData(board);
		pep.buildMoveData(board);
		assertTrue(FindSpace.doIt( pep.getCommandSequences(board), board.getChessSpace(1,0)) );
		pep.clearMoveData(board);
		pep.buildMoveData(board);
		assertTrue(FindSpace.doIt( pep.getCommandSequences(board) , board.getChessSpace(1,0)) );
	}
	
	/**
	 * ensures that no liftPlaceSequence exists
	 * after the move is not taken the turn it is active
	 */
	@Test
	public void TestGetLiftPlaceSequenceNextTurn(){
		board.getChessSpace(1,2).setOccupant(null);
		board.getChessSpace(1,0).setOccupant(pawnEvil);
		ChessSpace target= board.getChessSpace(1,0);
		MoveSequence com= new MoveSequence(pawnEvil, new PawnFirstMove(pawnEvil), target, board.getChessSpace(1,2), target );
		pawnEvil.setPosition( board.getChessSpace(1,0));
		board.getCommandHistory().add(com);
		pep.clearMoveData(board);
		pep.buildMoveData(board);
		assertTrue(FindSpace.doIt( pep.getCommandSequences(board) , board.getChessSpace(1,0)) );
		MoveSequence fake= new MoveSequence(null, null, board.getChessSpace(0,0), board.getChessSpace(0,0), null);
		board.getCommandHistory().add(fake);
		pep.clearMoveData(board);
		pep.buildMoveData(board);
		assertFalse(FindSpace.doIt( pep.getCommandSequences(board),  board.getChessSpace(1,0)) );
	}
	
	@Test
	public void TestBuildMoveHexagonalBoard() throws Exception{
		HexagonalBoard hBoard= new HexagonalBoard(9,10);
		hBoard.placePiece(pawn, 4, 2 );
		hBoard.placePiece(pawnEvil, 5, 1);
		MoveSequence com= new MoveSequence(pawnEvil, new PawnFirstMove(pawnEvil), null, board.getChessSpace(1,2), null );
		hBoard.getCommandHistory().add(com);
		pep.clearMoveData(board);
		pep.buildMoveData(hBoard);
		assertTrue( FindSpace.doIt(pep.getCommandSequences(hBoard), hBoard.getChessSpace(5,1)) );
		
	}
	

}
