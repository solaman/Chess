package main.ChessGUIFiles;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.ChessBoard;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

@SuppressWarnings("serial")
public class UndoTurnsPanel extends JPanel implements ActionListener{

	JButton button;
	JTextField textField;
	int player;
	ChessBoard board;
	BoardPanel boardPanel;

	public UndoTurnsPanel(BoardPanel bp, ChessBoard board, int player){
		super(new GridLayout(2,1));
		boardPanel = bp;
        button = new JButton("UndoTurns");
        button.addActionListener(this);
        add(button);
        textField = new JTextField("0");
        add(textField);
      
        this.player= player;
        this.board= board;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//if( game.getPlayerTurn() != player) return;
		int movesToUndo = (int)((Double.parseDouble(textField.getText())));
		JFrame secondWindow= new JFrame();
		secondWindow.getContentPane().add(new UndoTurnsConfirmPanel( boardPanel, board, player, movesToUndo), BorderLayout.CENTER);
		secondWindow.pack();
        secondWindow.setVisible(true);
	}
}
