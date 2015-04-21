package main;
import javax.swing.*;

import main.ChessGUIFiles.ChessMenu;


/**
 * creates the application along with its menus
 * @author Solaman
 *
 */
public class ChessGUI {
	
	 public static void main(String[] args) {
	        //Schedule a job for the event-dispatching thread:
	        //creating and showing this application's GUI.
	        javax.swing.SwingUtilities.invokeLater(new Runnable() {
	            @Override
				public void run() {
	                try {
						createAndShowGUI();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        });
	    }

	/**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
	 * @throws Exception 
     */
    private static void createAndShowGUI() throws Exception {
        //Create and set up the window.
        JFrame frame = new JFrame("Huq2Chess");
        ChessMenu.buildMenu( frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel label = new JLabel("ChessGame");
        frame.getContentPane().add(label);
		frame.setLocationRelativeTo( null );
        frame.pack();
        frame.setVisible(true);
    }
    
    
}

