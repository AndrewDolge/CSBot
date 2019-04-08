package csbot.core;

import java.io.File;
import java.security.Policy;

public class Main{

    private static CSBot bot = null;
    private static CSBotLogger logger = new CSBotLogger("csbot.core.Main");

    public static void main(String[] args) throws Exception{

        //set the security manager to block dangerous operations by external jars
        Policy.setPolicy(new BotSecurityPolicy(CSBot.getPluginDataDirectory().getCanonicalPath()));
        System.setSecurityManager(new SecurityManager());  
        

        // add a shutdown hook to shut off the bot when the JVM is turned off.
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                logger.debug("attempting to shutdown bot...");
                try {
                    if(bot != null){
                        bot.shutdown();
                        logger.debug("bot shutdown!");
                    }else{
                        logger.debug("bot reference is null");
                    }
                    
                   
                } catch (Exception e) {

                   logger.error("Exception occurred when shutting down bot.", e);
                }

            }
        }, "Bot-Shutdown-thread")); 

        //Check to see if the prerequisite files are there

        if(CSBot.getPluginDirectory().exists() && CSBot.getPluginDataDirectory().exists()){

            File propertyFile = new File (CSBot.getApplicationDirectory().getPath() + "/bot.properties");

            if(propertyFile.exists()){
 
                BotPropertyLoader loader = new BotPropertyLoader(propertyFile);

                CSBot bot = new CSBot(loader);

                if(!bot.start()){
                    logger.debug("Bot failed to start. Shutting down.");
                    bot.shutdown();
                    System.exit(-1);
                }

            }else{
                //could not find bot.properties file.
                logger.error("Could not find bot.properties file at location:\n\t" + propertyFile.getPath() );
                CSBot.setupDirectory();

            }
        }else{
            //could not find plugin directories
            logger.warn(
                        "Could not find plugin directory at location: \n\t" + CSBot.getPluginDirectory() +
                        "\nand/or could not find plugin data directory at location: \n\t" + CSBot.getPluginDirectory()
            );
            CSBot.setupDirectory();
        }

    }//main



}