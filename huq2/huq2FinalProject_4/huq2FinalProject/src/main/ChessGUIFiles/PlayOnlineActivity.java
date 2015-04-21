package main.ChessGUIFiles;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.*;

import main.ChessBoard;
import main.ChessGame;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.MouseAdapters.OnlinePlayMouseAdapter;
import main.chessGames.CustomGame;
import main.onlineFiles.CommandClient;
import main.onlineFiles.CommandHandler;
import main.onlineFiles.CommandServer;

/**
 * @author Solaman
 * "activity" used to start a chess game online. used mostly by the CommandServer
 */
@SuppressWarnings("serial")
public class PlayOnlineActivity extends JPanel {

	/**
	 * frame that the boardPanel is built in
	 */
	JFrame frame;
	
	/**
	 * Server to build if no one has hooked up the local port
	 */
	CommandServer toServe;
	
	/**
	 * Client to build if someone has already hooked up to the local port
	 */
	CommandClient toCli;
	public static boolean created=false;
	
	/**
	 * does nothing if it has already been instantiated, tries to build a CommandServer if
	 * no-one hooked up to the local port used for online play, else
	 * starts a CommandClient
	 * @param frame frame to build the game inside
	 */
	public PlayOnlineActivity(JFrame frame) {
		if(created)
			return;
		this.frame= frame;
		frame.getContentPane().removeAll();
		try {
			toServe= new CommandServer();
			buildOnlineGameMenu( toServe.commandHandler );
		} catch (IOException e) {
			try {
				toCli= new CommandClient( frame);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * if the CommandServer is started, the player with the CommandServer gets to
	 * choose what game to play, this displays the games they get to start
	 * @param handler
	 */
	private void buildOnlineGameMenu(CommandHandler handler){
		setLayout(new GridLayout(7,0));
		List<ChessGame> gamesToPlay= ChessMenu.getGamesToPlay();
		
		for( ChessGame game : gamesToPlay){
    		JButton gameButton= new JButton(game.getName());
    		gameButton.addActionListener( new StartOnlineGameAction(frame, game, handler) );
    		add(gameButton);
    	}
		frame.getContentPane().add( this, BorderLayout.WEST);
        frame.pack();
		
	}

	/**
	 * @author Solaman
	 * action associated with the game options, if the game is selected
	 * then start the game and send a packet to the client, stalls if no client is available
	 */
	public class StartOnlineGameAction implements ActionListener{
		
		JFrame frame;
		ChessGame game;
		CommandHandler commandhandler;
		
		/**
		 * @param frame -frame the game will be built in
		 * @param game -game the StartOnlineGameAction is associated with
		 * @param commandHandler commandHandler the CommandClient/CommandServer will use
		 * to manage command passing
		 */
		public StartOnlineGameAction(JFrame frame, ChessGame game, CommandHandler commandHandler) {
			this.frame= frame;
			this.game= game;
			this.commandhandler= commandHandler;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			commandhandler.chooseGame(this);
		}
		
		/**
		 * build the game once a request is sent and
		 * a game is selected
		 * @return panel that the game is started in
		 */
		public BoardPanel startGame(){
			created=true;
			ChessBoard board= game.setUp();
			BoardPanel panel= board.getRepresentation();
			new OnlinePlayMouseAdapter( panel, 1, commandhandler);
			panel.player=1;
			JSONObject gameJSON= createGameJSON();
			commandhandler.addMainCommand(gameJSON);
			frame.getContentPane().removeAll();
			frame.getContentPane().add( panel, BorderLayout.CENTER);
	        frame.setSize( panel.getPreferredSize());
	        frame.pack();
	        
	        return panel;
		}
		
		/**
		 * create JSON command to send to the client to start
		 * the game the CommandServer started
		 * @return JSONObject describing command
		 */
		private JSONObject createGameJSON(){
			JSONObject gameJSON= new JSONObject();
			try {
				gameJSON.put("type", "game started");
				JSONObject content= new JSONObject();
				content.put("class", game.getName());
				if( game instanceof CustomGame)
					content.put("bluePrint", ((CustomGame)game).bluePrint);
				gameJSON.put("content", content);
				return gameJSON;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	

}
