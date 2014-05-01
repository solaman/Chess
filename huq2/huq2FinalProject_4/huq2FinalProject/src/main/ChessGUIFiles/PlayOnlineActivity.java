package main.ChessGUIFiles;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import main.ChessBoard;
import main.ChessGame;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.ChessMenu.StartGameAction;
import main.ChessGUIFiles.MouseAdapters.OnlinePlayMouseAdapter;
import main.ChessGUIFiles.MouseAdapters.PlayMouseAdapter;
import main.chessGames.CustomGame;
import main.onlineFiles.CommandClient;
import main.onlineFiles.CommandHandler;
import main.onlineFiles.CommandServer;

public class PlayOnlineActivity extends JPanel {

	JFrame frame;
	CommandServer toServe;
	CommandClient toCli;
	
	public PlayOnlineActivity(JFrame frame) {
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

	public class StartOnlineGameAction implements ActionListener{
		
		JFrame frame;
		ChessGame game;
		CommandHandler commandhandler;
		
		public StartOnlineGameAction(JFrame frame, ChessGame game, CommandHandler commandHandler) {
			this.frame= frame;
			this.game= game;
			this.commandhandler= commandHandler;
			
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			commandhandler.chooseGame(this);
		}
		
		public BoardPanel startGame(){
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
