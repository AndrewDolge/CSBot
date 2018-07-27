package csbot.commands;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import csbot.core.Command;

public class PingCommand extends Command {
	
	public PingCommand() {
		super("ping", 1); 
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
		return "pings the server, then tells the user \"ping!\".";
	}
	
	@Override
    /**
     * Processes the current given Event.
     * The text of the message can be found by calling getMessage().getContentRaw().
     * 
     * This method is also responsible for responding to the user by queueing a message in the
     * channel this message was received in.
     * 
     * @param event the incoming event that contains the message
     * 
     */
	public void process(MessageReceivedEvent event) {
		event.getTextChannel().sendMessage("ping!").queue(); 
		return; 
	}
	
	@Override
    /**
     * returns a brief description of this command.
     * This string should be less than 20 characters, and give the user a basic idea of what the command does.
     * 
     * @return a brief description of this command.
     */
	public String getDescription() {
		return "pings the server."; 
	}	
}