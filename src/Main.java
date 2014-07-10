/**
 * Created by User on 6/26/2014.
 */
public class Main {

    public static String username = "drwallace";
    public static String host = "aiaas.pandorabots.com";
    public static String botname = "alice2";

    public static String apikey = "41805b6ef707445649d149af6cfa93db"; // "95c3c5df117e2c35985c9f28b3e7c6ae";
    //

    static String[] inputs = {"Hello", "How are you?", "name", "age", "gender", "Call me Richard", "air force blue"};

    public static void main (String[] args) {
        System.out.println("pb-java version "+MagicStrings.version);
        PandorabotsAPIHttpClient papihc = new PandorabotsAPIHttpClient(host, username, apikey, true);
        System.out.println("Creating bot "+botname);
        papihc.createBot(botname);
        System.out.println("Deleting bot "+botname);
        papihc.deleteBot(botname);
        System.out.println("Creating bot "+botname);
        papihc.createBot(botname);
        System.out.println("Upload file udc.aiml to "+botname);
        papihc.uploadFile(botname, "c:/alice/udc.aiml");
        System.out.println("Upload file bot_profile.aiml to "+botname);
        papihc.uploadFile(botname, "c:/ab/bots/alice2/aiml/bot_profile.aiml");
        System.out.println("Upload file alice2.properties to "+botname);
        papihc.uploadFile(botname, "c:/ab/bots/alice2/config_pand/alice2.properties");
        System.out.println("Upload file color.set to "+botname);
        papihc.uploadFile(botname, "c:/ab/bots/alice2/sets_pand/color.set");
        System.out.println("Compiling bot "+botname);
        papihc.compileBot(botname);
        System.out.println("Talk to "+botname);
        for (int i = 0; i < inputs.length; i++) {
            String request = inputs[i];
            String response = papihc.talk(botname, request);
            System.out.println("Human: "+request);
            System.out.println("Robot: "+response);
        }
    }


}