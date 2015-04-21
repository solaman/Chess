package main.ChessGUIFiles;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.json.*;

import main.ChessBoard;
import main.ChessPiece;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.BoardPanelFiles.HexagonalBoardPanel;
import main.ChessGUIFiles.MouseAdapters.CreateMouseAdapter;
import main.chessBoards.HexagonalBoard;
import main.chessGames.CustomGame;
import main.chessPieces.Bishop;
import main.chessPieces.King;
import main.chessPieces.Knight;
import main.chessPieces.Pawn;
import main.chessPieces.Queen;
import main.chessPieces.Rook;
import main.utils.ClassRetriever;
import main.utils.JNumericField;
import DragnGhost.GhostGlassPane;

/**
 * @author Solaman
 * used to set up a JFrame for building a ChessGame,
 * To avoid sub-classing, a majority of the code is found here
 */
@SuppressWarnings("serial")
public class CreateGameActivity extends JPanel {

	/**
	 * frame to build activity in
	 */
	JFrame frame;
	
	/**
	 * contains the board to build on
	 */
	BoardPanel boardPanel=null;
	
	/**
	 * used to drag and drop pieces onto the boardPanel
	 */
	CreateMouseAdapter createMouse;
	
	/**
	 * holds tabs for the board and all of the panels to select ChessMove loadOuts for ChessPieces
	 */
	JTabbedPane tabs;
	
	/**
	 * write error messages here for the user to see
	 */
	JTextField errorMessage;
	
	/**
	 * write a name for the game in this field
	 */
	JNumericField nameField;
	
	/**
	 * sets up the create game Activity.
	 * using borderLayout, puts dimension, board class, and name fields in the west pane,
	 * as well as a board displaying pieces to place.
	 * in the center Pane, creates a TabbedPane for selecting ChessMove loadOuts for pieces,
	 * and the board used to place pieces onto.
	 * @param frame to build Activity in
	 */
	public CreateGameActivity(JFrame frame) {
		frame.getContentPane().removeAll();
		this.frame = frame;
		tabs= new JTabbedPane();
		frame.getContentPane().add(tabs, BorderLayout.CENTER);
		setLayout(new GridBagLayout());
		
		JPanel boardOptions= createBoardOptions();
		GridBagConstraints constraints= new GridBagConstraints();
		constraints.gridx=0;
		constraints.anchor= GridBagConstraints.FIRST_LINE_START;
		add(boardOptions, constraints);
		
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx= 1;
		constraints.gridheight=12;
		
		JPanel piecesPanel= createPieceOptions();
		add(piecesPanel, constraints);
		addLoadOutOptions();
		
		constraints.gridx =0;
		constraints.gridheight=1;
		JLabel nameLabel= new JLabel("game name");
		nameField= new JNumericField( 30, JNumericField.NUMERIC);
		nameField.setFormat( JNumericField.CHAR);
		add(nameLabel, constraints);
		add(nameField, constraints);
		nameField.setColumns(10);
		JButton creatorOfTheWORLD= new JButton("create dis GAME!");
		creatorOfTheWORLD.addActionListener( createGameListener());
		add(creatorOfTheWORLD, constraints);
		
		errorMessage= new JTextField();
		errorMessage.setEditable(false);
		errorMessage.setColumns(20);
		add(errorMessage, constraints);
		frame.getContentPane().add( this, BorderLayout.WEST);
        frame.pack();
	}

	/**
	 * @return panel to use for board Options
	 */
	private JPanel createBoardOptions() {
		JPanel boardOptions= new JPanel();
		boardOptions.setLayout(new GridLayout(12, 1));
		JLabel boardLabel= new JLabel("board");
		boardOptions.add(boardLabel);
		
		String[] boardTypes= {"Rectangular Board", "Hexagonal Board"};
		JComboBox<String> boardTypeBox= new JComboBox<String>( boardTypes);
		boardOptions.add( boardTypeBox);
		
		JLabel widthLabel= new JLabel("width");
		boardOptions.add(widthLabel);
		

		JNumericField widthField = createDimensionField();
		boardOptions.add(widthField);
		
		JLabel heightLabel= new JLabel("height");
		boardOptions.add(heightLabel);
		
		JNumericField heightField = createDimensionField();
		boardOptions.add(heightField);
		
		new BoardOptionsListener(widthField, heightField, boardTypeBox);
		
		return boardOptions;	
	}
	
	/**
	 * @return JNumericField set for board dimension input
	 */
	private JNumericField createDimensionField(){
		JNumericField field= new JNumericField();
		field.setMaxLength(2); //Set maximum length             
		field.setPrecision(0); //Set precision (1 in your case)              
		field.setAllowNegative(false); //Set false to disable negatives
		field.setColumns(3);
		return field;
	}

	/**
	 * constructs a JPanel used to select pieces and
	 * constructs a CreateMouseAdapter to drag and drop them onto the boardPanel
	 * @return
	 */
	private JPanel createPieceOptions(){
		GhostGlassPane glassPane = new GhostGlassPane();
		frame.setGlassPane(glassPane);

		try {
			ChessBoard board= new ChessBoard(1, 12);
			int placeIndex=0;
			for(int i=0; i<2; i++){
				board.setPiece( new Bishop(i),0, placeIndex++ );
				board.setPiece( new King(i), 0, placeIndex++);
				board.setPiece( new Knight(i), 0, placeIndex++);
				board.setPiece( new Pawn(i), 0, placeIndex++);
				board.setPiece( new Queen(i), 0, placeIndex++);
				board.setPiece( new Rook(i), 0, placeIndex++);
			}
			BoardPanel piecesPanel= new BoardPanel(board);
			createMouse= new CreateMouseAdapter( piecesPanel, glassPane);
			return piecesPanel;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
		
	}
	
	/**
	 * adds tabs to tabs variable for all ChessPieces found in main.chessPieces
	 * TODO these are built-in references, this should automatically include all ChessPieces
	 */
	private void addLoadOutOptions(){
		List<Class> moveClasses= ClassRetriever.getClassesForPackage("main.chessMoves");
		addLoadOutOptions(Pawn.class, moveClasses);
		addLoadOutOptions(King.class, moveClasses);
		addLoadOutOptions(Bishop.class, moveClasses);
		addLoadOutOptions(Knight.class, moveClasses);
		addLoadOutOptions(Queen.class, moveClasses);
		addLoadOutOptions(Rook.class, moveClasses);
	}
	
	/**
	 * adds tabs to tabs variable that list possible ChessMoves
	 * to loadOut with a given ChessPiece, designated by the hint
	 * @param pieceClass -ChessPiece class the tab is for
	 * @param moveClasses -list of ChessMove classes to include in tab
	 */
	private void addLoadOutOptions(Class pieceClass, List<Class> moveClasses){
		String className= pieceClass.getName();
		JPanel loadOutPanel= createMoveOptions(moveClasses);
		tabs.addTab(className.substring(17), null, createMoveOptions(moveClasses), className);
	}
	
	/**
	 * creates a Panel of JCheckBoxes with text set as
	 * all ChessMoves found in main.chessMoves and subfolders
	 * @param moveClasses
	 * @return
	 */
	private JPanel createMoveOptions(List<Class> moveClasses){
		JPanel moveOptions= new JPanel();
		moveOptions.setLayout( new GridLayout(0,1));
		for( Class moveClass : moveClasses)
			moveOptions.add( new JCheckBox(moveClass.getName()));
		return moveOptions;
	}
	
	/**
	 * @author Solaman
	 * sets up the listener to build a boardPanel out of the boardOptions Panel. 
	 * set to automatically build on any change in the boardOptions Panel.
	 * returns an error message if the boardPanel could not be created.
	 */
	private class BoardOptionsListener implements KeyListener, ActionListener{

		JNumericField widthField;
		JNumericField heightField;
		JComboBox<String> boardTypeBox;
		
		public BoardOptionsListener( JNumericField widthField2, 
				JNumericField heightField2, JComboBox<String> boardTypeBox2){
			widthField= widthField2;
			heightField= heightField2;
			boardTypeBox= boardTypeBox2;
			
			widthField.addKeyListener(this);
			heightField.addKeyListener( this);
			boardTypeBox.addActionListener( this);
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			keyReleased(null);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if( boardPanel != null){
				tabs.remove(boardPanel);
				boardPanel= null;
				createMouse.boardToCreate= boardPanel;
			}
			errorMessage.setText("");
			if( widthField.getText().length() == 0 || heightField.getText().length() == 0 )
				return;
			int width= Integer.parseInt( widthField.getText() );
			int height= Integer.parseInt( heightField.getText());
			try{
				if( ((String)boardTypeBox.getSelectedItem()).equals("Rectangular Board"))
					boardPanel= new BoardPanel( new ChessBoard(width, height));
				else
					boardPanel= new HexagonalBoardPanel( new HexagonalBoard(width, height));
			}
			catch (Exception e1) {
				errorMessage.setText(e1.getMessage());
				return;
			}
			boardPanel.addMouseListener(createMouse);
			boardPanel.addMouseMotionListener(createMouse.ghostAdapter);
			createMouse.boardToCreate= boardPanel;
			tabs.addTab("board", null, boardPanel, "add pieces to this panel");
			tabs.setSelectedComponent(boardPanel);
			//frame.getContentPane().add( boardPanel, BorderLayout.CENTER);
			frame.pack();
		}

		@Override
		public void keyTyped(KeyEvent e) {}
		
		@Override
		public void keyPressed(KeyEvent e) {}
	}
	
	/**
	 * returns an ActionListener that will create a CustomGame from the Activity resources
	 * when activated
	 * @return
	 */
	private ActionListener createGameListener() {
		return new ActionListener(){

			/* 
			 * tries to save a json for a CustomGame built from the Activity resources,
			 * writes an error message on a failure
			 */
			@Override
			public void actionPerformed(ActionEvent arg0) {
				errorMessage.setText("");
				if( ! canBuild())
					return;
				
				String name= nameField.getText();	
				ChessBoard board= boardPanel.board;
				CustomGame toCreate= new CustomGame();
				toCreate.addBoard( boardPanel.board);
				
				for(List<ChessPiece> pieces : board.pieces)
					for(ChessPiece piece : pieces)
						toCreate.addPieceAt(piece, piece.getXCoord(), piece.getYCoord());
				addLoadOuts(toCreate);
				
				toCreate.changeName(name);
				try {
					toCreate.saveFile();
					ChessMenu.restartMenu();
				} catch (FileNotFoundException | UnsupportedEncodingException
						| JSONException e) {
					e.printStackTrace();
				}
			}
			
			/**
			 * adds load outs from the Activity to the CustomGame
			 * @param game to add loadouts to
			 */
			private void addLoadOuts(CustomGame game){
				int totalTabs = tabs.getTabCount();
				for(int i = 0; i < totalTabs; i++)
				{
				   Component c = tabs.getComponentAt(i);
				   if(! (c instanceof BoardPanel) ){
					   JPanel movePanel= (JPanel)c;
					   List<String> movesToLoad= new ArrayList<String>();
					   for(Component c2 : movePanel.getComponents()){
						   if( c2 instanceof JCheckBox){
							   JCheckBox moveBox= (JCheckBox)c2;
							   if( moveBox.isSelected()){
								   movesToLoad.add( moveBox.getText());
							   }
						   }
					   }
					   try {
						game.addLoadOut( Class.forName(tabs.getToolTipTextAt(i) ), movesToLoad);
					   } catch (ClassNotFoundException e) { e.printStackTrace();}
				   }
				}
				
			}
			
			/**
			 * Checks to see that a name is given, a board was created, and that
			 * both players are given exactly one king,
			 * writing an appropriate error message if otherwise
			 * @return can we build a game?
			 */
			private boolean canBuild(){
				String name= nameField.getText();
				if(name.isEmpty()){
					errorMessage.setText("game must be named!");
					return false;
				}
				if(boardPanel== null){
					errorMessage.setText("game must have board!");
					return false;
				}
				if( ! checkForKings()){
					errorMessage.setText("each player must have exactly one king!");
					return false;
				}
				return true;
			}
			
			/**
			 * @return does each player have exactly 1 King?
			 */
			private boolean checkForKings(){
				List< List<ChessPiece>> pieces = boardPanel.board.pieces;
				if (pieces.size() != 2)
					return false;
				for( List<ChessPiece> playerPieces : pieces){
					boolean hasKing=false;
					for(ChessPiece piece : playerPieces){
						if(piece instanceof King)
							if(hasKing== true)
								return false;
							else
								hasKing= true;
						}
					if(!hasKing)
						return false;
				}
				return true;
			}
			
		};
	}
		

}
