package main.games;

import org.json.*;

import main.boards.ChessBoard;

public class SavedGame extends ChessGame {

	String name;
	JSONObject bluePrint;
	
	public SavedGame() {
		name= null;
		bluePrint=null;
	}

	@Override
	public ChessBoard setUp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

}
