package main.ChessGUIFiles;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.json.JSONException;

import DragnGhost.GhostGlassPane;
import main.ChessBoard;
import main.ChessGame;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.MouseAdapters.CreateMouseAdapter;
import main.ChessGUIFiles.MouseAdapters.PlayMouseAdapter;
import main.chessGames.CustomGame;
import main.chessGames.GlinskisChess;
import main.chessGames.ShafransChess;
import main.chessGames.StandardChess;
import main.chessPieces.Bishop;
import main.chessPieces.King;
import main.chessPieces.Knight;
import main.chessPieces.Pawn;
import main.chessPieces.Queen;
import main.chessPieces.Rook;
import main.onlineFiles.CommandClient;
import main.onlineFiles.CommandHandler;
import main.onlineFiles.CommandServer;

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
    
    private static void addPlayOnlineMenu(JFrame frame, JMenuBar menuBar2) {
		JMenu playOnlineMenu= new JMenu("play online");
		JMenuItem play= new JMenuItem("play");
		play.addActionListener(new PlayOnlineAction(frame));
		playOnlineMenu.add(play);
		menuBar2.add(playOnlineMenu);
		
	}
    
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

	public static void restartMenu(){
    	Thisframe.setJMenuBar(null);
    	buildMenu(Thisframe);
    }
    
    private static void addCreateGameMenu(JFrame frame, JMenuBar menuBar){
    	JMenu createGameMenu= new JMenu("create new game");
    	JMenuItem create= new JMenuItem("create");
    	create.addActionListener(new CreateGameAction(frame));
    	createGameMenu.add(create);
    	menuBar.add( createGameMenu);
    }
    
    private static class CreateGameAction implements ActionListener{

    	JFrame frame;
    	
    	public CreateGameAction(JFrame frame){
    		this.frame= frame;
    	}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			new CreateGameActivity( frame);
		}	
    	
    }
    
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
    
    public static List<ChessGame> getGamesToPlay(){
    	List<ChessGame> gamesToStart= new ArrayList<ChessGame>();
    	gamesToStart.add( new GlinskisChess());
    	gamesToStart.add( new ShafransChess());
    	gamesToStart.add( new StandardChess());
    	File dir= new File("resources/CustomGames");
    	for( File file : dir.listFiles()){
    		CustomGame custom= new CustomGame();
    		try {
				custom.loadFile( file.getPath());
				gamesToStart.add( custom);
			} catch (IOException | JSONException e) { e.printStackTrace(); }
    	}
    	return gamesToStart;
    }
    
    public static class StartGameAction implements ActionListener{
    	
		JFrame frameToDisplayGame;
    	ChessGame gameToDisplay;
    	
	    public StartGameAction(JFrame frame, ChessGame game){
	    	frameToDisplayGame= frame;
	    	gameToDisplay= game;
	    	
	    }

		@Override
		public void actionPerformed(ActionEvent arg0) {
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