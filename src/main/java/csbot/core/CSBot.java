package csbot.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService    commandExecutor;
    private ArrayList<Command> commandList;
    private boolean            running;
    private String             prefix;
    
 


    private static final Logger logger = LoggerFactory.getLogger("CSBot");
    
    
    public CSBot(BotProperties properties){
        
        this.properties      = properties;
        this.commandList     = new ArrayList<Command>();
        this.running         = false;
        this.prefix          = "!";

        loadCommands();

    }//CSBot


    /**
     * setter method for the running field
     */
    public void setRunning(boolean value){
        this.running = value;
    }//setRunning

    /**
     * getter method for the running field.
     */
    public boolean isRunning(){
        return this.running;
    }//isRunning

    /**
     * setter method for the prefix.
     */
    public void setPrefix(String prefix){
        this.prefix = prefix;
    }//setRunning

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

        this.addCommand(new HelpCommand(1, this));

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
            commandExecutor = Executors.newCachedThreadPool(); 

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
        
        csJDA.shutdown();
        commandExecutor.shutdown();
        commandList.clear();
        
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

        //compare this command to all other commands, checking to see if there is a trigger conflict
        for(Command command: this.commandList){

            if(command.compareTo(toAdd) == 0){
               logger.warn("CSBot.addCommand: Command has same trigger as existing Command. Failed to add Command.");
                return false;
            }
        }

        this.commandList.add(toAdd);
        logger.debug("CSBot.addCommand: Command " + toAdd.getTrigger() + " added!");
        
        return true;

    }//addCommand  


    /**
     * gets the help string from a command with the given commandTrigger
     * 
     * @param commandTrigger a string containing the desired command's trigger.
     * @return the help string for the desired command
     * 
     */
    public String getHelp(String commandTrigger){
        
        return this.findCommand(commandTrigger).getHelpString();
    }//getDescription

    /**
     * returns a string containing a list of all this bot's command descriptions.
     * 
     * @return a string containing all of this bot's command descriptions.
     */
    public String getDescriptionList(){

        StringBuilder sb = new StringBuilder();

        for(Command command: commandList){
            sb.append(command.getTrigger() + ": " + command.getDescription() + "\n");
        }

        return sb.toString();
    }//getList

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

        if(message.startsWith(prefix) ){

            //find the appropriate command
            Command toExecute = findCommand(message.split(" ")[0].substring(1));
            logger.debug("CSBot.onMessageReceived: Executing Command "+ toExecute.getTrigger());

            if(isRunning() && toExecute != null){
                commandExecutor.execute(
                    //create a new Runnable implementation. Runs on it's own thread.
                    new Runnable(){
                        //method called when the Runnable is executed.
                        public void run(){
                            
                            try{
                                //execute the command
                                if(!toExecute.isOnCooldown()){
                                    toExecute.process(event);
                                    toExecute.startCooldown();
                                }
                                
                            
                            }catch(Exception e){
                                e.printStackTrace();
                                logger.error("CSBot.onMessageReceived: Command " + toExecute.getTrigger() + " threw exception", e);
                                
                            }//catch
                        }//run
                    }//Runnable
                );//execute
    
            }else{
          
             logger.error("CSBot.onMessageReceived: No Command found!");
            }//ifRunning

        }//if prefix

    }//onMessageReceived

    /**
     * gets the appropriate command that matches the given command trigger
     * 
     * @param commandTrigger the trigger of the desired command
     * @return the Command whose trigger exists within the message, null otherwise
     * 
     */
    public Command findCommand(String commandTrigger){
        Command result = null;

        try{
    
            for(Command command: this.commandList){
                //compares each command's trigger to 

                if(command.getTrigger().equals(commandTrigger)) {
                   result = command;
                }

            }
        
        }catch(Exception e){
         e.printStackTrace();
        }
       
        return result;
    }//findCommand


}//class

