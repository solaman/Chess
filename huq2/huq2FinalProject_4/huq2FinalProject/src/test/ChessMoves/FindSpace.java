package test.ChessMoves;

import java.util.List;

import main.ChessSpace;
import main.CommandFiles.CommandSequence;

public class FindSpace {

		
		/**
		 * helper function checks to see if a space exists in the list of list of ChessSpaces
		 * @param lps -liftPlaceSequences to check
		 * @param toFind -space to find
		 * @return did we find it?
		 */
		public static boolean doIt(List<CommandSequence> cs, ChessSpace toFind){
			for(CommandSequence commandSequence : cs)
				if(toFind == commandSequence.getTargetSpace())
						return true;
			return false;
		}
}