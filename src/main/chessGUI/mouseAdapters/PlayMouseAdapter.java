package main.chessGUI.mouseAdapters;

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

import main.boards.ChessBoard;
import main.boards.ChessSpace;
import main.chessGUI.boardPanels.BoardPanel;
import main.chessGUI.boardPanels.Cell;
import main.moveHistory.MoveSequence;
import main.pieces.ChessPiece;

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
				List< MoveSequence> toCheck= occupant.getAllCommandSequences(panel.board);
				for(MoveSequence commandSequence : toCheck){
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
				List< MoveSequence> toCheck= activePiece.getAllCommandSequences(panel.board);
				for(MoveSequence commandSequence : toCheck)
					if( (clickedSpace == commandSequence.getTargetSpace())){
						doCommand(commandSequence);
						return;
					}
			}
			if(activePiece != null) activePiece = null;
			else if( clickedSpace.getOccupant() != null && panel.board.getPlayerTurn() == clickedSpace.getOccupant().getPlayer()){
				clickedCell.setColor(Cell.HIGHLIGHTCOLOR);
				activePiece= clickedSpace.getOccupant();
				List< MoveSequence> toCheck= activePiece.getAllCommandSequences(panel.board);
				for(MoveSequence commandSequence : toCheck){
					ChessSpace toHighLight = commandSequence.getTargetSpace();
					panel.grid[toHighLight.getXCoord()][toHighLight.getYCoord()].setColor(Cell.HIGHLIGHTCOLOR);
				}
			}
			panel.repaint();
			
		}

		protected void doCommand(MoveSequence commandSequence) {
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
