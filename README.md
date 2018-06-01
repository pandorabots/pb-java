# pb-java

Version 1.0.1

Last revised June 01,2018

## Pandorabots API module for Java

This project contains some sample Java code to access the Pandorabots API.
Use this project if:

* You want to connect your Java or Android project to Pandorabots.
* You have set up an account on developer.pandorabots.com and have an API key.

### Usage

The simplest way to use the pb-java API is to add pb-java-X.X.X.jar into CLASSPATH.
Most commonly, your Java project will simply connect to a pandorabot and talk to it. You can easily implement this with:

```
public static String botname = "MyBot";
MagicParameters.readParameters();
PandorabotsAPI papi = new PandorabotsAPI(MagicParameters.hostname, MagicParameters.app_id, MagicParameters.user_key);
       
```

and

```
String request = "Hello!  My name is Joe.";
String response = papi.talk(botname, request);
```

### Dependencies

The pb-java API depends on the following JAR files.
You may import this project with "Existing Maven Project" in Eclipse,
or use Gradle, or simply download the JAR files and link them with your project.

* commons-codec-1.6.jar
* commons-logging-1.2.jar
* commons-io-2.4.jar
* fluent-hc-4.4-beta1.jar
* httpclient-4.4-beta1.jar
* httpcore-4.4-beta1.jar
* json-20090211.jar

### Classes

The pb-java project includes several classes.

### Main

The Main class is provided as an example of how to use the Java Pandorabots 
API.  You may wish to customize the code in PandorabotsAPI to suit your
application needs.  The code provided is intentended as an example only.

### MagicParameters

The MagicParameters class encapsulates some global variables for your
application:


* `static String version`: the version number of the API.
* `static String hostname`: the hostname of the Pandorabots host
* `static String app_id`: your Applicato ID on the Pandorabots host
* `static String user_key`: your Pandorabots API user key (available from developer.pandorabots.com)
* `static boolean debug`: A variable that tells the API whether to display debugging information.

MagicParameters also contains a pair of methods to read the global 
configuration data:

* `static void readParameters()`
* `static void readParameters(String configFileName)`

### config.txt

The first method looks for a file called config.txt in the current working
directory.  Use the second method if you want to specify a different file.

Create config.txt file according to following format with one parameter per line.

```
parametername:value
```

e.g.

```
user_key:f0123456789abcdef0123456789abcde
app_id:1234567890123
hostname:aiaas.pandorabots.com
debug:true
```

The user_key and app_id are provided at developer.pandorabots.com
as "User Key" and "Application ID respectively.

### To talk to the bot

Currently pb-java v1.0.1 supports following talk and atalk functionalities

```
talk(botname, input)
talk(botname, clientId, input)
talk(botname, clientId, input, extra) //with recent calls always true 
atalk(botname,input)
atalk(botname,clientId,input)
atalk(botname,clientId,input,extra) //with recent calls always true 

```

talk
* @return text of bot's response<br>
* @return metadata response if extra set to true <br>
<br>

atalk<br>
* @return text of bot's response<br>
* @return newly generated clientId<br>
* @return metadata response if extra set to true 

### PandorabotsAPI

The class PandorabotsAPI is the core of the code to access the Pandorabots API.
Please refer Java DOC of Pandorabots API.

