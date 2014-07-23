/**
 * Created by User on 6/26/2014.
 */
public class Main {
    static String[] inputs = {"Hello", "How are you?", "name", "age", "gender", "Call me Richard", "air force blue"};

    public static String botname = "alice2";

    public static void main (String[] args) {
        System.out.println("pb-java version "+ MagicParameters.version);
        MagicParameters.readParameters();
        PandorabotsAPI papi
                = new PandorabotsAPI(MagicParameters.hostname, MagicParameters.username, MagicParameters.userkey);
        System.out.println("Creating bot "+botname);
        papi.createBot(botname);
        System.out.println("Deleting bot "+botname);
        papi.deleteBot(botname);
        System.out.println("Creating bot "+botname);
        papi.createBot(botname);
        System.out.println("Upload file udc.aiml to "+botname);
        papi.uploadFile(botname, "c:/alice/udc.aiml");
        System.out.println("Upload file bot_profile.aiml to "+botname);
        papi.uploadFile(botname, "c:/ab/bots/alice2/aiml/bot_profile.aiml");
        System.out.println("Upload file alice2.properties to "+botname);
        papi.uploadFile(botname, "c:/ab/bots/alice2/config_pand/alice2.properties");
        System.out.println("Upload file color.set to "+botname);
        papi.uploadFile(botname, "c:/ab/bots/alice2/sets_pand/color.set");
        System.out.println("Compiling bot "+botname);
        papi.compileBot(botname);
        System.out.println("Talk to "+botname);
        for (int i = 0; i < inputs.length; i++) {
            String request = inputs[i];
            String response = papi.talk(botname, request);
            System.out.println("Human: "+request);
            System.out.println("Robot: "+response);
        }
        for (int i = 0; i < inputs.length; i++) {
            String request = inputs[i];
            String response = papi.debugBot(botname, request, false, true, false);
            System.out.println("Human: "+request);
            System.out.println("Robot: "+response);

        }
    }


}