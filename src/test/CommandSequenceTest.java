package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.games.StandardChess;
import main.moveHistory.MoveSequence;
import main.movePatterns.RookMovePattern;
import main.pieces.Pawn;
import main.pieces.Rook;

import org.json.*;
import org.junit.Before;
import org.junit.Test;

public class CommandSequenceTest {
	
	ChessBoard board;
	Rook rook;
	Pawn  pawn;

	@Before
	public void setUp() throws Exception {
		board= new ChessBoard(2, 4);
		rook= new Rook(new StandardChess(), 0);
		pawn= new Pawn(1);
		board.placePiece(rook, 0, 0);
		board.placePiece(pawn, 0, 3);
	}

	@Test
	public void testJSONConstructor() throws JSONException, IOException, ClassNotFoundException {
		String fileString= readFile("resources/TestResources/commandSequenceTest.json", StandardCharsets.UTF_8);
		JSONObject commandObject = new JSONObject(fileString);
		MoveSequence toTest= new MoveSequence(board, commandObject);
		assertTrue( toTest.getPerformingPiece()== rook);
		assertTrue(toTest.getMovePerformed() instanceof RookMovePattern);
		ChessSpace target= toTest.getTargetSpace();
		assertTrue("target is "+ target.getXCoord()+","+target.getYCoord()+" should be 0,3",
				toTest.getTargetSpace() == board.getChessSpace(0,3));
		toTest.doCommands(board);
		assertTrue(pawn.getXCoord()+ ", "+ pawn.getYCoord(),pawn.getPosition()== null);
		assertTrue(rook.getPosition()== board.getChessSpace(0,3));
	}
	
	@Test
	public void testAsJSON() throws JSONException{
		board.initializeMoves();
		MoveSequence comm=null;
		for( MoveSequence command: rook.getMoves().get(0).getCommandSequences(board))
			if( command.getTargetSpace() == board.getChessSpace(0, 3)){
				comm= command;
				break;
			}
		JSONObject commObj= comm.asJSON(board);
		assertTrue( commObj.getString("move performed")== "main.chessMoves.RookBasic");
		
		JSONArray commArr= commObj.getJSONArray("performing piece");
		assertTrue(commArr.getInt(0)== 0 && commArr.getInt(1)==0);
		
		commArr= commObj.getJSONArray("target space");
		assertTrue(commArr.getInt(0)== 0 && commArr.getInt(1)==3);
		
		commArr= commObj.getJSONArray("lift sequence");
		JSONArray coords= commArr.getJSONArray(0);
		assertTrue(coords.getInt(0)==0 && coords.getInt(1)==3);
		coords= commArr.getJSONArray(1);
		assertTrue(coords.getInt(0)+ ", " + coords.getInt(1), coords.getInt(0)==0 && coords.getInt(1)==0);
		
		commArr= commObj.getJSONArray("place sequence");
		coords= commArr.getJSONArray(0);
		assertTrue(coords.getInt(0)==-1 && coords.getInt(1)== -1);
		coords= commArr.getJSONArray(1);
		assertTrue(coords.getInt(0)== 0 && coords.getInt(1)== 3);
		
	}
	

	/**
	 * helper function reads files
	 * @param path -path to file
	 * @param encoding -encoding to use
	 * @return contents of file as String
	 * @throws IOException -could not find file specified
	 */
	static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	


}
