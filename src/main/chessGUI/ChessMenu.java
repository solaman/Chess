package main.chessGUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.json.*;

import main.boards.ChessBoard;
import main.chessGUI.boardPanels.BoardPanel;
import main.chessGUI.mouseAdapters.PlayMouseAdapter;
import main.games.ChessGame;
import main.games.CustomGame;
import main.games.GlinskisChess;
import main.games.ShafransChess;
import main.games.StandardChess;

public class ChessMenu {
	static JMenuBar menuBar;
	static JFrame Thisframe;
	
	/**
     * menu for starting a new game
     * @param frame
     */
    public static void buildMenu( JFrame frame){
    	Thisframe= frame;
    	menuBar= new JMenuBar();
    	addStartGameMenu( frame, menuBar);
    	addCreateGameMenu(frame, menuBar);
    	addPlayOnlineMenu(frame, menuBar);
    	
    	frame.setJMenuBar(menuBar);
    	frame.pack();
    	
    }
    
    /**
     * build and add the PlayOnline menu for menu
     * @param frame -frame to build the menu in
     * @param menuBar2 -menubar to add the playOnline menu to
     */
    private static void addPlayOnlineMenu(JFrame frame, JMenuBar menuBar2) {
		JMenu playOnlineMenu= new JMenu("play online");
		JMenuItem play= new JMenuItem("play");
		play.addActionListener(new PlayOnlineAction(frame));
		playOnlineMenu.add(play);
		menuBar2.add(playOnlineMenu);
		
	}
    
    /**
     * @author Solaman
     * action listener to start the play online activity
     */
    private static class PlayOnlineAction implements ActionListener{
    	JFrame frame;
    	
    	public PlayOnlineAction(JFrame frame){
    		this.frame= frame;
    	}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new PlayOnlineActivity(frame);
			
		}
    }

	/**
	 * restart the menuBar, used mostly by CreateGameMenu to
	 * add the created game menu to the list of games to play
	 */
	public static void restartMenu(){
    	Thisframe.setJMenuBar(null);
    	buildMenu(Thisframe);
    }
    
    /**
     * create the menu for created a new game
     * @param frame -frame to build the createActivity in
     * @param menuBar -menuBar to add the menu to
     */
    private static void addCreateGameMenu(JFrame frame, JMenuBar menuBar){
    	JMenu createGameMenu= new JMenu("create new game");
    	JMenuItem create= new JMenuItem("create");
    	create.addActionListener(new CreateGameAction(frame));
    	createGameMenu.add(create);
    	menuBar.add( createGameMenu);
    }
    
    /**
     * @author Solaman
     *  action that starts the createGame Activity
     */
    private static class CreateGameAction implements ActionListener{

    	JFrame frame;
    	
    	public CreateGameAction(JFrame frame){
    		this.frame= frame;
    	}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			PlayOnlineActivity.created= false;
			new CreateGameActivity( frame);
		}	
    	
    }
    
    /**
     * create the menu to start a new game
     * @param frame -frame to build the game
     * @param menuBar -menuBar to add the menu to
     */
    public static void addStartGameMenu(JFrame frame, JMenuBar menuBar){
    	JMenu newGameMenu= new JMenu("start new Game");
    	menuBar.add(newGameMenu);
    	List<ChessGame> gamesToPlay= getGamesToPlay(); 
    		
    	
    	for( ChessGame game : gamesToPlay){
    		JMenuItem menuItem= new JMenuItem(game.getName(), 0);
    		menuItem.addActionListener( new StartGameAction(frame, game) );
    		newGameMenu.add( menuItem);
    	}
    	
    }
    
    /**
     * @return list of games to play
     */
    public static List<ChessGame> getGamesToPlay(){
    	List<ChessGame> gamesToStart= new ArrayList<ChessGame>();
    	gamesToStart.add( new GlinskisChess());
    	gamesToStart.add( new ShafransChess());
    	gamesToStart.add( new StandardChess());
    	File dir= new File("src/resources/CustomGames");
    	for( File file : dir.listFiles()){
    		try {
    			CustomGame custom= new CustomGame();
				custom.loadFile( file.getPath());
				gamesToStart.add( custom);
			} catch (IOException | JSONException e) { e.printStackTrace(); }
    	}
    	return gamesToStart;
    }
    
    /**
     * @author Solaman
     * action listener to start the game
     */
    public static class StartGameAction implements ActionListener{
    	
		JFrame frameToDisplayGame;
    	ChessGame gameToDisplay;
    	
	    public StartGameAction(JFrame frame, ChessGame game){
	    	frameToDisplayGame= frame;
	    	gameToDisplay= game;
	    	
	    }

		@Override
		public void actionPerformed(ActionEvent arg0) {
			PlayOnlineActivity.created= false;
			ChessBoard board = gameToDisplay.setUp();
			BoardPanel grid= board.getRepresentation();
			new PlayMouseAdapter(grid);
			frameToDisplayGame.getContentPane().removeAll();
			frameToDisplayGame.getContentPane().add( grid, BorderLayout.CENTER);
	        frameToDisplayGame.setSize( grid.getPreferredSize());
	        frameToDisplayGame.pack();
		}
    }

}
