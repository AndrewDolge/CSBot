// package csbot.core;

// import csbot.core.BotProperties;
// import csbot.core.CSBot;

// import java.io.File;
// import java.io.IOException;
// import java.nio.file.Files;
// import java.nio.file.Paths;
// import java.security.Policy;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

// public class Main {

//     private static Logger logger = LoggerFactory.getLogger("csbot.Main");
//     private static CSBot bot = null;

//     public static void main(String[] args) {

//         // add a shutdown hook to shut off the bot when the JVM is turned off.
//         Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
//             public void run() {
//                 logger.debug("attempting to shutdown bot...");
//                 try {
//                     if(bot != null){
//                         bot.shutdown();
//                         logger.debug("bot shutdown!");
//                     }else{
//                         logger.debug("bot reference is null");
//                     }
                    
                   
//                 } catch (Exception e) {

//                     logger.error("Exception occurred when shutting down bot.", e);
//                 }

//             }
//         }, "Bot-Shutdown-thread"));


//         Policy.setPolicy(new BotSecurityPolicy());
//         System.setSecurityManager(new SecurityManager());

//         BotProperties properties = null;
//         File propertyFile        = null;

//         try {

//             accessTest();
 
//             //parse the command line arguments.
//             if (args.length > 0) {
                
//                 propertyFile = new File(args[0]);
//             //attempt to access the property file from the directory of the jar
//             }else{
//                 logger.debug("Debug statement.");

               
//                 logger.debug("attempting to load properties file ");
//                 propertyFile =  new File("bot.properties");
//             }

//                 if (propertyFile != null && propertyFile.exists()) {
                    
//                     logger.debug("File path specified: " + propertyFile.getAbsolutePath());
//                     properties = new BotProperties(propertyFile);
//                     bot = new CSBot(properties);

//                     bot.start();

//                 } else {

//                     logger.error("no property file specified.");
//                 }


//         } catch (Exception e) {
//             logger.error("Exception occurred.", e);
//             if (bot != null) {
//                 bot.shutdown();
//             }
//         } // catch

//     }// main

//     public static void accessTest() throws IOException{
       
      
//         for(String line : Files.readAllLines(Paths.get("C:", "Users","Curious Sight","Desktop","Command.1.java"))){
//             System.out.println(line);
//         }

//     }

// }// class