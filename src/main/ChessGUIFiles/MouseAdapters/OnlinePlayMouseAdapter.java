package main.ChessGUIFiles.MouseAdapters;

import java.awt.event.MouseEvent;

import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

public class OnlinePlayMouseAdapter extends PlayMouseAdapter {

	int player;
	int eventLock;
	
	public OnlinePlayMouseAdapter(BoardPanel panel, int player) {
		super(panel);
		this.player= player;
		
	}
	
	@Override 
	public void mouseMoved(MouseEvent e){
		if( panel.board.getPlayerTurn() == player)
			super.mouseMoved(e);
	}
	
	@Override
	public void mouseClicked(MouseEvent e){
		if( panel.board.getPlayerTurn() == player)
			super.mouseClicked(e);
	}

}
