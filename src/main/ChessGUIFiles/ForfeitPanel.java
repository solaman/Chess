package main.ChessGUIFiles;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import main.ChessBoard;
import main.ChessGame;

@SuppressWarnings("serial")
public class ForfeitPanel extends JPanel implements ActionListener {

	JButton button;
	int player;
	ChessGame game;
	ChessBoard board;

	public ForfeitPanel(ChessBoard board, int player){
		super(new BorderLayout());
        button = new JButton("Forfeit");
        this.player= player;
        this.board= board;
        
        add(button, BorderLayout.CENTER);
        button.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Toolkit.getDefaultToolkit().beep();
	}

}
