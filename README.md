# pb-java

## Pandorabots API module for Java

### Usage

The simplest way to use the pb-java API is to include the classes
HttpDeleteWithBody, MagicParameters, and PandorabotsAPI in your Java project.

### Dependencies

The pb-java API depends on the following JAR files.  You may include
them in your project with Maven or Gradle, or simply download the JAR
files and link them with your project.

* commons-logging-1.1.1.jar
* httpclient-4.2.1.jar
* httpclient-cache-4.2.1.jar
* httpcore-4.2.1.jar
* httpmime-4.2.1.jar
* json-20090211.jar

#### Classes

The pb-java project includes several classes.

##### Main

The Main class is provided as an example of how to use the Java Pandorabots 
API.  You may wish to customize the code in PandorabotsAPI to suit your
application needs.  The code provided is intentended as an example only.

##### MagicParameters

The MagicParameters class encapsulates some global variables for your
application:


* static String version: the version number of the API.
* static String hostname: the hostname of the Pandorabots host
* static String username: your username on the Pandorabots host
* static String userkey: your Pandorabots API user key (available from developer.pandorabots.com)
* static boolean debug: A variable that tells the API whether to display debugging information.

MagicParameters also contains a pair of methods to read the global 
configuration data:

* static void readParameters()
* static void readParameters(String configFileName)

The first method looks for a file called config.txt in the current working
directory.  Use the second method if you want to specify a different file.

The parameters file config.txt should contain one parameter per line, with the format

```
parametername:value
```

e.g.

```
hostname:aiaas.pandorabots.com
username:drwallace
userkey:xxxxxxxxxxxxxxxxxxxxxxxx
debug:false
```

The userkey is provided at developer.pandorabots.com

##### HttpDeleteWithBody

##### PandorabotsAPI



