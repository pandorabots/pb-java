/**
 * PandorabotsAPI.java
 */
package com.pandorabots.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * previously supported
 * talk(botname, input)
 * talk(botname, clientId, input)
 * 
 * 
 * Now support following functionality
 * talk(botname, input)
 * talk(botname, clientId, input)
 * talk(botname, clientId, input, extra) //with recent calls always true
 * atalk(botname,input)
 * atalk(botname,clientId,input)
 * atalk(botname,clientId,input,extra) //with recent calls always true
 */

/**
 * Pandorabots API Class.
 * <p>
 * Created by User on 6/25/2014.<br>
 * Edited by Aadish Joshi on 5/30/2018
 * See: <a href="https://developer.pandorabots.com/docs">Pandorabots API
 * Documentation</a><br>
 * </p>
 * 
 * @author Richard Wallace
 * @edited by Aadish Joshi
 * @version 1.0.1
 */
public class PandorabotsAPI {
	private String host = null;
	private String userKey = null;
	private String appId = null;
	private int sessionId = -1;
	private String set_client_name = null;

	/** flag to indicate verbosity of output. */
	private boolean debug = false;

	/**
	 * Print out debuggining info.
	 * 
	 * @param message
	 *            log message
	 * @since 0.0.1
	 */
	private void Log(String message) {
		if (debug)
			System.out.println("PandorabotsAPI: " + message);
	}

	/**
	 * Constructor without debug.
	 * 
	 * @see #PandorabotsAPI(String, String, String, boolean)
	 * @since 0.0.1
	 */
	public PandorabotsAPI(String host, String appId, String userKey) {
		this(host, appId, userKey, false);
	}

	/**
	 * Constructor.
	 * 
	 * @param host
	 *            host name of pandrabots API server
	 * @param appId
	 *            app_id to pandrabots API
	 * @param userKey
	 *            user_key to pandrabots API
	 * @param debug
	 *            print debug info if true
	 * @since 0.0.9
	 */
	public PandorabotsAPI(String host, String appId, String userKey,
			boolean debug) {
		this.host = host;
		this.appId = appId;
		this.userKey = userKey;
		this.debug = debug;
	}

	/**
	 * helper for composing URI.
	 * 
	 * @param path
	 *            string to be added
	 * @return string of partial URI path
	 * @since 0.0.9
	 */
	private String sep(String path) {
		return path == null ? "" : "/" + path;
	}

	/**
	 * composing path part of URI.
	 * 
	 * @param mode
	 *            bot or talk or atalk
	 * @param botName
	 *            name of bot
	 * @param kind
	 *            file kind
	 * @param fileName
	 *            file name
	 * @return string of path part of URI
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private String composeUri(String mode, String botName, String kind,
			String fileName) throws URISyntaxException {
		String uri = "https://" + host;
		uri += sep(mode);
		uri += sep(appId);
		uri += sep(botName);
		uri += sep(kind);
		uri += sep(fileName);
		return uri;
	}

	/**
	 * base parameter for HTTP request.
	 * 
	 * @return list of name-value pair represents URI parameters
	 * @since 0.0.9
	 */
	private List<NameValuePair> baseParams() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_key", userKey));
		return params;
	}

	/**
	 * composing parameter part of URI.
	 * 
	 * @return string of parameter part of URI
	 * @since 0.0.9
	 */
	private String composeParams(List<NameValuePair> params) {
		List<NameValuePair> baseParams = baseParams();
		if (params != null)
			baseParams.addAll(params);
		return "?" + URLEncodedUtils.format(baseParams, "UTF-8");
	}

	/**
	 * composing URI for listing bot.
	 * 
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private URI listUri() throws URISyntaxException {
		return new URI(composeUri("bot", null, null, null)
				+ composeParams(null));
	}

	/**
	 * composing URI for handling bot.
	 * 
	 * @param botName
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private URI botUri(String botName) throws URISyntaxException {
		return new URI(composeUri("bot", botName, null, null)
				+ composeParams(null));
	}

	/**
	 * composing URI for handling file.
	 * <p>
	 * URI form varies as follows depending on extension.<br>
	 * <ul>
	 * <li>/bot/{appId}/{botName}/{fileKind} for pdefaults, properties.
	 * <li>/bot/{appId}/{botName}/{fileKind}/baseName for map, set,
	 * substitution.
	 * <li>/bot/{appId}/{botName}/file/baseName.extName for aiml.
	 * </ul>
	 * <p>
	 * 
	 * @param botName
	 * @param pathName
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private URI fileUri(String botName, String pathName)
			throws URISyntaxException {
		String baseName = FilenameUtils.getBaseName(pathName);
		String extName = FilenameUtils.getExtension(pathName);
		String fileKind = extName;
		String fileName = null;
		if (extName.equals("pdefaults") || extName.equals("properties")) {
			;
		} else if (extName.equals("map") || extName.equals("set")
				|| extName.equals("substitution")) {
			fileName = baseName;
		} else if (extName.equals("aiml")) {
			fileKind = "file";
			fileName = baseName + "." + extName;
		}
		return new URI(composeUri("bot", botName, fileKind, fileName)
				+ composeParams(null));
	}

	/**
	 * composing URI for downloading zip.
	 * 
	 * @param botName
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private URI zipUri(String botName) throws URISyntaxException {
		List<NameValuePair> params = baseParams();
		params.add(new BasicNameValuePair("return", "zip"));
		return new URI(composeUri("bot", botName, null, null)
				+ composeParams(params));
	}

	/**
	 * composing URI for compiling bot.
	 * 
	 * @param botName
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private URI verifyUri(String botName) throws URISyntaxException {
		return new URI(composeUri("bot", botName, "verify", null)
				+ composeParams(null));
	}

	/**
	 * composing URI for talking to bot.
	 * 
	 * @param botName
	 * @param params
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	private URI talkUri(String botName) throws URISyntaxException {
		return new URI(composeUri("talk", botName, null, null));
	}
	
	/**
	 * composing URI for anonymously talking to bot.
	 * 
	 * @param botName
	 * @param params
	 * @return URI for request
	 * @throws URISyntaxException
	 * @since 1.0.1
	 */
	
	private URI atalkUri(String botName) throws URISyntaxException {
		return new URI(composeUri("atalk", botName, null, null));
	}


	/**
	 * Read response from Pandorabots server.
	 * 
	 * @param httpResp
	 *            HTTP response
	 * @return HTTP response
	 * @since 0.0.1
	 */
	public String readResponse(HttpResponse httpResp) {
		String response = "";
		try {
			int code = httpResp.getStatusLine().getStatusCode();
			Log("Response code=" + code);
			InputStream is = httpResp.getEntity().getContent();
			BufferedReader inb = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder("");
			String line;
			String NL = System.getProperty("line.separator");
			while ((line = inb.readLine()) != null) {
				sb.append(line).append(NL);
				Log("Read " + line);
			}
			inb.close();
			response = sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	/**
	 * List names of bots
	 * 
	 * @return list of name of bots
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws JSONException
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	public List<String> list() throws ClientProtocolException, IOException,
			JSONException, URISyntaxException {
		URI uri = listUri();
		Log("List uri=" + uri);
		String response = Request.Get(uri).execute().returnContent().asString();
		JSONArray jArray = new JSONArray(response);
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < jArray.length(); i++) {
			names.add(jArray.getJSONObject(i).getString("botname"));
		}
		return names;
	}

	/**
	 * Create a bot.
	 * 
	 * @param botName
	 *            name of bot
	 * @return status of response
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @since 0.0.1
	 */
	public String createBot(String botName) throws ClientProtocolException,
			IOException, URISyntaxException {
		URI uri = botUri(botName);
		Log("Create botName=" + botName + " uri=" + uri);
		HttpResponse response = Request.Put(uri).execute().returnResponse();
		return readResponse(response);
	}

	/**
	 * Delete a existing bot.
	 * 
	 * @param botName
	 *            name of bot
	 * @return status of response
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @since 0.0.1
	 */
	public String deleteBot(String botName) throws ClientProtocolException,
			IOException, URISyntaxException {
		URI uri = botUri(botName);
		Log("Delete botName=" + botName + " uri=" + uri);
		HttpResponse response = Request.Delete(uri).execute().returnResponse();
		return readResponse(response);
	}

	/**
	 * Upload a file.
	 * 
	 * @param botName
	 *            name of bot
	 * @param pathName
	 *            path for upload file
	 * @return status of response
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @since 0.0.1
	 */
	public String uploadFile(String botName, String pathName)
			throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = fileUri(botName, pathName);
		Log("Upload botName=" + botName + " pathName=" + pathName + " uri="
				+ uri);
		HttpResponse response = Request.Put(uri)
				.bodyByteArray(Files.readAllBytes(Paths.get(pathName)))
				.execute().returnResponse();
		return readResponse(response);
	}

	/**
	 * Remove a file.
	 * 
	 * @param botName
	 *            name of bot
	 * @param fileName
	 * @return status of response
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	public String removeFile(String botName, String fileName)
			throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = fileUri(botName, fileName);
		Log("Remove botName=" + botName + " fileName=" + fileName + " uri="
				+ uri);
		HttpResponse response = Request.Delete(uri).execute().returnResponse();
		return readResponse(response);
	}

	/**
	 * Download a file.
	 * 
	 * @param botName
	 *            name of bot
	 * @param fileName
	 *            file name to be downloaded
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	public void downloadFile(String botName, String fileName)
			throws ClientProtocolException, IOException, URISyntaxException {
		URI uri = fileUri(botName, fileName);
		Log("Download botName=" + botName + " fileName=" + fileName + " uri="
				+ uri);
		Request.Get(uri).execute().saveContent(new File(fileName));
	}

	/**
	 * Get property of bot
	 * 
	 * @param botName
	 *            name of bot
	 * @return json string of properties
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	public String getProperties(String botName) throws ClientProtocolException,
			IOException, URISyntaxException {
		URI uri = botUri(botName);
		Log("Get botName=" + botName + " uri=" + uri);
		HttpResponse response = Request.Get(uri).execute().returnResponse();
		return readResponse(response);
	}

	/**
	 * Get ZIP of bot
	 * 
	 * @param botName
	 *            name of bot
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 * @since 0.0.9
	 */
	public void getZip(String botName) throws ClientProtocolException,
			IOException, URISyntaxException {
		URI uri = zipUri(botName);
		Log("Get ZIP botName=" + botName + " uri=" + uri);
		Request.Get(uri).execute().saveContent(new File(botName + ".zip"));
	}

	/**
	 * Compile a existing bot.
	 * 
	 * @param botName
	 *            name of bot
	 * @return status of response
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @since 0.0.1
	 */
	public String compileBot(String botName) throws ClientProtocolException,
			IOException, URISyntaxException {
		URI uri = verifyUri(botName);
		Log("Compile botName=" + botName + "uri=" + uri);
		HttpResponse response = Request.Get(uri).execute().returnResponse();
		return readResponse(response);
	}

	/**
	 * Simplest method to talk to a bot.
	 * 
	 * @param botName
	 *            name of bot
	 * @param input
	 *            text for conversation
	 * @return text of bot's response
	 * @see #talk(String, String, String)
	 * @since 0.0.1
	 */

	public String talk(String botName, String input)
			throws ClientProtocolException, IOException, JSONException,
			URISyntaxException {
		return debugBot(botName, null, input, false, false, false, false,
				false);
	}
	/**
	 * Talk to a bot as a specific client.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client
	 * @param input
	 *            text for conversation
	 * @return text of bot's response
	 * @see #debugBot(String, String, String, boolean, boolean, boolean,
	 *      boolean, boolean)
	 * @since 0.0.1
	 */
	public String talk(String botName, String clientName, String input)
			throws ClientProtocolException, IOException, JSONException,
			URISyntaxException {
		return debugBot(botName, clientName, input, false, false, false, false,
				false);
	}
	
	/**
	 * Talk to a bot as a specific client debug purpose.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client
	 * @param input
	 *            text for conversation
	 * @param extra
	 *            boolean for conversation
	 * @param recent = true
	 *            boolean for conversation
	 * @return text of bot's response
	 * @see #debugBot(String, String, String, boolean, boolean, boolean,
	 *      boolean, boolean)
	 * @since 1.0.1
	 */

	public String talk(String botName, String clientName, String input,boolean extra)
			throws ClientProtocolException, IOException, JSONException,
			URISyntaxException {
		return debugBot(botName, clientName, input, extra, false, false, false,
				true);
	}
	
	/**
	 * Simplest method to anonymously talk to a bot.
	 * 
	 * @param botName
	 *            name of bot
	 * @param input
	 *            text for conversation
	 * @return text of bot's response
	 * @see #atalkDebugBot(String, String, String, boolean, boolean, boolean,
	 *      boolean, boolean)
	 * @since 1.0.1
	 */
	
	public String atalk(String botName, String input)
			throws ClientProtocolException, IOException, JSONException,
			URISyntaxException {
		return atalk(botName, null, input);
	}
	
	/**
	 * Anonymously talk to a bot as a specific client.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client
	 * @param input
	 *            text for conversation
	 * @return text of bot's response
	 * @see #atalkDebugBot(String, String, String, boolean, boolean, boolean,
	 *      boolean, boolean)
	 * @since 1.0.1
	 */
	
	public String atalk(String botName, String clientName, String input)
			throws ClientProtocolException, IOException, JSONException,
			URISyntaxException {
		return atalkDebugBot(botName, clientName, input, false, false, false, false,
				false);
	}
	
	
	/**
	 * Anonymously talk to a bot as a specific client debug purpose.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client
	 * @param input
	 *            text for conversation
	 * @param extra
	 *            boolean for conversation
	 * @param recent = true
	 *            boolean for conversation
	 * @return text of bot's response
	 * @see #atalkDebugBot(String, String, String, boolean, boolean, boolean,
	 *      boolean, boolean)
	 * @since 1.0.1
	 */
	
	public String atalk(String botName, String clientName, String input,boolean extra)
			throws ClientProtocolException, IOException, JSONException,
			URISyntaxException {
		return atalkDebugBot(botName, clientName, input, extra, false, false, false,
				true);
	}

	/**
	 * Most general version of talk method that returns detailed debugging
	 * information.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client (optional)
	 * @param input
	 *            text for conversation
	 * @param extra
	 *            adds extra information into response
	 * @param reset
	 *            reset status of bot
	 * @param trace
	 *            adds trace data into response
	 * @param reload
	 *            force system to reload bot
	 * @param recent
	 *            use recent pod even if it is older than files
	 * @return text of bot's response
	 * @return metadata response if extra set to true 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @since 0.0.1
	 * @edited 1.0.1
	 */
	public String debugBot(String botName, String clientName, String input,
			boolean extra, boolean reset, boolean trace, boolean reload,
			boolean recent) throws ClientProtocolException, IOException,
			JSONException, URISyntaxException {
		URI uri = talkUri(botName);
		Log("Talk botName=" + botName + " input=\"" + input + "\"" + " uri="
				+ uri);
		List<NameValuePair> params = baseParams();
		params.add(new BasicNameValuePair("input", input));
		if (clientName != null)
			params.add(new BasicNameValuePair("client_name", clientName));
		if (sessionId != -1)
			params.add(new BasicNameValuePair("sessionid", Integer.toString(sessionId)));
		if (extra)
			params.add(new BasicNameValuePair("extra", "true"));
		if (reset)
			params.add(new BasicNameValuePair("reset", "true"));
		if (trace)
			params.add(new BasicNameValuePair("trace", "true"));
		if (reload)
			params.add(new BasicNameValuePair("reload", "true"));
		if (recent)
			params.add(new BasicNameValuePair("recent", "true"));
		Log("Talk params=" + URLEncodedUtils.format(params, "UTF-8"));
		try{
			String response = Request.Post(uri).bodyForm(params).execute()
			
					.returnContent().asString();
			JSONObject jObj = new JSONObject(response);
			sessionId = jObj.getInt("sessionid");
//			sessionId = Integer.parseInt(jObj.getString("sessionid"));
//			System.out.println("SessionId:"+sessionId);
			JSONArray jArray = jObj.getJSONArray("responses");
			if(extra){
				Log("\nExtra: \n"+jObj.toString());
				}
			String responses = "";
			for (int i = 0; i < jArray.length(); i++) {
				responses += jArray.getString(i).trim();
			}
			return responses;
		}catch(Exception e) {
			Log("\nError: "+e.toString());
			return "";
		}
	}
	
	/**
	 * Most general version of anonymous talk method that returns detailed debugging
	 * information.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client (optional)
	 * @param input
	 *            text for conversation
	 * @param extra
	 *            adds extra information into response
	 * @param reset
	 *            reset status of bot
	 * @param trace
	 *            adds trace data into response
	 * @param reload
	 *            force system to reload bot
	 * @param recent
	 *            use recent pod even if it is older than files
	 * @return text of bot's response
	 * @return newly generated clientId
	 * @return metadata response if extra set to true 
	 * @throws IOException
	 * @throws JSONException
	 * @throws ClientProtocolException
	 * @throws URISyntaxException
	 * @since 1.0.1
	 */
	
	public String atalkDebugBot(String botName, String clientName, String input,
			boolean extra, boolean reset, boolean trace, boolean reload,
			boolean recent) throws ClientProtocolException, IOException,
			JSONException, URISyntaxException {
		URI uri = atalkUri(botName);
		Log("Anonymously Talk to botName=" + botName + " input=\"" + input + "\"" + " uri="
				+ uri);
		List<NameValuePair> params = baseParams();
		params.add(new BasicNameValuePair("input", input));
		if (clientName != null)
			params.add(new BasicNameValuePair("client_name", clientName));
		if (sessionId != -1)
			params.add(new BasicNameValuePair("sessionid", Integer.toString(sessionId)));
		if (extra)
			params.add(new BasicNameValuePair("extra", "true"));
		if (reset)
			params.add(new BasicNameValuePair("reset", "true"));
		if (trace)
			params.add(new BasicNameValuePair("trace", "true"));
		if (reload)
			params.add(new BasicNameValuePair("reload", "true"));
		if (recent)
			params.add(new BasicNameValuePair("recent", "true"));
		Log("aTalk params=" + URLEncodedUtils.format(params, "UTF-8"));
		
		String responses = "";
		try {
			String response = Request.Post(uri).bodyForm(params).execute()
					.returnContent().asString();
			JSONObject jObj = new JSONObject(response);
			sessionId = jObj.getInt("sessionid");
//			sessionId = Integer.parseInt(jObj.getString("sessionid"));
//			System.out.println("SessionId:"+sessionId);
			set_client_name = jObj.getString("client_name");
			JSONArray jArray = jObj.getJSONArray("responses");
			if(extra){
				Log("\nExtra: \n"+jObj.toString());
			}
			for (int i = 0; i < jArray.length(); i++) {
				responses += jArray.getString(i).trim();
			}
			return responses +"\nclientId: "+set_client_name+"\n";
		}catch(Exception e) {
			Log("\nError"+e.toString());
			return "";
		}
	}
	
}
