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
 * <li>Download color.set file.
 * <li>Remove color.set file.
 * <li>Upload color.set file.
 * <li>Compile the bot again.
 * <li>Get properties of the bot.
 * <li>Get ZIP of the bot.
 * <li>Talk to the bot as "user123".
 * </p>
 * 
 * @author Richard Wallace
 * @since 0.0.1
 */
public class Main {
	static String[] inputs = { "Hello", "How are you?", "name", "age",
			"gender", "Call me Richard", "my name?", "air force blue" };

	public static String botName = "alice2";

	public static void main(String[] args) {
		MagicParameters mp = new MagicParameters();
		System.out.println("pb-java version " + mp.getVersion());
		PandorabotsAPI papi = new PandorabotsAPI(mp.getHostName(),
				mp.getAppId(), mp.getUserKey(), mp.isDebug());
		String response;
		try {
			System.out.println("Creating bot " + botName);
			response = papi.createBot(botName);

			System.out.println("Deleting bot " + botName);
			response = papi.deleteBot(botName);

			System.out.println("Creating bot " + botName);
			response = papi.createBot(botName);

			System.out.println("Upload file udc.aiml to " + botName);
			response = papi.uploadFile(botName, "bot/udc.aiml");

			System.out.println("Upload file bot_profile.aiml to " + botName);
			response = papi.uploadFile(botName, "bot/bot_profile.aiml");

			System.out.println("Upload file client_profile.aiml to " + botName);
			response = papi.uploadFile(botName, "bot/client_profile.aiml");

			System.out.println("Upload file alice2.properties to " + botName);
			response = papi.uploadFile(botName, "bot/alice2.properties");

			System.out.println("Upload file color.set to " + botName);
			response = papi.uploadFile(botName, "bot/color.set");

			System.out.println("Compiling bot " + botName);
			response = papi.compileBot(botName);

			System.out.println("Download file color.set of " + botName);
			papi.downloadFile(botName, "color.set");

			System.out.println("Remove file color.set of " + botName);
			response = papi.removeFile(botName, "color.set");

			System.out.println("Upload file color.set to " + botName);
			response = papi.uploadFile(botName, "bot/color.set");

			System.out.println("Compiling bot " + botName);
			response = papi.compileBot(botName);

			System.out.println("Get properties of bot " + botName);
			response = papi.getProperties(botName);
			System.out.println("Property: " + response);

			System.out.println("Get ZIP of bot " + botName);
			papi.getZip(botName);

			System.out.println("aTalk to " + botName);
			for (int i = 0; i < inputs.length; i++) {
				String request = inputs[i];
				response = papi.atalk(botName, request);
				System.out.println("Human: " + request);
				System.out.println("Robot: " + response);
			}
			
			System.out.println("Talk to " + botName);
			for (int i = 0; i < inputs.length; i++) {
				String request = inputs[i];
				response = papi.talk(botName, "user123", request);
				System.out.println("Human: " + request);
				System.out.println("Robot: " + response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}