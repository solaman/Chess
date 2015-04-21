package main.onlineFiles;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.swing.JFrame;

import org.apache.commons.io.IOUtils;
import org.json.*;

import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

/**
 * @author Solaman
 * handles communication between one board and a remote other. Note:
 * functionality is not guaranteed between two boards unless one is a CommandClient
 * and the other is the CommandServer
 */
public class CommandClient {

	/**
	 * used to control the flow of commands that interact with the board and
	 * the CommandClient
	 */
	public CommandHandler commandHandler;
	
	/**
	 * used to refer to the port of communication
	 */
	final String urlString = "http://localhost:8081";
	
	/**
	 * used to allow the CommandClients communication with the server
	 * to be done in parallel
	 */
	private final ExecutorService pool;
	
	/**
	 * @param frame -frame of the GUI, primarily for the commandHandler to use
	 * @throws IOException -error occured while building commandHandler
	 */
	public CommandClient(JFrame frame) throws IOException {
		commandHandler= new CommandHandler();
		commandHandler.setFrame(frame);
		startGamePOST();
		pool = Executors.newFixedThreadPool(1);

		
		pool.execute( new ContinuousPost());
	}
	
	
	/**
	 * the first command sent MUST be to start a game.
	 * this function fascilitates a games construction from the
	 * CommandClient's end
	 * @throws IOException
	 */
	public void startGamePOST() throws IOException{
		URL urlObj= new URL(urlString);
		HttpURLConnection con= (HttpURLConnection) urlObj.openConnection();
		
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "text/javascript");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes( "{ \"type\": \"start the game!\"}");
		
		wr.flush();
		wr.close();
		
		
		doCommand(con);
	}
	
	/**
	 * @author Solaman
	 * continuously sends requests to the server, first sending this players command (as it is 0th player)
	 * or waiting if one is not available, and performing the command returned from the servers response
	 */
	public class ContinuousPost implements Runnable {

		@Override
		public void run() {
			while(true){
			try {
				URL urlObj= new URL(urlString);
				HttpURLConnection con= (HttpURLConnection) urlObj.openConnection();
				con.setRequestMethod("POST");
				con.setRequestProperty("Content-Type", "text/javascript");
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes( commandHandler.removeMainCommand().toString());
				
				wr.flush();
				wr.close();
				con.connect();
				doCommand(con);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			}
		}
		
	}

	
	/**
	 * pulls the JSONObject from the HttpURLConnection to perform its command in the commandHandler
	 * @param conn -HttpURLConnection to pull the command from
	 */
	public void doCommand(HttpURLConnection conn){
		String body;
		try {
			body = IOUtils.toString( conn.getInputStream() );
			commandHandler.doSubCommand( new JSONObject(body));
			
		} catch (IOException| JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
