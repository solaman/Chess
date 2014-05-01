package test.ChessMoves.PawnMoves;



import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.chessBoards.HexagonalBoard;
import main.chessMoves.pawnMoves.PawnCaptureMcCooeys;
import main.chessPieces.Pawn;

public class PawnCaptureMcCooeysTest {

	Pawn pawn;
	HexagonalBoard board;
	PawnCaptureMcCooeys capture;

	@Before
	public void setUp() throws Exception {
		board= new HexagonalBoard(3,3);
		pawn= new Pawn(0);
		capture= new PawnCaptureMcCooeys(pawn);
	}
	
	@Test
	public void test(){
		board.getChessSpace(1,0).setOccupant(pawn);
		pawn.setPosition( board.getChessSpace(1,0));
		capture.buildMoveData(board);
		assertTrue(board.getChessSpace(0,3).getDependentMoves().indexOf(capture) != -1);
	}

}
