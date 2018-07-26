package csbot.core;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class HelpCommand extends Command {
    
    private CSBot bot;

	public HelpCommand( int cooldown, CSBot bot) {
        super("help", cooldown); 
        this.bot = bot;
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
        return  "usage: !help <command>\n" +
                "provides a detailed instruction on how to use a command.\n" +
                "type !list to display a list of commands.";
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

        //TODO: get rid of bot instance
        if(message.split(" ").length == 2 &&  bot.findCommand(message.split(" ")[1]) != null){
            toUserString = bot.findCommand(message.split(" ")[1]).getHelpString() ;

        }else if(message.split(" ").length == 1){

            toUserString = bot.getDescriptionList();
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
		return "provides instructions for a command."; 
	}	
}