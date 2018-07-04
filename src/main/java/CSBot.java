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
 * 
 */

public class CSBot extends ListenerAdapter{

    private JDA                csJDA;
    private ExecutorService    commandExecutor;
    private ArrayList<Command> commandList;
    private boolean            running;
    

    //TODO: make a File to parse string to text to get token
    //TODO: make this class singular
    private CSBot(){
        
        commandList = new ArrayList<Command>();
        commandExecutor = Executors.newCachedThreadPool(); 


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
     * Starts up the bot.
     * Initializes the JDA object with the token, and waits for it to connect to discord.
     * 
     * @param token the token of the bot. Get this from discord.
     * @return true, if the bot started up without error, false otherwise.
     */
    public boolean start(String token ){
    
        try{
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
        
        setRunning(false);
    }//shutdown

    /**
     * called whenever a discord messsage appears in chat.
     * checks the event for a command, and calls the Message Processor to handle the message.
     * 
     * Executes the appropriate command in its own thread.
     * 
     */
    @Override 
    public void onMessageReceived(MessageReceivedEvent event){

        Command toExecute = findCommand(event.getMessage().getContentRaw());
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
    }//onMessageReceived

    /**
     * gets the appropriate command whose trigger exists in the given message.
     * 
     * @param message the message from the discord channel
     * @return the Command whose trigger exists within the message, null otherwise
     * 
     */
    private Command findCommand(String message){

        try{
            String[] split = message.split(" ");

            for(Command command: this.commandList){
                if(command.getTrigger().compareTo(split[0]) == 0) {
                    return command;
                }

            }
        
        }catch(Exception e){
            return null;
        }
       
        return null;
    }//findCommand


}//class

