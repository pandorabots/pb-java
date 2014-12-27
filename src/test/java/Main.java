/**
 * Main.java
 */

import com.pandorabots.api.MagicParameters;
import com.pandorabots.api.PandorabotsAPI;

/**
 * Sample Main.java class that tests the Pandorabots API.
 * <p>
 * Created by User on 6/26/2014.<br>
 * <ul>
 * <li>Create a bot named alice2 (cause an error if bot exists).
 * <li>Delete the bot (attempt to delete existing bot).
 * <li>Create the bot alice2 again.
 * <li>Upload some AIML files, a config file and an AIML Set file.
 * <li>Compile the bot.
 * <li>Talk to the bot as "user123".
 * </p>
 * 
 * @author Richard Wallace
 * @since 0.0.1
 */
public class Main {
	static String[] inputs = { "Hello", "How are you?", "name", "age",
			"gender", "Call me Richard", "my name?", "air force blue" };

	public static String botName = "yyconn2";

	public static void main(String[] args) {
		System.out.println("pb-java version " + MagicParameters.version);
		MagicParameters.readParameters();
		PandorabotsAPI papi = new PandorabotsAPI(MagicParameters.hostName,
				MagicParameters.appId, MagicParameters.userKey,
				MagicParameters.debug);
		System.out.println("Creating bot " + botName);
		papi.createBot(botName);
		System.out.println("Deleting bot " + botName);
		papi.deleteBot(botName);
		System.out.println("Creating bot " + botName);
		papi.createBot(botName);
		System.out.println("Upload file udc.aiml to " + botName);
		papi.uploadFile(botName, "bot/udc.aiml");
		System.out.println("Upload file bot_profile.aiml to " + botName);
		papi.uploadFile(botName, "bot/bot_profile.aiml");
		System.out.println("Upload file client_profile.aiml to " + botName);
		papi.uploadFile(botName, "bot/client_profile.aiml");
		System.out.println("Upload file alice2.properties to " + botName);
		papi.uploadFile(botName, "bot/alice2.properties");
		System.out.println("Upload file color.set to " + botName);
		papi.uploadFile(botName, "bot/color.set");
		System.out.println("Compiling bot " + botName);
		papi.compileBot(botName);
		System.out.println("Talk to " + botName);
		for (int i = 0; i < inputs.length; i++) {
			String request = inputs[i];
			String response = papi.talk(botName, "user123", request);
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