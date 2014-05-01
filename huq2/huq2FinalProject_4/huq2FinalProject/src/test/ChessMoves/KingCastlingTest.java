package test.ChessMoves;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import main.ChessBoard;
import main.ChessGame;
import main.ChessPiece;
import main.ChessSpace;
import main.CommandFiles.CommandSequence;
import main.chessBoards.HexagonalBoard;
import main.chessMoves.KingCastling;
import main.chessMoves.RookBasic;
import main.chessPieces.Rook;

import org.junit.Before;
import org.junit.Test;

public class KingCastlingTest {

	ChessBoard board;
	Rook rook1;
	Rook rook2;
	ChessPiece king;
	ChessGame game;
	KingCastling kingMove;
	List<ChessPiece> p1Pieces;
	
	@Before
	public void setUp() throws Exception{
		board = new ChessBoard( 8,8);
		rook1 = new Rook(0);
		rook2 = new Rook(0);
		king= new ChessPiece(0);
		kingMove=new KingCastling(king);
		
		int[] s={4,0};
		ChessSpace shuffle=board.getChessSpace(s);
		board.placePiece(king, shuffle);
		
		shuffle=board.getChessSpace(7,0);
		shuffle.setOccupant(rook1);
		rook1.setPosition(shuffle);
		
		shuffle=board.getChessSpace(0,0);
		shuffle.setOccupant(rook2);
		rook2.setPosition(shuffle);
		
		p1Pieces= new ArrayList<ChessPiece>();
		p1Pieces.add(king);
		p1Pieces.add(rook2);
		p1Pieces.add(rook1);
		board.pieces.add(p1Pieces);
		board.pieces.add(new ArrayList<ChessPiece>());
		
		kingMove.buildMoveData(board);
	}
	
	/**
	 * asserts that all space dependencies are made and all liftPlaceSequences are made when
	 * Castling on both rooks is possible
	 */
	@Test
	public void testBuildPathData(){
		assertTrue(board.getChessSpace(4,0).getDependentMoves().indexOf(kingMove) == -1);
		assertTrue(board.getChessSpace(2,0).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(1,0).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(2,0).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(6,0).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(5,0).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(7,0).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(FindSpace.doIt(kingMove.getCommandSequences(board), board.getChessSpace(6,0) ));
		assertTrue(FindSpace.doIt(kingMove.getCommandSequences(board), board.getChessSpace(2,0)  ));
	}
	
	/**
	 * asserts that liftPlaceSequences are proper when castling for a different player
	 * other than player 0
	 */
	@Test
	public void testBuildPathDataOtherTeam(){
		ChessSpace shuffle=board.getChessSpace(4,7);
		king.setPosition(shuffle);
		shuffle.setOccupant(king);
		shuffle=board.getChessSpace(7,7);
		shuffle.setOccupant(rook1);
		rook1.setPosition(shuffle);
		shuffle=board.getChessSpace(0,7);
		shuffle.setOccupant(rook2);
		rook2.setPosition(shuffle);
		kingMove.clearMoveData(board);
		kingMove.buildMoveData(board);
		assertTrue(board.getChessSpace(1,7).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(2,7).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(6,7).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(5,7).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(board.getChessSpace(7,7).getDependentMoves().indexOf(kingMove) != -1);
		assertTrue(FindSpace.doIt(kingMove.getCommandSequences(board), board.getChessSpace(6,7)) );
		assertTrue(FindSpace.doIt(kingMove.getCommandSequences(board), board.getChessSpace(2,7)) );
	}
	
	/**
	 * asserts that no liftPlaceSequence is available if the king is in check
	 */
	@Test
	public void testBuildPathDataInCheck(){
		ChessPiece evilGhost=new ChessPiece(1);
		RookBasic soEvil=new RookBasic(evilGhost);
		board.getChessSpace(4,0).addDependentMove(soEvil);
		assertFalse(FindSpace.doIt(kingMove.getCommandSequences(board), board.getChessSpace(4,0) ));
	}
	
	/**
	 * asserts that the move can threaten no-one
	 */
	@Test
	public void testCanThreat(){
		ChessPiece evilGhost=new ChessPiece(1);
		assertFalse( kingMove.canThreaten(evilGhost));
	}
	
	/**
	 * test if proper commandSequences are made for hexagonal Chess
	 * @throws Exception 
	 */
	@Test
	public void testCommandSequencesHexagonalBoard() throws Exception{
		HexagonalBoard hBoard= new HexagonalBoard(9, 10);
		rook2.setPosition(null);
		hBoard.pieces.add(p1Pieces);
		hBoard.placePiece(king, hBoard.getChessSpace(4, 0));
		hBoard.placePiece(rook1, hBoard.getChessSpace(0, 4));
		kingMove.clearMoveData(board);
		kingMove.buildMoveData(hBoard);
		List<CommandSequence> commList= kingMove.getCommandSequences(hBoard);
		assertTrue( commList.size() == 2);
		assertTrue( FindSpace.doIt(commList, hBoard.getChessSpace(2, 2)));
		assertTrue( FindSpace.doIt(commList, hBoard.getChessSpace(1, 3)));
	}
	
}
