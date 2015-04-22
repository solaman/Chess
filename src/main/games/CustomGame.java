package main.games;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.json.*;

import main.boards.ChessBoard;
import main.boards.HexagonalBoard;
import main.movePatterns.MovePattern;
import main.pieces.ChessPiece;

/**
 * @author Solaman
 * Class used to build and load Customizable Games
 */
public class CustomGame extends ChessGame {
	
	/**
	 * name of game
	 */
	String name;
	
	/**
	 * used to designate how the board is made,
	 * this should be easily grabbed from a file
	 */
	public JSONObject bluePrint;
	
	/**
	 * pieces for player 0
	 */
	List<ChessPiece> player0Pieces;
	
	
	/**
	 * pieces for player 1
	 */
	List<ChessPiece> player1Pieces;
	
	@SuppressWarnings("rawtypes")
	public CustomGame(){
		try {
			loadFile("src/resources/TestResources/EmptyGame.json");
		} catch (IOException | JSONException e) {
			System.err.print("Skeleton \"EmptyGame.json\" could not be found.\n");
		}
	}
	
	public CustomGame(JSONObject bluePrint){
		this.bluePrint= bluePrint;
		try {
			this.name= bluePrint.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * builds relevant data to perform the setUp of a game saved to the given file
	 * @param filePath -path to game file
	 * @throws IOException -filePath not found, error while reading stream
	 * @throws JSONException -content of file could not be read as a JSONObject
	 */
	public void loadFile(String filePath) throws IOException, JSONException{
		String fileString= readFile(filePath, StandardCharsets.UTF_8);
		bluePrint = new JSONObject(fileString);
		
		this.name= bluePrint.getString("name");
	}
	
	
	/**
	 * saves file in the format required to be able to load the file
	 * @throws FileNotFoundException Pathname is erroneous (should only occur if resources/CustomGames is deleted)
	 * @throws UnsupportedEncodingException -should only occur if somehow UTF-8 stops being supported miraculously
	 * @throws JSONException -error occurred while converting the bluePrint JSONObject to a json formatted string
	 */
	public void saveFile() throws FileNotFoundException, UnsupportedEncodingException, JSONException{
		PrintWriter writer = new PrintWriter("src/resources/CustomGames/"+name+".json", "UTF-8");
		String jString= bluePrint.toString(3);
		writer.print(jString);
		writer.close();
	}
	
	public void addLoadOut(Class pieceClass, List<String> moveClasses){
		try {
			JSONArray loadOut= new JSONArray();
			for(String moveClass : moveClasses){
				JSONObject moveObject= new JSONObject();
				moveObject.put("moveName", moveClass );
				loadOut.put(moveObject);
			}
			bluePrint.getJSONObject("pieceTypes").put(pieceClass.getName(), loadOut);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * either adds or replaces a loadOut for a given class.
	 * Does not assign if pieceClass is not a ChessPiece class and
	 * moveClasses are not a ChessMove class
	 * @param pieceClass -class that gets the load out
	 * @param moveClasses -move classes involved in load out
	 */
	public void addLoadOut(Class pieceClass, Class... moveClasses){
		List<String> moves= new ArrayList<String>();
		if(!ChessPiece.class.isAssignableFrom(pieceClass))
			return;
		for(int moveIndex=0; moveIndex< moveClasses.length ; moveIndex++)
			if( MovePattern.class.isAssignableFrom( moveClasses[moveIndex]))
				moves.add(moveClasses[moveIndex].getName());
		addLoadOut(pieceClass, moves);
	}
	
	/**
	 * removes the piece to be built at the given coordinates
	 * @param xCoord -x coordinate to check
	 * @param yCoord -y coordinate to check
	 */
	public void removePieceAt(int xCoord, int yCoord){
		try {
			JSONArray pieces= bluePrint.getJSONArray("pieces");
			for(int pieceIndex=0; pieceIndex< pieces.length(); pieceIndex++){
				JSONObject piece= pieces.getJSONObject(pieceIndex);
				if( piece.getInt("xCoord")== xCoord && piece.getInt("yCoord")== yCoord){
					pieces.remove(pieceIndex);
					return;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addBoard(ChessBoard board){
		JSONObject boardObject= new JSONObject();
		try{
			boardObject.put("className", board.getClass().getName());
			boardObject.put("xLength", board.getXLength());
			if(board instanceof HexagonalBoard)
				boardObject.put("yLength", board.getYLength()/2);
			else
				boardObject.put("yLength", board.getYLength());
			bluePrint.put("board", boardObject);
		} catch (JSONException e){
			
		}
	}
	
	public void addPieceAt( ChessPiece piece, int xCoord, int yCoord){
		addPieceAt(piece.getClass(), piece.getPlayer(), xCoord, yCoord);
	}
	
	/**
	 * add the piece to be built at the given coordinate
	 * @param pieceClass -class of piece to build
	 * @param player - player owner of the piece
	 * @param xCoord -x coordinate to place piece at
	 * @param yCoord -y coordinate to place piece at
	 */
	public void addPieceAt(Class pieceClass, int player, int xCoord, int yCoord){
		JSONObject toAdd= new JSONObject();
		try {
			toAdd.put("className", String.valueOf(pieceClass).substring(6));
			toAdd.put("player", player);
			toAdd.put("xCoord", xCoord);
			toAdd.put("yCoord", yCoord);
			
			bluePrint.getJSONArray("pieces").put(toAdd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param name -name to change game name to
	 */
	public void changeName(String name){
		this.name= name;
		try {
			bluePrint.put("name", name);
		} catch (JSONException e) {}
	}

	@Override
	public ChessBoard setUp() {
		ChessBoard board= buildBoard();
		setPieces(board);
		return board;
	}

	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * build the board from the parameters found in the blueprint JSONObject as well as
	 * the pieceLoadOut hashtable
	 * @return board that was constructed
	 */
	private ChessBoard buildBoard() {
		
		JSONObject boardObject=null;
		String boardClassName=null;
		try {
			boardObject = bluePrint.getJSONObject("board");
			boardClassName = boardObject.getString("className");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Class boardClass= null;
		try {
			boardClass = Class.forName(boardClassName);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Constructor ctor;
		ChessBoard board=null;
		try {
			ctor = boardClass.getDeclaredConstructor(int.class, int.class);
			try {
				try {
					board= (ChessBoard) ctor.newInstance(boardObject.getInt("xLength"), boardObject.getInt("yLength"));
				} catch (InstantiationException | IllegalAccessException
						| JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return board;
		
	}
	
	/**
	 * builds the pieces with correct loadout, player, and puts them at the correct coordinates
	 * before then initializing all of their moves
	 * @param board to place pieces on
	 */
	private void setPieces(ChessBoard board){
		List<ChessPiece> p0Pieces= new ArrayList<ChessPiece>();
		List<ChessPiece> p1Pieces= new ArrayList<ChessPiece>();
		JSONArray piecesToSet=null;
		try {
			piecesToSet = bluePrint.getJSONArray("pieces");
			
			for(int pieceIndex=0; pieceIndex< piecesToSet.length(); pieceIndex++){
				JSONObject pieceObject = piecesToSet.getJSONObject(pieceIndex);
				Class className= Class.forName( pieceObject.getString("className"));
				
				Constructor ctor= className.getDeclaredConstructor( int.class);
				ChessPiece pieceToSet= (ChessPiece) ctor.newInstance( pieceObject.getInt("player"));
				
				JSONArray loadOut= bluePrint.getJSONObject("pieceTypes").getJSONArray(className.getName());
				for( int i=0; i< loadOut.length(); i++){
					Class moveClass= Class.forName( loadOut.getJSONObject(i).getString("moveName"));
					ctor= moveClass.getDeclaredConstructor( ChessPiece.class);
					ctor.newInstance( pieceToSet);
				}
				
				board.placePiece( pieceToSet, pieceObject.getInt("xCoord"), pieceObject.getInt("yCoord"));
				
				if( pieceObject.getInt("player") == 0)
					p0Pieces.add( pieceToSet);
				else
					p1Pieces.add( pieceToSet);
			}
			board.pieces.add(p0Pieces);
			board.pieces.add(p1Pieces);
			
			board.initializeMoves();
			
		} catch (JSONException| ClassNotFoundException | NoSuchMethodException |InvocationTargetException |
				 IllegalAccessException |  InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
