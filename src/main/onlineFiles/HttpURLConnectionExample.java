package main.onlineFiles;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

import org.json.*;
 

import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;
 
public class HttpURLConnectionExample {
 
	private final String USER_AGENT = "Mozilla/5.0";
 
	public static void main(String[] args) throws Exception {
 
		HttpURLConnectionExample http = new HttpURLConnectionExample();
 
		System.out.println("Testing 1 - Send Http GET request");
		http.sendGet();
 
//		System.out.println("\nTesting 2 - Send Http POST request");
//		http.sendPost();
 
	}
 
	// HTTP GET request
	private void sendGet() throws Exception {
 
		String url = "http://web.engr.illinois.edu/~huq2/Chess/createAccountQuery.php";
 
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
		InetAddress ip;
		  try {
			  JSONObject body= new JSONObject();
			  ip = InetAddress.getLocalHost();
			  String address= ip.getHostAddress();
			  body.put("username", "hi");
			  body.put("password", "passe");
			  body.put("address1", 1);
			  body.put("address2", 2);
			  body.put("address3", 3);
			  body.put("address4", 4);
			  String toReturn= body.toString();
			  System.out.println("body "+ toReturn);
			  
			  connection.setDoOutput(true); // Triggers POST.
			  connection.setRequestProperty("Accept-Charset", "utf-8");
			  connection.setRequestProperty("Content-Type", "text/javascript");

			  try (OutputStream output = connection.getOutputStream()) {
			      output.write(toReturn.getBytes("utf-8"));
			  }

			  InputStream response = connection.getInputStream();
			  
			  String contentType = connection.getHeaderField("Content-Type");
			  System.out.println(contentType);
			  System.out.println( connection.getResponseCode());
			  System.out.println(connection.getContentLength());
			  String charset = null;

			  for (String param : contentType.replace(" ", "").split(";")) {
			      if (param.startsWith("charset=")) {
			          charset = param.split("=", 2)[1];
			          break;
			      }
			  }
			  
			  if (charset != null) {
			      try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset))) {
			          for (String line; (line = reader.readLine()) != null;) {
			              System.out.println(line);
			          }
			      }
			  }
	 
		  } catch (UnknownHostException e) {
	 
			e.printStackTrace();
	 
		  }
 
//		// optional default is GET
//		con.setRequestMethod("GET");
// 
//		//add request header
//		con.setRequestProperty("User-Agent", USER_AGENT);
// 
//		int responseCode = con.getResponseCode();
//		System.out.println("\nSending 'GET' request to URL : " + url);
//		System.out.println("Response Code : " + responseCode);
// 
//		BufferedReader in = new BufferedReader(
//		        new InputStreamReader(con.getInputStream()));
//		String inputLine;
//		StringBuffer response = new StringBuffer();
// 
//		while ((inputLine = in.readLine()) != null) {
//			response.append(inputLine);
//		}
//		in.close();
// 
		//print result
		//System.out.println(response.toString());
 
	}
 
	// HTTP POST request
	private void sendPost() throws Exception {
 
		String url = "https://selfsolve.apple.com/wcResults.do";
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
 
		String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
 
	}
 
}