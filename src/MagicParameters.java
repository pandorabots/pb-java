import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by User on 7/10/2014.
 */
public class MagicParameters {
    public static String version = "0.0.4";
    public static String userkey = "unknown";
    public static String username = "unknown";
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
    The parameters file config.txt should contain one parameter per line, with the format
    parametername:value

    e.g.

    hostname:aiaas.pandorabots.com
    username:drwallace
    userkey:xxxxxxxxxxxxxxxxxxxxxxxx
    debug:false

    The userkey is provided at developer.pandorabots.com

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
                    if (pair[0].equals("userkey")) userkey = pair[1];
                    else if (pair[0].equals("username")) username = pair[1];
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
