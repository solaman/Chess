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
import org.json.JSONException;
import org.json.JSONObject;

import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;

public class CommandClient {

	public CommandHandler commandHandler;
	String urlString = "http://localhost:8081";
	private final ExecutorService pool;
	
	public CommandClient(JFrame frame) throws IOException {
		commandHandler= new CommandHandler();
		commandHandler.setFrame(frame);
		firstPOST();
		pool = Executors.newFixedThreadPool(1);

		
		pool.execute( new ContinuousPost());
	}
	
	
	public void firstPOST() throws IOException{
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
