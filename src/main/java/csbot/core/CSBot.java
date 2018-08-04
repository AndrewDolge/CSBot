package csbot.core;


import org.slf4j.LoggerFactory;
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

        loadCommands();

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
     * loads the commands from the BotProperties.
     */
    private void loadCommands(){
        String[] commandNames = properties.getCommands();
        Command toAdd = null;

        if(commandNames != null){

            //for every command
            for(int i = 0; i < commandNames.length; i++){
                try{

                    toAdd = (Command) Class.forName(commandNames[i]).getConstructor().newInstance();
                    this.addCommand(toAdd);

                }catch(Exception e){
                    logger.warn("could not load command " + commandNames[i],e);
                }
                
            }//for

        }else{
            logger.warn("could not load commands, Property commandNames array is null.");
        }

        HelpCommand help = new HelpCommand();
        this.addCommand(help);

    }//loadCommands


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

            logger.debug("Command: (" + toAdd.getTrigger() + ") added!");
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
}//class

