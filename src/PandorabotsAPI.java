import org.apache.commons.logging.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by User on 6/25/2014.
 */
public class PandorabotsAPI {

    private String host = "";
    private String userkey = "";
    private String username = "";
    private String sessionid = "";
    private String TAG = "PandorabotsAPI";

    private void Log(String TAG, String x) {
        if (MagicParameters.debug) System.out.println(TAG+": "+x);
    }

    public PandorabotsAPI(String host, String username, String userkey) {
        this.host = host;
        this.username = username;
        this.userkey = userkey;
    }

    public String readResponse (HttpResponse httpResp) {
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
            HttpClient client = new DefaultHttpClient();
            String url = "http://" + host + "/bot/" + username + "/" + botname;
            Log(TAG, "url = " + url);
            HttpPut request = new HttpPut();
            request.setURI(new URI(url));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("user_key",
                    userkey));
            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
            Log(TAG, "entity = " + nameValuePairs);
            request.setEntity(entity);
            HttpResponse httpResp = client.execute(request);
            readResponse(httpResp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public void deleteBot(String botname) {
        Log(TAG, "Delete bot " + botname);
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://" + host + "/bot/" + username + "/" + botname+"?user_key="+userkey;
            Log(TAG, "url = " + url);
            HttpDeleteWithBody request = new HttpDeleteWithBody();
            request.setURI(new URI(url));
            HttpResponse httpResp = client.execute(request);
            readResponse(httpResp);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

   public void compileBot(String botname) {
        Log(TAG, "Compile bot " + botname);
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://" + host + "/bot/" + username + "/" + botname+"/"+"verify?user_key="+userkey;
            Log(TAG, "url = " + url);
            HttpGet request = new HttpGet();
            request.setURI(new URI(url));
            HttpResponse httpResp = client.execute(request);
            readResponse(httpResp);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // curl -v  -X POST "http://aiaas.pandorabots.com/talk/drwallace/alice2" -d 'input=hello&user_key=41805b6ef707445649d149af6cfa93db'
    public String talk(String botname, String input) {
        Log(TAG, "Talk " + botname + " \"" + input + "\"");
        String response = "";
        try {
            HttpClient client = new DefaultHttpClient();
            String url = "http://" + host + "/talk/" + username + "/" + botname;
            Log(TAG, "url = " + url);
            HttpPost request = new HttpPost();
            request.setURI(new URI(url));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            if (sessionid.length() > 0) nameValuePairs.add(new BasicNameValuePair("sessionid", sessionid));
            nameValuePairs.add(new BasicNameValuePair("input", input));
            nameValuePairs.add(new BasicNameValuePair("user_key", userkey));
            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
            Log(TAG, "entity = " + nameValuePairs);
            request.setEntity(entity);
            HttpResponse httpResp = client.execute(request);
            String jsonStringResponse = readResponse(httpResp);
            JSONObject jsonObj = new JSONObject(jsonStringResponse);
            JSONArray responses = jsonObj.getJSONArray("responses");
            for (int i = 0; i < responses.length(); i++) response += " "+responses.getString(i);
            response = response.trim();
            sessionid = jsonObj.getString("sessionid");
            Log(TAG, "response = " + response);
            Log(TAG, "sessionid = " + sessionid);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response;


    }
    // Upload file /bot/{username}/{botname}/{file-kind}/{filename
    public void uploadFile(String botname, String filename) {
        String fileType = "file";
        boolean includeFileName = true;
        try {
            File file = new File(filename);
            String basename = file.getName();
            if (filename.contains("substitution")) {fileType = "substitution"; basename = filename.substring(0, basename.lastIndexOf('.'));}
            else if (filename.contains(".properties")) {fileType = "properties"; includeFileName = false;}
            else if (filename.contains(".pdefaults")) {fileType = "pdefaults"; includeFileName = false;}
            else if (filename.contains(".map")) {fileType = "map"; basename = basename.substring(0, basename.lastIndexOf('.'));}
            else if (filename.contains(".set")) {fileType = "set"; basename = basename.substring(0, basename.lastIndexOf('.'));}
            Log(TAG, "Upload  File " + botname);
            Log(TAG, "Basename = "+basename);
            HttpClient client = new DefaultHttpClient();
            String url = "http://" + host + "/bot/" + username + "/" + botname +"/"+fileType+(includeFileName ? "/"+basename : "")+"?user_key="+userkey;
            Log(TAG, "url = " + url);
            HttpPut request = new HttpPut();
            request.setURI(new URI(url));
            String data = MagicParameters.readFile(filename, Charset.defaultCharset());
            StringEntity entity = new StringEntity(data);
            request.setEntity(entity);
            HttpResponse httpResp = client.execute(request);
            readResponse(httpResp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }



    }


}

