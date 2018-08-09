package csbot.core;


import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.security.Policy;
import java.util.Iterator;
import java.util.ServiceLoader;

import org.slf4j.Logger;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Main class of CSBot
 * maintains the core functionality of the bot.
 * 
 */
public class CSBot extends ListenerAdapter{

    private BotProperties      properties;
    private JDA                csJDA;
    private boolean            running;
    private CommandManager     commandManager;
    private final String       prefix = "!";
    
 


    private static final Logger logger = LoggerFactory.getLogger("csbot.core.CSBot");
	private static final int DEFAULT_COOLDOWN = 1;
    
    
    public CSBot(BotProperties properties){
        
        this.properties      = properties;
        this.commandManager  = new CommandManager();
        this.running         = false;

        loadCommandsFromJars();
        HelpCommand help = new HelpCommand();
        this.addCommand(help);
        help.setDescriptionList(this.commandManager.getDescriptions());
        help.setMap(this.commandManager.getHelpText());

    }//CSBot


    /**
     * setter method for the running field
     */
    private void setRunning(boolean value){
        this.running = value;
    }//setRunning

    /**
     * getter method for the running field.
     */
    public boolean isRunning(){
        return this.running;
    }//isRunning

    /**
     * getter method for the prefix.
     */
    public String getPrefix(){
        return this.prefix;
    }//isRunning


    /**
     * Loads in commands from Jar files in the /plugins directory.
     * 
     * 
     */
    public void loadCommandsFromJars(){
       
        FilenameFilter jarFilter =  (dir, name) ->  name.endsWith(".jar");
     
        for(File jarFile : getPluginDirectory().listFiles(jarFilter)){

            try( CommandClassLoader commandLoader = new CommandClassLoader(jarFile.toURI().toURL());) {

                Class<?> commandClass = commandLoader.loadClass("RollCommand");
                Command toAdd = (Command) commandClass.getConstructor().newInstance();
            
                addCommand( toAdd);

            } catch (Exception e) {
                logger.error("CSBot.loadCommandsFromJars: exception while loading jar file:\n" + jarFile.toString(),e);
            }
        }//for jarFile
    }//loadCommandsFromJar

    /**
     * Starts up the bot.
     * Initializes the JDA object with the token, and waits for it to connect to discord.
     * 
     * @param token the token of the bot. Get this from discord.
     * @return true, if the bot started up without error, false otherwise.
     */
    public boolean start(){
        
        try{
            this.commandManager.start();

            csJDA = new JDABuilder(AccountType.BOT)
            .setToken(properties.getToken())
            .addEventListener(this)
            .buildBlocking();
        }catch(Exception e){

            e.printStackTrace();
            return false;
        }

        setRunning(true);
        return true;

    }//start

    /**
     * shuts down the bot.
     * 
     */
    public void shutdown(){
        
        csJDA.shutdownNow();
        this.commandManager.shutdown();
        
        setRunning(false);
    }//shutdown

    /**
     * Adds the command to the bot.
     * Each command must have a unique trigger.
     * If a command has the same trigger as a command that is already added, it will not be added.
     * 
     * @param command the command to be added
     * @return true, if the command was added, false otherwise
     * 
     */
    public boolean addCommand(Command toAdd){
        boolean added = this.commandManager.addCommand(toAdd, DEFAULT_COOLDOWN);

        if(added){

            logger.debug("Command: (" + toAdd.getTrigger() + ") added! ");
        }else{
            logger.warn("Command: (" + toAdd.getTrigger() + ") failed to add!");
        }

        return added;

    }//addCommand  


    /**
     * called whenever a discord messsage appears in chat.
     * checks the event for a command, and calls the Message Processor to handle the message.
     * 
     * Executes the appropriate command in its own thread.
     * 
     */
    @Override 
    public void onMessageReceived(MessageReceivedEvent event){

        String message = event.getMessage().getContentRaw();
        //check to see if the message starts with the prefix

        if(this.isRunning() && message.startsWith(prefix)  ){

            String trigger = message.split(" ")[0].substring(1);

            if(commandManager.contains(trigger)){
                commandManager.execute(event,trigger);
            }else{
                logger.error("CSBot.onMessageReceived: No Command found for message: " + message );
            }
        }//if running and prefix
    }//onMessageReceived

    public static File getApplicationDirectory(){
        try {
			return new File(CSBot.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			logger.error("CSBot.getApplicationDirectory: bad URI",e);
        }
        return null;
    }

    public static File getPluginDirectory(){
        try{
            File parent = getApplicationDirectory();
            File pluginDir = new File(parent.getAbsolutePath() + File.separatorChar + "plugins");
            
            return pluginDir;
        }catch(Exception e){
            logger.error("error accessing plugin directory",e);
        }

        return null;
    }


    public static File getPluginDataDirectory(){
        try{
            File parent = getApplicationDirectory();
            File pluginDir = new File(parent.getAbsolutePath() + File.separatorChar + "data");
            
            return pluginDir;
        }catch(Exception e){
            logger.error("error accessing plugin directory",e);
        }

        return null;
    }


}//class

