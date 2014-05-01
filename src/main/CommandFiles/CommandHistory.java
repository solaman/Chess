package main.CommandFiles;

import java.util.Stack;

import main.ChessBoard;
import main.ChessPiece;

/**
 * @author Solaman
 * stores CommandSequences to be used for review, doing and undoing.
 * because undoing commands is highly dependent on the sequence of commands that are already performed,
 * it is highly advisable NOT to add them yourself, but to use "undoMoves" and "doMove"
 */
public class CommandHistory extends Stack<CommandSequence> {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Query the stored CommandSequences to return the number of moves made by a piece
	 * @param depthMax -how deep should we check?
	 * @param performer -ChessPiece to query moves for.
	 * @param moveMade -null if any move made by the piece should be counted, other if a certain type of move is counted.
	 * @return -number of moves made.
	 */
	public int queryNumOfMoves(int depthMax, ChessPiece performer, Class<?> moveMade){
		int total=0;
		for(int depth=0; size()-depth> 0 && (depth< depthMax || depthMax == -2); depth++){
			CommandSequence current= elementAt(size()-depth-1);
			if(current == null)
				break;
			if(current.getPerformingPiece() == performer && (moveMade == null || moveMade.isAssignableFrom( current.getMovePerformed().getClass())))
				total++;
		}
			return total;
	}
	
	/**
	 * pops and undoes CommandSequences in storage
	 * @param turnsToGoBack -number of CommandSequences to undo/turns to go back
	 * @param board -board to undo the CommandSequences on
	 */
	public void undoMoves(int turnsToGoBack, ChessBoard board){
		while(turnsToGoBack-- > 0 && !isEmpty())
			 pop().undoCommands(board);
	}
	
	/**
	 * read the instructions to "do" a CommandSequence and stores it.
	 * @param toDo -CommandSequence defining movement to do
	 * @param board -board to perform the CommandSequence on
	 */
	public void doMove(CommandSequence toDo, ChessBoard board){
		toDo.doCommands(board);
		push(toDo);
	}

}
