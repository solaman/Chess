package test.ChessMoves;

import static org.junit.Assert.*;

import java.util.List;

import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;
import main.CommandFiles.CommandSequence;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.BishopBasic;

import org.junit.Before;
import org.junit.Test;

public class BishopBasicTest {

	ChessBoard board;
	ChessPiece bishop;
	ChessPiece friend;
	ChessPiece enemy1;
	ChessPiece enemy2;
	BishopBasic bishopMove;
	List< CommandSequence> cs;
	
	ChessSpace current;
	List<ChessSpace> toMove;
	
	@Before
	public void setUp() throws Exception {
		board=new ChessBoard(5,5);
		current=board.getChessSpace(2,2);
		
		bishop=new ChessPiece(1);
		bishopMove=new BishopBasic(bishop);
		current.setOccupant(bishop);
		int[] s= {2,2};
		bishop.setPosition(board.getChessSpace(s));
		bishop.setPosition(current);
		
		friend=new ChessPiece(1);
		enemy1=new ChessPiece(2);
		enemy2=new ChessPiece(2);
		
		board.getChessSpace(3,3).setOccupant(enemy1);
		board.getChessSpace(1,3).setOccupant(friend);
		board.getChessSpace(0,0).setOccupant(enemy2);
		bishopMove.buildMoveData(board);
		cs = bishopMove.getCommandSequences(board);
		
	}
	

	
	/**
	 * tests that a path stops at an enemy space
	 * checks that a space dependency is laid, but a liftPlaceSequence is not
	 */
	@Test
	public void testBuildPathFriendlyOccupant() {
		assertTrue( board.getChessSpace(1,3).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( board.getChessSpace(0,4).getDependentMoves().indexOf(bishopMove) == -1);
		assertFalse( FindSpace.doIt(cs, board.getChessSpace(1,3)) );
		assertFalse( FindSpace.doIt(cs, board.getChessSpace(0, 4)));
		
	}
	
	/**
	 * tests that a path stops at an enemy space,
	 * checks that a space dependency and liftPlaceSequence is made for the space
	 */
	@Test
	public void testBuildPathEnemyOccupant(){
		assertTrue( board.getChessSpace(3,3).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( FindSpace.doIt(cs, board.getChessSpace(3,3) ) );
		assertTrue( board.getChessSpace(0,4).getDependentMoves().indexOf(bishopMove) == -1);
		assertFalse( FindSpace.doIt(cs, board.getChessSpace(0, 4)));
	}
	
	/**
	 * tests that the path adds spaces as a continuous vector
	 * checks both the space dependencies and liftPlaceSequence
	 */
	@Test
	public void testBuildPathNoOccupant(){
		assertTrue( board.getChessSpace(0,0).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( FindSpace.doIt(cs, board.getChessSpace(0, 0)));
	}
	
	/**
	 * assert that clear path clears SpaceDependencies properly
	 * assume that if one is cleared, all are cleared
	 */
	@Test
	public void testClearPath(){
		bishop.setPosition(null);
		bishopMove.clearMoveData(board);
		assertTrue( board.getChessSpace(3,3).getDependentMoves().indexOf(bishopMove) == -1);
	}
	
	/**
	 * check if proper dependent spaces are laid on a hexagonal board.
	 * @throws Exception 
	 */
	@Test
	public void testHexagonalBoard() throws Exception{
		HexagonalBoard hBoard= new HexagonalBoard(9,10);
		hBoard.placePiece(bishop, hBoard.getChessSpace(6, 6));
		bishopMove.clearMoveData(board);
		bishopMove.buildMoveData(hBoard);
		assertTrue(bishopMove.getCommandSequences(hBoard).size() == 12);
		assertTrue( hBoard.getChessSpace(8,6).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( hBoard.getChessSpace(7, 3).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( hBoard.getChessSpace(5, 3).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( hBoard.getChessSpace(4, 6).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( hBoard.getChessSpace(5, 9).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( hBoard.getChessSpace(7, 9).getDependentMoves().indexOf(bishopMove) != -1);
		assertTrue( hBoard.getChessSpace(4, 12).getDependentMoves().indexOf(bishopMove) != -1);
	}
}
