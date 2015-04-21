package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.boards.ChessBoard;
import main.games.CustomGame;
import main.movePatterns.MovePattern;
import main.movePatterns.RookMovePattern;
import main.movePatterns.pawnMovePatterns.PawnBasic;
import main.movePatterns.pawnMovePatterns.PawnFirstMove;
import main.pieces.ChessPiece;
import main.pieces.Pawn;
import main.pieces.Rook;

import org.junit.Before;
import org.junit.Test;

public class CustomGameTest {

	CustomGame game;
	ChessBoard board;
	
	@Before
	public void setUp() throws Exception {
		game= new CustomGame();
		game.loadFile("resources/TestResources/CustomGameTest.json");
		board= game.setUp();
	}
	
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}

	@Test
	public void testCorrectName() {
		assertTrue("result: "+game.getName()+" || expected: Test", game.getName().equals( "Test" ));
	}
	
	@Test
	public void testBoardCorrectSize(){
		assertTrue(board.getXLength()==3 && board.getYLength()==3);
	}
	
	@Test
	public void testCorrectPlacement(){
		ChessPiece piece= board.getOccupant(0,0);
		assertTrue(piece != null);
		assertTrue(piece.getPlayer() == 0);
		assertTrue(piece instanceof Pawn);
		
		piece= board.getOccupant(0,2);
		assertTrue(piece != null);
		assertTrue(piece.getPlayer() == 1);
		assertTrue(piece instanceof Pawn);
	}
	
	@Test
	public void testCorrectLoadOut(){
		for(ChessPiece piece : board.pieces.get(0) ){
			if(piece instanceof Pawn){
				boolean hasMoveType=false;
				for(MovePattern move: piece.getMoves())
					if(move instanceof PawnFirstMove)
						hasMoveType= true;
				assertTrue(hasMoveType);
					
				hasMoveType=false;
				for(MovePattern move: piece.getMoves())
					if(move instanceof PawnBasic)
						hasMoveType= true;
				assertTrue(hasMoveType);
			}
			else if(piece instanceof Rook){
				boolean hasMoveType= false;
				for(MovePattern move: piece.getMoves())
					if(move instanceof RookMovePattern)
						hasMoveType= true;
				assertTrue(hasMoveType);
			}
		}
	}
	
	/**
	 * asserts that after setUp is called, the moves are initialized
	 */
	@Test
	public void testInitializeData(){
		assertTrue( board.getChessSpace(0,1).getDependentMoves().size() == 4);
		
	}
	
	/**
	 * asserts that a load out can successfully be added to bluePrint
	 */
	@Test
	public void testAddLoadOut(){
		game.addLoadOut(Pawn.class);
		board= game.setUp();
		assertTrue(board.getOccupant(0,0).getMoves().size() == 0);
		
		game.addLoadOut(Pawn.class, PawnFirstMove.class);
		board= game.setUp();
		assertTrue(board.getOccupant(0,0).getMoves().size() == 1);
	}
	
	/**
	 *  asserts that a piece can successfully be added to bluePrint
	 */
	@Test
	public void testAddPieceAt(){
		game.addPieceAt( Pawn.class, 1,1,1);
		board= game.setUp();
		assertTrue(board.getOccupant(1,1) instanceof Pawn);
	}
	
	/**
	 * asserts that a piece can successfully be removed from bluePrint
	 */
	@Test
	public void testRemovePieceAt(){
		game.removePieceAt(0,0);
		board= game.setUp();
		assertTrue(board.getOccupant(0,0) == null);
	}
	
//	@Test
//	public void testSaveFile() throws JSONException, IOException{
//		game.saveFile();
//		String trueFile= readFile("resources/TestResources/CustomGameTest.json", StandardCharsets.UTF_8);
//		String toCompare= readFile("resources/CustomGames/Test.json", StandardCharsets.UTF_8);
//		assertTrue( trueFile.equals(toCompare));
//		
//	}
	
	

}
