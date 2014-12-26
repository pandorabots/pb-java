/**
 * PandorabotsAPI.java
 */
package com.pandorabots.api;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
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
 * @author Richard Wallace
 * 
 *         Pandorabots API Class Created by User on 6/25/2014.
 * 
 *         Implements methods to access Pandorabots API:
 * 
 *         Log(TAG, x) - Print out debuggining info PandorabotsAPI(host, app_id,
 *         user_key) - Constructor readResponse(httpResponse) - read response
 *         from Pandorabots server createBot(botname) - create a pandorabot.
 *         deleteBot(botname) - delete a pandorabot. uploadFile(botname,
 *         filename) - upload a file to a pandorabot. compileBot(botname) -
 *         compile a pandorabot. talk(botname, input) - talk to a bot.
 *         talk(botname, client_name, input) - talk to a bot as a specific
 *         client. debugBot(botname, client_name, input, reset, trace, recent) -
 *         more general version of talk method that returns detailed debugging
 *         information.
 * 
 *         See: https://developer.pandorabots.com/docs
 * 
 */
public class PandorabotsAPI {
	private String host = "";
	private String user_key = "";
	private String app_id = "";
	private String sessionid = "";
	private String TAG = "PandorabotsAPI";
	private String protocol = "https:";

	private void Log(String TAG, String x) {
		if (MagicParameters.debug)
			System.out.println(TAG + ": " + x);
	}

	public PandorabotsAPI(String host, String app_id, String user_key) {
		this.host = host;
		this.app_id = app_id;
		this.user_key = user_key;
	}

	public String readResponse(HttpResponse httpResp) {
		String response = "";
		try {
			int code = httpResp.getStatusLine().getStatusCode();
			Log(TAG, "Response code = " + code);
			InputStream is = httpResp.getEntity().getContent();
			BufferedReader inb = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder("");
			String line;
			String NL = System.getProperty("line.separator");
			while ((line = inb.readLine()) != null) {
				sb.append(line).append(NL);
				Log(TAG, "Read " + line);
			}
			inb.close();
			response = sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	public void createBot(String botname) {
		Log(TAG, "Create bot " + botname);
		try {
			String url = protocol + "//" + host + "/bot/" + app_id + "/"
					+ botname;
			Log(TAG, "url = " + url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("user_key", user_key));
			Log(TAG, "entity = " + nameValuePairs);
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

	public void deleteBot(String botname) {
		Log(TAG, "Delete bot " + botname);
		try {
			String url = protocol + "//" + host + "/bot/" + app_id + "/"
					+ botname + "?user_key=" + user_key;
			Log(TAG, "url = " + url);
			HttpDeleteWithBody request = new HttpDeleteWithBody();
			request.setURI(new URI(url));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void compileBot(String botname) {
		Log(TAG, "Compile bot " + botname);
		try {
			String url = protocol + "//" + host + "/bot/" + app_id + "/"
					+ botname + "/" + "verify?user_key=" + user_key;
			Log(TAG, "url = " + url);
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String talk(String botname, String input) {
		return debugBot(botname, "", input, false, false, false);
	}

	public String talk(String botname, String client_name, String input) {
		return debugBot(botname, client_name, input, false, false, false);
	}

	public String debugBot(String botname, String client_name, String input,
			boolean reset, boolean trace, boolean recent) {
		Log(TAG, "Talk " + botname + " \"" + input + "\"");
		String response = "";
		try {
			String url = protocol + "//" + host + "/talk/" + app_id + "/"
					+ botname;
			Log(TAG, "url = " + url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			if (sessionid.length() > 0)
				nameValuePairs.add(new BasicNameValuePair("sessionid",
						sessionid));
			if (client_name.length() > 0)
				nameValuePairs.add(new BasicNameValuePair("client_name",
						client_name));
			nameValuePairs.add(new BasicNameValuePair("input", input));
			nameValuePairs.add(new BasicNameValuePair("user_key", user_key));
			if (reset)
				nameValuePairs.add(new BasicNameValuePair("reset", "true"));
			if (trace)
				nameValuePairs.add(new BasicNameValuePair("trace", "true"));
			if (recent)
				nameValuePairs.add(new BasicNameValuePair("recent", "true"));
			HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			Log(TAG, "entity = " + nameValuePairs);
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
			sessionid = jsonObj.getString("sessionid");
			Log(TAG, "response = " + response);
			Log(TAG, "sessionid = " + sessionid);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}

	// Upload file /bot/{appid}/{botname}/{file-kind}/{filename
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
			Log(TAG, "Upload  File " + botName);
			Log(TAG, "Basename = " + baseName);
			String url = protocol + "//" + host + "/bot/" + app_id + "/"
					+ botName + "/" + fileKind
					+ (fileName != null ? "/" + fileName : "") + "?user_key="
					+ user_key;
			Log(TAG, "url = " + url);
			HttpPut request = new HttpPut();
			request.setURI(new URI(url));
			String data = MagicParameters.readFile(pathName,
					Charset.defaultCharset());
			StringEntity entity = new StringEntity(data);
			request.setEntity(entity);
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpResponse httpResp = httpClient.execute(request);
			readResponse(httpResp);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
