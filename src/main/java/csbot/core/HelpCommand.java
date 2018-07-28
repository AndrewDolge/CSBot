package csbot.core;


import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class HelpCommand implements Command {
    
    private CSBot bot;

	public HelpCommand( CSBot bot) {
       
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
	public String getHelp() {
        return  "usage: !help\n" + 
                "\tprovides a list of all Commands this bot has.\n" + 
                "usage: !help <Command>\n" +
                "\tprovides a detailed instruction on how to use the given <Command>.\n";
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
	public void execute(MessageReceivedEvent event, String message) {
        String toUserString = null;

        //TODO: get rid of bot instance
        if(message.split(" ").length == 2 &&  bot.findCommand(message.split(" ")[1]) != null){
            toUserString = bot.findCommand(message.split(" ")[1]).getHelp() ;

        }else if(message.split(" ").length == 1){

            toUserString = bot.getDescriptionList();
        }

        if(toUserString != null){
            Command.sendMessageWithMention(event, Command.formatText(toUserString));
        }
	}//execute
	
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

	@Override
	public String getCredits() {
		return "Andrew Dolge: 7/28/2018";
	}

	@Override
	public String getTrigger() {
		return "help";
	}	
}