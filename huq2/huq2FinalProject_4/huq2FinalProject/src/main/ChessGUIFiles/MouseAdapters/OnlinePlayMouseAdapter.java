package main.ChessGUIFiles.MouseAdapters;

import java.awt.event.MouseEvent;

import org.json.JSONObject;

import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.CommandFiles.CommandSequence;
import main.onlineFiles.CommandHandler;

public class OnlinePlayMouseAdapter extends PlayMouseAdapter {

	int player;
	CommandHandler commandHandler;
	
	public OnlinePlayMouseAdapter(BoardPanel panel, int player, final CommandHandler commandHandler) {
		super(panel);
		this.player= player;
		this.commandHandler= commandHandler;
		
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
	
	@Override
	protected void doCommand(CommandSequence commandSequence){
		try {
			JSONObject commJSON= commandSequence.asJSON(panel.board);
			JSONObject toPut= new JSONObject();
			toPut.put("content", commJSON);
			toPut.put("type", "command sequence");
			if(panel.board.performMovePermanent( commandSequence) )
				commandHandler.addMainCommand(toPut);
			activePiece = null;
			loserMessage();
			panel.repaint();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

}
