package main.ChessGUIFiles.MouseAdapters;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.ChessBoard;
import main.ChessPiece;
import main.ChessSpace;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import main.ChessGUIFiles.BoardPanelFiles.Cell;
import main.CommandFiles.CommandSequence;

public class PlayMouseAdapter extends MouseAdapter {
		
		BoardPanel panel;
		ChessPiece activePiece;
		
		public PlayMouseAdapter(BoardPanel panel){
			this.panel=panel;
			activePiece= null;
			for( MouseListener ml : panel.getMouseListeners())
				panel.removeMouseListener( ml);
			for( MouseMotionListener mml : panel.getMouseMotionListeners())
				panel.removeMouseMotionListener(mml);
			
			addListenersToPanel();
		}
		
		protected  void addListenersToPanel(){
			this.panel.addMouseListener(this);
			this.panel.addMouseMotionListener(this);
		}
		
		protected void loserMessage() {
			ChessBoard board= panel.board;
			int playerTurn= board.getPlayerTurn();
			try {
				if( !panel.board.hasLegalMove(playerTurn)){
					System.out.println("player turn: "+playerTurn);
					JPanel loserMessage= new JPanel();
					JLabel title= new JLabel("player "+(playerTurn+1) + " lost!");
					loserMessage.add(title);
					JFrame secondWindow= new JFrame();
					Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
					secondWindow.setLocation(dim.width/2-secondWindow.getSize().width/2, dim.height/2-secondWindow.getSize().height/2);
					secondWindow.getContentPane().add(loserMessage, BorderLayout.CENTER);
					secondWindow.pack();
			        secondWindow.setVisible(true);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void mouseMoved( MouseEvent e){
			if(activePiece != null)
				return;
			Cell clickedCell= panel.cellFromMouseEvent(e);
			if( clickedCell == null) return;
			ChessSpace clickedSpace= clickedCell.spaceReference;
			if(clickedSpace == null) return;
			ChessPiece occupant= clickedSpace.getOccupant();
			
			if(occupant != null && panel.board.getPlayerTurn() == occupant.getPlayer()){
				clickedCell.setColor(Cell.HIGHLIGHTCOLOR);
				List< CommandSequence> toCheck= occupant.getAllCommandSequences(panel.board);
				for(CommandSequence commandSequence : toCheck){
					ChessSpace toHighLight = commandSequence.getTargetSpace();
					panel.grid[toHighLight.getXCoord()][toHighLight.getYCoord()].setColor(Cell.HIGHLIGHTCOLOR);
				}
			}
			panel.repaint();
			
		}
		
		@Override
		public void mouseClicked(MouseEvent e) { 
			Cell clickedCell= panel.cellFromMouseEvent(e);
			
			if (clickedCell == null) return;
			ChessSpace clickedSpace= clickedCell.spaceReference;
			
			if(clickedSpace == null) return;

			if( activePiece != null){
				List< CommandSequence> toCheck= activePiece.getAllCommandSequences(panel.board);
				for(CommandSequence commandSequence : toCheck)
					if( (clickedSpace == commandSequence.getTargetSpace())){
						doCommand(commandSequence);
						return;
					}
			}
			if(activePiece != null) activePiece = null;
			else if( clickedSpace.getOccupant() != null && panel.board.getPlayerTurn() == clickedSpace.getOccupant().getPlayer()){
				clickedCell.setColor(Cell.HIGHLIGHTCOLOR);
				activePiece= clickedSpace.getOccupant();
				List< CommandSequence> toCheck= activePiece.getAllCommandSequences(panel.board);
				for(CommandSequence commandSequence : toCheck){
					ChessSpace toHighLight = commandSequence.getTargetSpace();
					panel.grid[toHighLight.getXCoord()][toHighLight.getYCoord()].setColor(Cell.HIGHLIGHTCOLOR);
				}
			}
			panel.repaint();
			
		}

		protected void doCommand(CommandSequence commandSequence) {
			try {
				panel.board.performMovePermanent( commandSequence);
				activePiece = null;
				loserMessage();
				panel.repaint();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
		}		 
}
