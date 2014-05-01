package main.ChessGUIFiles;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.ChessBoard;
import main.ChessGame;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

@SuppressWarnings("serial")
public class ChessPlayerPanel extends JPanel implements ActionListener{

	ChessGame game;
	ChessBoard board;
	int player;
	BoardPanel boardPanel;
	public ChessPlayerPanel(BoardPanel boardPanel, ChessBoard board, int player) {
		this.boardPanel= boardPanel;
		this.setLayout(new GridBagLayout());
		this.board= board;
		this.player=player;
		this.add(new JLabel("Player "+(player+1)) );
		this.add( new ForfeitPanel(board, player));
		this.add( new TrucePanel(board, player));
		this.add( new UndoTurnsPanel(boardPanel, board, player));
		this.add(new JButton("Complete Move"));
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		//
		
	}

	
}
