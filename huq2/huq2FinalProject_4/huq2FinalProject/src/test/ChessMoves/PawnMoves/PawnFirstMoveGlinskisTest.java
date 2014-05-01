package test.ChessMoves.PawnMoves;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import main.ChessPiece;
import main.ChessSpace;
import main.CommandFiles.CommandSequence;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.pawnMoves.PawnBasic;
import main.chessMoves.pawnMoves.PawnCapture;
import main.chessMoves.pawnMoves.firstMoves.PawnFirstMoveGlinskis;
import main.chessPieces.Pawn;

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
		List<CommandSequence > comm= pfmG.getCommandSequences(board);
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
		CommandSequence capture= new CommandSequence(pawn, new PawnCapture(pawn), pawnSpace, pawnSpace, null);
		board.getCommandHistory().add(capture);
		
		pfmG.clearMoveData(board);
		pfmG.buildMoveData(board);
		
		List<CommandSequence > comm= pfmG.getCommandSequences(board);
		assertTrue( comm.size()== 1);
		assertTrue( FindSpace.doIt( pfmG.getCommandSequences(board), board.getChessSpace(3, 5)));
		
		CommandSequence notCapture= new CommandSequence(pawn, new PawnBasic(pawn), pawnSpace, pawnSpace, null);
		board.getCommandHistory().add(notCapture);
		pfmG.clearMoveData(board);
		pfmG.buildMoveData(board);
		comm= pfmG.getCommandSequences(board);
		assertTrue( comm.size() == 0);
	}

}
