import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Main class of CSBot
 * maintains the core functionality of the bot.
 * 
 * 
 */

public class CSBot extends ListenerAdapter{

    private JDA csJDA;
    private ThreadPoolExecutor commandExecutor;
    private ArrayList<Command> commandList;


    private CSBot(){
        
        commandList = new ArrayList<Command>();




    }//CSBot


    /**
     * called whenever a discord messsage appears in chat.
     * checks the event for a command, and calls the Message Processor to handle the message.
     * 
     */
    @Override 
    public void onMessageReceived(MessageReceivedEvent event){

    }//onMessageReceived






}//class

