package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;

import java.util.List;

import main.boards.HexagonalBoard;
import main.moveHistory.MoveSequence;
import main.movePatterns.pawnMoves.firstMovePatterns.PawnFirstMoveShafrans;
import main.pieces.Pawn;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;

public class PawnFirstMoveShafransTest {

	HexagonalBoard board;
	Pawn pawn;
	PawnFirstMoveShafrans pfmS;
	
	@Before
	public void setUp() throws Exception {
		board= new HexagonalBoard( 9, 10);
		pawn= new Pawn(0);
		pfmS= new PawnFirstMoveShafrans(pawn);
	}

	@Test
	public void test() {
		board.placePiece(pawn, 4, 2);
		pfmS.buildMoveData(board);
		List<MoveSequence> commList= pfmS.getCommandSequences(board);
		assertTrue(commList.size() == 2);
		assertTrue(FindSpace.doIt(commList, board.getChessSpace(4,6)));
		assertTrue(FindSpace.doIt(commList, board.getChessSpace(4,8)));
		
		board.placePiece(pawn, 5, 3);
		pfmS.clearMoveData(board);
		pfmS.buildMoveData(board);
		commList= pfmS.getCommandSequences(board);
		assertTrue(commList.size() == 2);
		assertTrue(FindSpace.doIt(commList, board.getChessSpace(5,7)));
		assertTrue(FindSpace.doIt(commList, board.getChessSpace(5,9)));
		
		board.placePiece(pawn, 6, 4);
		pfmS.clearMoveData(board);
		pfmS.buildMoveData(board);
		commList= pfmS.getCommandSequences(board);
		assertTrue(commList.size() == 1);
		assertTrue(FindSpace.doIt(commList, board.getChessSpace(6,8)));
	}

}
