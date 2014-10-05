import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by User on 7/10/2014.
 *
 * Global storage for Magic Parameters associated with PandorabotsAPI
 *
 * version - Program version
 * user_key - The User Key assigned by developer.pandorabots.com
 * app_id - The Application ID assigned by developer.pandorabots.com
 * hostname - The name of the Pandorabots host
 * debug - flag to indicate verbosity of output
 *
 */
public class MagicParameters {
    public static String version = "0.0.5";
    public static String user_key = "unknown";
    public static String app_id = "unknown";
    public static String hostname = "unknown";
    public static boolean debug = false;
    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);

    }
    static List<String> readLines(String path, Charset encoding)
            throws IOException
    {
        List<String> lines = Files.readAllLines(Paths.get(path), encoding);
        return lines;
    }

    /*

    Create a parameters file called config.txt in the directory where the Java program runs.

    The parameters file config.txt should contain one parameter per line, with the format
    parametername:value

    e.g.

user_key:f0123456789abcdef0123456789abcde
app_id:1234567890123
hostname:aiaas.pandorabots.com
debug:true

    The user_key and app_id are provided at developer.pandorabots.com
    as "User Key" and "Application ID respectively.

    */

    static void readParameters() {
        String cwd = System.getProperty("user.dir");
        readParameters(cwd+"/config.txt");
    }
    static void readParameters(String configFileName) {
        try {
            List<String> lines = readLines(configFileName, Charset.defaultCharset());
            for (String line : lines) {
                String[] pair = line.split(":");
                if (pair.length >= 2) {
                    if (pair[0].equals("user_key")) user_key = pair[1];
                    else if (pair[0].equals("app_id")) app_id = pair[1];
                    else if (pair[0].equals("hostname")) hostname = pair[1];
                    else if (pair[0].equals("debug")) {
                        if (pair[1].equals("true")) debug = true;
                        else debug = false;
                    }
                    //System.out.println("pair =" + pair[0] + "," + pair[1]);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
