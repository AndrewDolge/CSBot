

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ListCommand extends Command {
	
	public ListCommand(String trigger, int cooldown) {
		super(trigger, cooldown); 
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
		return "provides a list of all commands and their descriptions for this bot.";
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
        String toUserString = null;
        String message = event.getMessage().getContentRaw();

        if(message.split(" ").length == 1){
            toUserString = CSBot.getInstance().getDescriptionList();

        }

        if(toUserString != null){
            Command.sendMessageWithMention(event, Command.formatText(toUserString));
        }
	}//process
	
	@Override
    /**
     * returns a brief description of this command.
     * This string should be less than 20 characters, and give the user a basic idea of what the command does.
     * 
     * @return a brief description of this command.
     */
	public String getDescription() {
		return "lists all commands."; 
	}	
}