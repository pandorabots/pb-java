/**
 * PandorabotsAPI.java
 */
package com.pandorabots.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Pandorabots API Class.
 * <p>
 * Created by User on 6/25/2014.<br>
 * See: <a href="https://developer.pandorabots.com/docs">Pandorabots API
 * Documentation</a><br>
 * </p>
 * 
 * @author Richard Wallace
 * @version 0.0.8
 */
public class PandorabotsAPI {
	private String host = "";
	private String userKey = "";
	private String appId = "";
	private String sessionId = "";
	private String tag = "PandorabotsAPI";
	private String protocol = "https:";

	/** flag to indicate verbosity of output. */
	private boolean debug = false;

	/**
	 * Print out debuggining info.
	 * 
	 * @param tag
	 * @param message
	 * @since 0.0.1
	 */
	private void Log(String tag, String message) {
		if (debug)
			System.out.println(tag + ": " + message);
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
			Log(tag, "Response code = " + code);
			InputStream is = httpResp.getEntity().getContent();
			BufferedReader inb = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder("");
			String line;
			String NL = System.getProperty("line.separator");
			while ((line = inb.readLine()) != null) {
				sb.append(line).append(NL);
				Log(tag, "Read " + line);
			}
			inb.close();
			response = sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	/**
	 * Create a pandorabot.
	 * 
	 * @param botName
	 *            name of bot
	 * @since 0.0.1
	 */
	public void createBot(String botName) {
		Log(tag, "Create bot " + botName);
		try {
			String url = protocol + "//" + host + "/bot/" + appId + "/"
					+ botName;
			Log(tag, "url = " + url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("user_key", userKey));
			Log(tag, "entity = " + nameValuePairs);
			HttpPut request = new HttpPut();
			request.setURI(new URI(url));
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Delete a existing pandorabot.
	 * 
	 * @param botName
	 * @since 0.0.1
	 */
	public void deleteBot(String botName) {
		Log(tag, "Delete bot " + botName);
		try {
			String url = protocol + "//" + host + "/bot/" + appId + "/"
					+ botName + "?user_key=" + userKey;
			Log(tag, "url = " + url);
			HttpDeleteWithBody request = new HttpDeleteWithBody();
			request.setURI(new URI(url));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Compile a existing pandorabot.
	 * 
	 * @param botName
	 * @since 0.0.1
	 */
	public void compileBot(String botName) {
		Log(tag, "Compile bot " + botName);
		try {
			String url = protocol + "//" + host + "/bot/" + appId + "/"
					+ botName + "/" + "verify?user_key=" + userKey;
			Log(tag, "url = " + url);
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Simplest method to talk to a bot.
	 * 
	 * @param botName
	 * @param text
	 * @return text of bot's response
	 * @see #talk(String, String)
	 * @since 0.0.1
	 */
	public String talk(String botName, String text) {
		return debugBot(botName, "", text, false, false, false);
	}

	/**
	 * Talk to a bot as a specific client.
	 * 
	 * @param botName
	 * @param clientName
	 * @param text
	 * @return text of bot's response
	 * @see #talk(String, String, String)
	 * @since 0.0.1
	 */
	public String talk(String botName, String clientName, String text) {
		return debugBot(botName, clientName, text, false, false, false);
	}

	/**
	 * Most general version of talk method that returns detailed debugging
	 * information.
	 * 
	 * @param botName
	 *            name of bot
	 * @param clientName
	 *            name of client (optional)
	 * @param text
	 *            text for conversation
	 * @param reset
	 * @param trace
	 * @param recent
	 * @return text of bot's response
	 * @since 0.0.1
	 */
	public String debugBot(String botName, String clientName, String text,
			boolean reset, boolean trace, boolean recent) {
		Log(tag, "Talk " + botName + " \"" + text + "\"");
		String response = "";
		try {
			String url = protocol + "//" + host + "/talk/" + appId + "/"
					+ botName;
			Log(tag, "url = " + url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			if (sessionId.length() > 0)
				nameValuePairs.add(new BasicNameValuePair("sessionid",
						sessionId));
			if (clientName.length() > 0)
				nameValuePairs.add(new BasicNameValuePair("client_name",
						clientName));
			nameValuePairs.add(new BasicNameValuePair("input", text));
			nameValuePairs.add(new BasicNameValuePair("user_key", userKey));
			if (reset)
				nameValuePairs.add(new BasicNameValuePair("reset", "true"));
			if (trace)
				nameValuePairs.add(new BasicNameValuePair("trace", "true"));
			if (recent)
				nameValuePairs.add(new BasicNameValuePair("recent", "true"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			Log(tag, "entity = " + nameValuePairs);
			HttpPost request = new HttpPost();
			request.setURI(new URI(url));
			request.setEntity(entity);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			String jsonStringResponse = readResponse(httpResp);
			JSONObject jsonObj = new JSONObject(jsonStringResponse);
			JSONArray responses = jsonObj.getJSONArray("responses");
			for (int i = 0; i < responses.length(); i++)
				response += " " + responses.getString(i);
			response = response.trim();
			sessionId = jsonObj.getString("sessionid");
			Log(tag, "response = " + response);
			Log(tag, "sessionid = " + sessionId);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	/**
	 * Upload a file to a pandorabot.
	 * <p>
	 * URL form varies<br>
	 * <ul>
	 * <li>/bot/{appId}/{botName}/{fileKind} for pdefaults, properties.
	 * <li>/bot/{appId}/{botName}/{fileKind}/baseName for map, set,
	 * substitution.
	 * <li>/bot/{appId}/{botName}/file/baseName.extName for aiml.
	 * </ul>
	 * <p>
	 * 
	 * @param botName
	 *            name of bot
	 * @param pathName
	 *            path for upload file
	 * @since 0.0.1
	 */
	public void uploadFile(String botName, String pathName) {
		try {
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
			Log(tag, "Upload  File " + botName);
			Log(tag, "Basename = " + baseName);
			String url = protocol + "//" + host + "/bot/" + appId + "/"
					+ botName + "/" + fileKind
					+ (fileName != null ? "/" + fileName : "") + "?user_key="
					+ userKey;
			Log(tag, "url = " + url);
			HttpPut request = new HttpPut();
			request.setURI(new URI(url));
			byte[] bytes = Files.readAllBytes(Paths.get(pathName));
			StringEntity entity = new StringEntity(new String(bytes,
					Charset.defaultCharset()));
			request.setEntity(entity);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
