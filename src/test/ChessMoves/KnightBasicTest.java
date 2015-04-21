package test.ChessMoves;

import static org.junit.Assert.*;

import java.util.List;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.movePatterns.KnightMovePattern;
import main.pieces.ChessPiece;

import org.junit.Before;
import org.junit.Test;

public class KnightBasicTest {

	ChessBoard board;
	ChessPiece knight;
	KnightMovePattern knightMove;
	
	ChessSpace current;
	List<ChessSpace> toMove;
	
	@Before
	public void setUp() throws Exception {
		board=new ChessBoard( 5,5);
		current=board.getChessSpace(2,2);
		knight=new ChessPiece(1);
		
		current.setOccupant(knight);	
		
		knightMove=new KnightMovePattern(knight);
		knight.setPosition(board.getChessSpace(2,2));
		knightMove.buildMoveData(board);
	}

	/**
	 * functionality of the abstract move class has been tested in TestMoveWithBishop,
	 * only check to see if the correct spaces are affected, as KingBasic uses
	 * the ChessMove functions almost exclusively
	 */
	@Test
	public void testUpdatePathData() {
		assertTrue(board.getChessSpace(4,3).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(4,1).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(0,3).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(0,1).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(1,0).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(3,0).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(4,3).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue(board.getChessSpace(4,1).getDependentMoves().indexOf(knightMove) != -1);
		assertFalse(board.getChessSpace(2,2).getDependentMoves().indexOf(knightMove) != -1);
	}
	
	@Test
	public void testBuildMoveDataHexagonal() throws Exception{
		HexagonalBoard hBoard= new HexagonalBoard(9,10);
		hBoard.placePiece(knight, hBoard.getChessSpace(5, 7));
		knightMove.clearMoveData(board);
		knightMove.buildMoveData(hBoard);
		assertTrue(knightMove.getCommandSequences(hBoard).size() == 12);
		assertTrue( hBoard.getChessSpace(6,12).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(7, 11).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(8, 8).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(8, 6).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(6, 2).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(7, 3).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(4, 2).getDependentMoves().indexOf(knightMove) != -1);
		
		assertTrue( hBoard.getChessSpace(3,3).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(2, 6).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(2, 8).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(4, 12).getDependentMoves().indexOf(knightMove) != -1);
		assertTrue( hBoard.getChessSpace(3, 11).getDependentMoves().indexOf(knightMove) != -1);
	}
}

