package main.onlineFiles;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import main.ChessGUIFiles.BoardPanelFiles.BoardPanel;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author Solaman
 * fields communication to the server. Responsible for sending Commands with the correct format, as well as interpreting
 * commands sent as a response to the server.
 */
public class CommandServer {
	
	public CommandHandler commandHandler;
	
	public CommandServer() throws IOException { 
		commandHandler= new CommandHandler();
	    
	    InetSocketAddress addr = new InetSocketAddress(8081);
	    HttpServer server = HttpServer.create(addr, 0);

	    server.createContext("/", new requestHandler(this));
	    server.setExecutor(Executors.newCachedThreadPool());
	    server.start();
	    System.out.println("Server is listening on port 8081" );

	  }

	/**
	 * @author Solaman
	 * upon request, perform the received command on the board,
	 * and return a command made by the user, will stall if no command is available
	 */
	class requestHandler implements HttpHandler {
		CommandServer client;
		
		public requestHandler(CommandServer client){
			super();
			this.client= client;
		}
		
		public void handle(HttpExchange exchange) throws IOException {
		    String requestMethod = exchange.getRequestMethod();
		    if (requestMethod.equalsIgnoreCase("POST") && 
		    		exchange.getRequestHeaders().get("Content-Type").get(0).equalsIgnoreCase("text/javascript")) {
		    	
		    	doCommand(exchange);
				
				Headers responseHeaders = exchange.getResponseHeaders();
				responseHeaders.set("Content-Type", "text/javascript");
				exchange.sendResponseHeaders(200, 0);
				
				OutputStream responseBody = exchange.getResponseBody();
				responseBody.write( commandHandler.removeMainCommand().toString().getBytes());
				responseBody.close();
		    }
		  }
		  
		  
			/**
			 * do the command described in exchange content
			 * @param exchange- exchange between server and client
			 * @return was the command performed successfully?
			 */
			private boolean doCommand(HttpExchange exchange){
			  
				try {
					String theString = IOUtils.toString(exchange.getRequestBody());
					JSONObject command= new JSONObject(theString);
					commandHandler.doSubCommand(command);
				} catch (JSONException| IOException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
		  }
		  
		}  

}
