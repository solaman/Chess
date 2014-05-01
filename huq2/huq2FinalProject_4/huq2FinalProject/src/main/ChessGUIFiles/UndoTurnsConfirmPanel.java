package main.ChessGUIFiles;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.ChessBoard;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

@SuppressWarnings("serial")
public class UndoTurnsConfirmPanel extends JPanel implements ActionListener {

	JButton button;
	JTextField textField;
	int player;
	ChessBoard board;
	int numberOfTurns;
	BoardPanel boardPanel;
	
	public UndoTurnsConfirmPanel(BoardPanel bp, ChessBoard board, int player, int numberOfTurns){
		super(new GridLayout(2,1));
		boardPanel=bp;
		JLabel title= new JLabel("player "+(player+1) + " wants to undo the last "+ 
				numberOfTurns +" turns. Click \"UndoTurns\" to confirm or X out to cancel.");
        button = new JButton("UndoTurns");
        button.addActionListener(this);
        add(title);
        add(button);
      
        this.numberOfTurns= numberOfTurns;
        this.player= player;
        this.board= board;;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		board.reverseMoves(numberOfTurns);
		boardPanel.repaint();
	}
}
