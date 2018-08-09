package csbot.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Policy;




public class Main{

    private static CSBot bot = null;

    public static void main(String[] args) throws Exception{

        Policy.setPolicy(new BotSecurityPolicy());
        System.setSecurityManager(new SecurityManager());  
        
        new File("/bot.properties");

                 // add a shutdown hook to shut off the bot when the JVM is turned off.
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
               // logger.debug("attempting to shutdown bot...");
                try {
                    if(bot != null){
                        bot.shutdown();
                      //  logger.debug("bot shutdown!");
                    }else{
                       // logger.debug("bot reference is null");
                    }
                    
                   
                } catch (Exception e) {

                   // logger.error("Exception occurred when shutting down bot.", e);
                }

            }
        }, "Bot-Shutdown-thread")); 

        URL pathToJar = Main.class.getProtectionDomain().getCodeSource().getLocation();

        File file = new File(pathToJar.toURI()).getParentFile();
        File pluginDirectory = new File(file.getAbsolutePath() + File.separator +"plugins");


        System.out.println("plugin dir: " + pluginDirectory);
        System.out.println("plugin dir exists: " + pluginDirectory.exists());
        System.out.println("URL toString: " + pathToJar.toString());
        System.out.println("URI getPath: " + pathToJar.toURI().getPath());
		System.out.println("file get absPath: " + file.getAbsolutePath());
        System.out.println("isDirectory: " + file.isDirectory() );
        System.out.println("parent file" + file.getParentFile());

        File propertyFile = new File (CSBot.getApplicationDirectory().getPath() + "/bot.properties");

        BotProperties properties = new BotProperties(propertyFile);
        CSBot bot = new CSBot(properties);

        bot.start();

     

    }//main



}