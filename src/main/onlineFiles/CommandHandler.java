package main.onlineFiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

import org.json.JSONObject;

/**
 * @author Solaman
 * handles control flow of Commands to be sent to and from the boardPanel
 */
public class CommandHandler {
		
		BoardPanel panel;
		
		private Semaphore responseReadSemaphore;
		private Semaphore responseWriteSemaphore;
		private Semaphore requestSemaphore;
		private List<JSONObject> responseCommands;
		private List<JSONObject> requestCommands;
		
		private int player;
		
		public CommandHandler(int player, BoardPanel panel) throws IOException { 
			this.panel= panel;
		    responseReadSemaphore= new Semaphore(0);
		    responseWriteSemaphore= new Semaphore(1);
		    requestSemaphore= new Semaphore(1);
		    responseCommands= new ArrayList<JSONObject>();
		    requestCommands= new ArrayList<JSONObject>();

		}
		
		/**
		 * add a JSONObject describing a command from the responseCommand list, blocks
		 * if too many commands have been added (4)
		 * @param toAdd -JSONObject of command to add
		 */
		public void addRequestCommand(JSONObject toAdd){
			try {
				responseWriteSemaphore.acquire();
				responseCommands.add( toAdd);
				responseReadSemaphore.release();
				responseWriteSemaphore.release();
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
		private JSONObject removeRequestCommand(){
			JSONObject toReturn=null;
			try {
				responseReadSemaphore.acquire();
				toReturn = responseCommands.remove(0);
				responseWriteSemaphore.release();
				responseReadSemaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return toReturn;
		}
		
		public void doRequestCommand(JSONObject toDo){
			try {
				requestSemaphore.acquire();
				doCommand(toDo);
				requestSemaphore.release();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void doResponseCommand(){
			doCommand( removeRequestCommand() );
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
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
}