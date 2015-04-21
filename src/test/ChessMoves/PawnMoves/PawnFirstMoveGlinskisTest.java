package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import main.boards.ChessSpace;
import main.boards.HexagonalBoard;
import main.moveHistory.MoveSequence;
import main.movePatterns.pawnMovePatterns.PawnBasic;
import main.movePatterns.pawnMovePatterns.PawnCapture;
import main.movePatterns.pawnMoves.firstMovePatterns.PawnFirstMoveGlinskis;
import main.pieces.ChessPiece;
import main.pieces.Pawn;

import org.junit.Before;
import org.junit.Test;

import test.ChessMoves.FindSpace;

public class PawnFirstMoveGlinskisTest {

	HexagonalBoard board;
	Pawn pawn;
	PawnFirstMoveGlinskis pfmG;
	Pawn pawnFriend;
	
	List<ChessPiece> pieces= new ArrayList<ChessPiece>();
	
	@Before
	public void setUp() throws Exception {
		board= new HexagonalBoard( 5, 5);
		pawn= new Pawn(0);
		pawnFriend= new Pawn(0);
		List<ChessPiece> pieces= new ArrayList<ChessPiece>();
		pieces.add(pawn);
		pieces.add(pawnFriend);
		board.pieces.add(pieces);
		pfmG= new PawnFirstMoveGlinskis(pawn);
	}

	/**
	 * test if CommandSequence is available when piece hasn't moved
	 */
	@Test
	public void testHasntMove() {
		ChessSpace pawnSpace= board.getChessSpace(2,0);
		board.placePiece(pawn, pawnSpace);
		pfmG.initializeMoveData( board);
		List<MoveSequence > comm= pfmG.getCommandSequences(board);
		assertTrue( comm.size()== 1);
		assertTrue( FindSpace.doIt( pfmG.getCommandSequences(board), board.getChessSpace(2, 4)));
	}
	
	/**
	 * test if commandSequence is avialable after a capture onto
	 * a space initially occupied by a pawn.
	 */
	@Test
	public void afterCapture(){
		ChessSpace pawnSpace= board.getChessSpace(2,0);
		ChessSpace friendSpace= board.getChessSpace(3,1);
		board.placePiece(pawn, pawnSpace);
		board.placePiece(pawnFriend, friendSpace);
		pfmG.initializeMoveData( board);
		
		board.placePiece(pawn, friendSpace);
		MoveSequence capture= new MoveSequence(pawn, new PawnCapture(pawn), pawnSpace, pawnSpace, null);
		board.getCommandHistory().add(capture);
		
		pfmG.clearMoveData(board);
		pfmG.buildMoveData(board);
		
		List<MoveSequence > comm= pfmG.getCommandSequences(board);
		assertTrue( comm.size()== 1);
		assertTrue( FindSpace.doIt( pfmG.getCommandSequences(board), board.getChessSpace(3, 5)));
		
		MoveSequence notCapture= new MoveSequence(pawn, new PawnBasic(pawn), pawnSpace, pawnSpace, null);
		board.getCommandHistory().add(notCapture);
		pfmG.clearMoveData(board);
		pfmG.buildMoveData(board);
		comm= pfmG.getCommandSequences(board);
		assertTrue( comm.size() == 0);
	}

}
