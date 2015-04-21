package main.chessGUI.mouseAdapters;

import java.awt.event.MouseEvent;

import org.json.*;

import main.chessGUI.boardPanels.BoardPanel;
import main.moveHistory.MoveSequence;
import main.onlineFiles.CommandHandler;

/**
 * @author Solaman
 * MouseAdapter used to facilitate play between two separate boards communicating the same game
 */
public class OnlinePlayMouseAdapter extends PlayMouseAdapter {

	/**
	 * dictates when the control of the OnlinePlayMouseAdapter
	 * is available during play
	 */
	int player;
	
	/**
	 * CommandHandler used to control the flow of commands between all components of the board
	 */
	CommandHandler commandHandler;
	
	/**
	 * @param panel -BoardPanel the Adapter interacts with
	 * @param player -player that this adapter allows control of the BoardPanel over
	 * @param commandHandler
	 */
	public OnlinePlayMouseAdapter(BoardPanel panel, int player, final CommandHandler commandHandler) {
		super(panel);
		this.player= player;
		this.commandHandler= commandHandler;
		
	}
	
	/* 
	 * ignores event if the board is not of the turn of the Adapter's player
	 */
	@Override 
	public void mouseMoved(MouseEvent e){
		if( panel.board.getPlayerTurn() == player)
			super.mouseMoved(e);
	}
	
	/* 
	 * ignores event if the board is not of the turn of the Adapter's player
	 */
	@Override
	public void mouseClicked(MouseEvent e){
		if( panel.board.getPlayerTurn() == player)
			super.mouseClicked(e);
	}
	
	/* 
	 * constructs a JSONObject representation of the command. if it is successfully performed then
	 * the command is sent to the commandHandler to be communicated to the other BoardPanel
	 */
	@Override
	protected void doCommand(MoveSequence commandSequence){
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
