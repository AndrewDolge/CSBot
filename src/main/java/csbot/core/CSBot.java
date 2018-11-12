package csbot.core;


import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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

    private JDA                csJDA;
    private boolean            running;
    private CommandManager     commandManager;
    private final String       prefix = "!";
    private BotPropertyLoader loader;
  

    private static final CSBotLogger logger = new CSBotLogger("csbot.core.CSBot");
    private static final int DEFAULT_COOLDOWN = 1;

    
    public CSBot(BotPropertyLoader loader){

        this.loader = loader;
        this.commandManager  = new CommandManager();
        this.running         = false;
       

        loadCommandsFromJars();
        HelpCommand help = new HelpCommand();
        this.addCommand(help,"");
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
     
        for(File jFile : getPluginDirectory().listFiles(jarFilter)){

            try( CommandClassLoader commandLoader = new CommandClassLoader(jFile.toURI().toURL());
                 JarFile jarFile = new JarFile(jFile);) {

               
                //Gets the File of this plugins assigned data directory
                File dataDir = new File(CSBot.getPluginDataDirectory().getCanonicalPath() +  File.separatorChar 
                +  jFile.toPath().getFileName().toString().substring(0, jFile.toPath().getFileName().toString().lastIndexOf(".")));

                  

                logger.debug("File data path: " + dataDir.toString());
                //create the directory if it doesn't exist
                if(!dataDir.exists()){
                    dataDir.mkdir();
                }

                //get the InputStream for the Configuration file
                InputStream inputStream = commandLoader.getResourceAsStream("Command.properties");

                //get the list of commands listed in the properties file
                CommandPropertyLoader propertyLoader = new CommandPropertyLoader(inputStream);

                //print out the plugin information
                logger.debug("Loading Plugin: "  +  propertyLoader.getName() +" Version: " + propertyLoader.getVersion() + "  by " + propertyLoader.getAuthor() ); 
                String[] commands = propertyLoader.getCommands();

              
                //for every entry in the jar file
                Enumeration<JarEntry> classes = jarFile.entries();
                while(classes.hasMoreElements()){
                    JarEntry current = classes.nextElement();
                    
                    //if it is a class file, attempt to load it
                    if(current.getName().endsWith(".class")){
                        logger.debug("attempting to load: " + current.getName().substring(0,current.getName().length() - 6).replace("/","."));

                        commandLoader.loadClass(current.getName().substring(0,current.getName().length() - 6).replace("/","."));
                    }
                }
               
                //for every command listed in the jar property file
                for(String command : commands){
  
                    try{
                        
                        

                        Command toAdd = (Command) commandLoader.loadClass(command).getConstructor().newInstance();
                        toAdd.initialize( dataDir.toString() + File.separatorChar);
                        addCommand(toAdd, dataDir.toString() + File.separatorChar);

                    }catch(Exception e){
                        logger.error("Could not load Command: " + command + "From file: " + jFile.getName(), e);
                    }  
                }//for each command
                
            } catch (Exception e) {
               logger.error("CSBot.loadCommandsFromJars: exception while loading jar file:\n" + jFile.toString(),e);
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
            String token = loader.getToken();
            this.commandManager.start();

            csJDA = new JDABuilder(AccountType.BOT)
            .setToken(token)
            .addEventListener(this)
            .buildBlocking();
        }catch(Exception e){

            logger.error("Exception thrown while starting bot", e);
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
    public boolean addCommand(Command toAdd, String dataDir){
        //add the command
        boolean added = this.commandManager.addCommand(toAdd,
                                                       dataDir,
                                                       loader.getCooldown(toAdd.getTrigger(), DEFAULT_COOLDOWN));
        //report if the command was added
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

    /**
     * returns a file representation of the directory where the CSBot.jar is executing
     * 
     */
    public static File getApplicationDirectory(){
        try {
			return new File(CSBot.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
		} catch (URISyntaxException e) {
			logger.error("CSBot.getApplicationDirectory: bad URI",e);
        }
        return null;
    }

    /**
     * returns a file representation of the directory where the plugins jars are stored.
     * 
     */
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

    /**
     * returns a file representation of the directory where the data subdirectories are stored.
     */
    public static File getPluginDataDirectory(){
        File result = null;
        try{
           
            result = new File(CSBot.getApplicationDirectory().getPath() + File.separatorChar + "data" + File.separatorChar);

        }catch(Exception e){
            throw new RuntimeException("error accessing plugin directory",e);
        }

        return result;
    }//File

}//class

