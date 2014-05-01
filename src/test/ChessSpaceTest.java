package test;

import static org.junit.Assert.*;
import main.ChessMove;
import main.ChessPiece;
import main.ChessSpace;

import org.junit.Before;
import org.junit.Test;


public class ChessSpaceTest {
	
	ChessSpace space;
	ChessPiece piece;
	ChessMove move;
	ChessMove move2;
	

	@Before
	public void setUp() throws Exception{
		space=new ChessSpace(1, 1);
		piece= new ChessPiece(1);
	}
	
	@Test
	public void TestGetSetOccupant() {
		assertNull( space.getOccupant() );
		space.setOccupant(piece);
		assertNotNull(space.getOccupant());
	}
	
	@Test
	public void TestAddRemoveGetDependentMoves() {
		assertTrue(space.getDependentMoves().isEmpty());
		space.addDependentMove(move);
		space.addDependentMove(move2);
		assertTrue(space.getDependentMoves().size()==2);	
	}

}

