package main.onlineFiles;

import java.awt.BorderLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.JFrame;

import org.json.JSONException;
import org.json.JSONObject;

import main.boards.ChessBoard;
import main.chessGUI.PlayOnlineActivity;
import main.chessGUI.PlayOnlineActivity.StartOnlineGameAction;
import main.chessGUI.boardPanels.BoardPanel;
import main.chessGUI.mouseAdapters.OnlinePlayMouseAdapter;
import main.games.CustomGame;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;

/**
 * @author Solaman
 * handles control flow of Commands to be sent to and from the boardPanel
 */
public class CommandHandler {
		
		BoardPanel panel;
		public JFrame frame;
		
		private Semaphore mainReadSemaphore;
		private Semaphore mainWriteSemaphore;
		private Semaphore subSemaphore;
		private List<JSONObject> mainCommands;
		
		private Semaphore gamePutSemaphore;
		private Semaphore gameGetSemaphore;
		private StartOnlineGameAction gameAction;
		
		public CommandHandler() throws IOException { 
		    mainReadSemaphore= new Semaphore(0);
		    mainWriteSemaphore= new Semaphore(1);
		    subSemaphore= new Semaphore(1);
		    gamePutSemaphore= new Semaphore(1);
		    gameGetSemaphore= new Semaphore(0);
		    mainCommands= new ArrayList<JSONObject>();
		    new ArrayList<JSONObject>();

		}
		
		public void setFrame(JFrame frame){
			this.frame= frame;
		}
		
		public void chooseGame(StartOnlineGameAction action){
			try {
				gamePutSemaphore.acquire();
				gameAction= action;
				gamePutSemaphore.release();
				gameGetSemaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		/**
		 * add a JSONObject describing a command from the responseCommand list, blocks
		 * if too many commands have been added (4)
		 * @param toAdd -JSONObject of command to add
		 */
		public void addMainCommand(JSONObject toAdd){
			try {
				mainWriteSemaphore.acquire();
				mainCommands.add( toAdd);
				mainReadSemaphore.release();
				//mainWriteSemaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		/**
		 * remove a JSONObject describing a command from the responseCommand list, blocks
		 * if no command is available
		 * @return JSONObject of command
		 */
		JSONObject removeMainCommand(){
			JSONObject toReturn=null;
			try {
				mainReadSemaphore.acquire();
				toReturn = mainCommands.remove(0);
				mainWriteSemaphore.release();
				//mainReadSemaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return toReturn;
		}
		
		public void doSubCommand(JSONObject toDo){
			try {
				subSemaphore.acquire();
				doCommand(toDo);
				subSemaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/**
		 * translate the given JSONObject to a command and then perform it on the panel.
		 * @param toDo -JSONObject to interpret into a command
		 * @return false if the command type was not understood, or if the command was unsuccessful.
		 */
		private void doCommand(JSONObject toDo){
			try {
				if(toDo.getString("type").equals("command sequence")){
					panel.board.performMovePermanent(toDo.getJSONObject("content"));
				panel.repaint();
				} else if( toDo.getString("type").equals("start the game!")){
					gameGetSemaphore.acquire();
					gamePutSemaphore.acquire();
					panel= gameAction.startGame();
				} else if( toDo.getString("type").equals("game started")){
					panel= startGame( toDo);
				}
				else System.out.println("wtf");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * mainly used by a CommandClient, starts the game described as a JSON
		 * @param gameJSON -JSONObject describing game
		 * @return board panel that contains the ChessBoard view and references ChessBoard model
		 */
		private BoardPanel startGame(JSONObject gameJSON){
			ChessBoard board;
			try {
				JSONObject content= gameJSON.getJSONObject("content");
				String className= content.getString("class");
				if( className.equals(StandardChess.NAME))
					board= new StandardChess().setUp();
				else if( className.equals(GlinskisChess.NAME) )
					board= new GlinskisChess().setUp();
				else if( className.equals(ShafransChess.NAME))
					board= new ShafransChess().setUp();
				else
					board= new CustomGame( content.getJSONObject("bluePrint")).setUp();
				BoardPanel panel= board.getRepresentation();
				panel.player= 0;
				new OnlinePlayMouseAdapter( panel, 0, this);
				PlayOnlineActivity.created=true;
				
				frame.getContentPane().removeAll();
				frame.getContentPane().add( panel, BorderLayout.CENTER);
		        frame.setSize( panel.getPreferredSize());
		        frame.pack();
		        return panel;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
}