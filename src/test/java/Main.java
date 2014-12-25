/**
 * Main.java
 */

import com.pandorabots.api.MagicParameters;
import com.pandorabots.api.PandorabotsAPI;

/**
 * @author Richard Wallace
 * 
 *         Sample Main.java class that tests the Pandorabots API.
 * 
 *         1. Create a bot named alice2. 2. Delete the bot (testing delete) 3.
 *         Create the bot alice2 again. 4. Upload some AIML files, a config file
 *         and an AIML Set file. 5. Compile the bot 6. Talk to the bot as
 *         "user123".
 * 
 *         Created by User on 6/26/2014.
 */
public class Main {
	static String[] inputs = { "Hello", "How are you?", "name", "age",
			"gender", "Call me Richard", "my name?", "air force blue" };

	public static String botname = "yyconn2";

	public static void main(String[] args) {
		System.out.println("pb-java version " + MagicParameters.version);
		MagicParameters.readParameters();
		PandorabotsAPI papi = new PandorabotsAPI(MagicParameters.hostname,
				MagicParameters.app_id, MagicParameters.user_key);
		System.out.println("Creating bot " + botname);
		papi.createBot(botname);
		System.out.println("Deleting bot " + botname);
		papi.deleteBot(botname);
		System.out.println("Creating bot " + botname);
		papi.createBot(botname);
		System.out.println("Upload file udc.aiml to " + botname);
		papi.uploadFile(botname, "bot/udc.aiml");
		System.out.println("Upload file bot_profile.aiml to " + botname);
		papi.uploadFile(botname, "bot/bot_profile.aiml");
		System.out.println("Upload file client_profile.aiml to " + botname);
		papi.uploadFile(botname, "bot/client_profile.aiml");
		System.out.println("Upload file alice2.properties to " + botname);
		papi.uploadFile(botname, "bot/alice2.properties");
		System.out.println("Upload file color.set to " + botname);
		papi.uploadFile(botname, "bot/color.set");
		System.out.println("Compiling bot " + botname);
		papi.compileBot(botname);
		System.out.println("Talk to " + botname);
		for (int i = 0; i < inputs.length; i++) {
			String request = inputs[i];
			String response = papi.talk(botname, "user123", request);
			System.out.println("Human: " + request);
			System.out.println("Robot: " + response);
		}
		/*
		 * for (int i = 0; i < inputs.length; i++) { String request = inputs[i];
		 * String response = papi.debugBot(botname, request, false, true,
		 * false); System.out.println("Human: "+request);
		 * System.out.println("Robot: "+response);
		 * 
		 * }
		 */
	}
}