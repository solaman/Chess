package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;
import main.ChessSpace;
import main.CommandFiles.CommandSequence;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.pawnMoves.PawnEnPassantMcCooeys;
import main.chessMoves.pawnMoves.PawnFirstMove;
import main.chessPieces.Pawn;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;

public class PawnEnPassantMcCooeysTest {

	HexagonalBoard board;
	Pawn pawn;
	PawnEnPassantMcCooeys pepMC;
	Pawn pawnMisunderstood;

	@Before
	public void setUp() throws Exception {
		board= new HexagonalBoard( 5, 5);
		pawn= new Pawn(0);
		pawnMisunderstood= new Pawn(1);
		pepMC= new PawnEnPassantMcCooeys(pawn);
	}

	/**
	 * test if move makes correct CommandSequencesAvailable
	 */
	@Test
	public void testFirstMoveAbove() {
		board.placePiece(pawn, 2, 2);
		board.placePiece(pawnMisunderstood, 3, 1);
		ChessSpace space= board.getChessSpace(3,1);
		CommandSequence dummy= new CommandSequence( pawnMisunderstood, new PawnFirstMove(pawnMisunderstood), space, space, null);
		board.getCommandHistory().add(dummy);
		pepMC.buildMoveData(board);
		assertTrue( FindSpace.doIt(pepMC.getCommandSequences(board), space ) );
		
		space= board.getChessSpace(3, 3);
		board.placePiece(pawnMisunderstood, space);
		pepMC.clearMoveData(board);
		pepMC.buildMoveData(board);
		assertTrue( FindSpace.doIt(pepMC.getCommandSequences(board), space ) );
		
		
	}
	

}
