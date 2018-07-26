package csbot.commands;

import java.util.Random;
import csbot.core.Command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class RollCommand extends Command {
	
	public RollCommand() {
		super("roll", 1); 
	}
	
	@Override
    /**
     * Retrieves the Help String for this command.
     * The Help string should include detailed instructions on how to use this command,
     * including all the different usages of this command.
     * 
     * @return The Help String of this command.
     */
	public String getHelpString() {
        return  "usage: !roll XdY\n" +
                "where X and Y are any integers between 1 and 100.\n" +
                "rolls X dice with values from 1 to Y. ";
	}
	
	@Override
    /**
     * Processes the current given Event.
     * 
     * @param event the incoming event that contains the message
     * 
     */
	public void process(MessageReceivedEvent event) {

        String toUserString = null;
        String[] message = event.getMessage().getContentRaw().split(" ");

        if(message.length == 2 && message[1].matches("\\d{1,3}d\\d{1,3}")){
            int num  =  Math.max( 1,Math.min(100, Integer.valueOf(message[1].split("d")[0])));
            int dice =  Math.max( 1,Math.min(100, Integer.valueOf(message[1].split("d")[1])));
            int total = 0;
            int roll = -1;

            Random rand = new Random();
        
            for(int i = 0; i < num; i++){  
                roll = rand.nextInt(dice) + 1;
                total = total + roll;
            }

            toUserString = "Rolling " + num + "d" + dice + ": "  + total; 

        }

        if(toUserString != null){
            Command.sendMessageWithMention(event, Command.formatText(toUserString));
        }
	}
	
	@Override
    /**
     * returns a brief description of this command.
     * This string should be less than 20 characters, and give the user a basic idea of what the command does.
     * 
     * @return a brief description of this command.
     */
	public String getDescription() {
		return "rolls XdY dice."; 
	}	
}