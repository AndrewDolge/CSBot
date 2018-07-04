import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final boolean DEBUG = true;
    private JDA                csJDA;
    private ExecutorService    commandExecutor;
    private ArrayList<Command> commandList;
    private boolean            running;
    private String             prefix;

    private static final CSBot instance = new CSBot();
    
    private CSBot(){
        
        commandList     = new ArrayList<Command>();
        running         = false;
        prefix          = "!";

    }//CSBot

    /**
     * Singleton pattern method for CSBot.
     * 
     * 
     */
    public static CSBot getInstance(){
        return instance;
    }//getInstance

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
     * Starts up the bot.
     * Initializes the JDA object with the token, and waits for it to connect to discord.
     * 
     * @param token the token of the bot. Get this from discord.
     * @return true, if the bot started up without error, false otherwise.
     */
    public boolean start(String token ){
    
        try{
            commandExecutor = Executors.newCachedThreadPool(); 

            csJDA = new JDABuilder(AccountType.BOT)
            .setToken(token)
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
                //TODO: Log command add failure
                return false;
            }
        }

            this.commandList.add(toAdd);
            //TODO: log command add success
            return true;

    }//addCommand  

    /**
     * adds a set of standard commands to the bot.
     * 
     * you should place addCommands here!
     * 
     * 
     */
    public void addStandardCommands(){

        this.addCommand(new PingCommand("ping", 3));


    }//addStandardCommands


    /**
     * gets the help string from a command with the given commandTrigger
     * 
     * @param commandTrigger a string containing the desired command's trigger.
     * @return the help string for the desired command
     * 
     */
    protected String getHelp(String commandTrigger){
        
        return this.findCommand(commandTrigger).getHelpString();
    }//getDescription

    /**
     * returns a string containing a list of all this bot's command descriptions.
     * 
     * @return a string containing all of this bot's command descriptions.
     */
    protected String getDescriptionList(){

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

        if(DEBUG){System.out.println("starts with prefix: " + message.startsWith(prefix));}
        if(DEBUG){System.out.println("length: " + Boolean.valueOf(message.split(" ")[0].length() > 1));}

        if(message.startsWith(prefix) && message.split(" ")[0].length() > 1 ){

            //find the appropriate command
            Command toExecute = findCommand(message.split(" ")[0].substring(1));

            if(isRunning() && toExecute != null){
                commandExecutor.execute(
                    //create a new Runnable implementation. Runs on it's own thread.
                    new Runnable(){
                        //method called when the Runnable is executed.
                        public void run(){
                            
                            try{
                                //execute the command
                                toExecute.process(event);
                            
                            }catch(Exception e){
                                e.printStackTrace();
                                //TODO: Log command exceptions
                            }//catch
                        }//run
                    }//Runnable
                );//execute
    
            }else{
             //TODO: Log failed command
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
    private Command findCommand(String commandTrigger){
        Command result = null;

        if(DEBUG){System.out.println("command to find:" + commandTrigger );}
        try{
            if(DEBUG){System.out.println("command list size: " + this.commandList.size() );}
            for(Command command: this.commandList){
                //compares each command's trigger to 
                if(DEBUG){System.out.println("command in list:" + command.getTrigger() );}

                if(command.getTrigger().equals(commandTrigger)) {
                    if(DEBUG){System.out.println("found match: " + command.getTrigger() +":"+ commandTrigger);}
                   result = command;
                }

            }
        
        }catch(Exception e){
         e.printStackTrace();
        }
       
        if(DEBUG){System.out.println("command to return: " + result);}
        return result;
    }//findCommand


}//class

