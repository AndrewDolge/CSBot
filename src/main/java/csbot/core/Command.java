package csbot.core;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Represents a Command that a user can invoke through a text channel.
 * 
 * @author Andrew Dolge
 */
public interface Command{

    /**
     * returns a string that signifies who created this command.
     * @return a string with this command's author.
     */
    public String getCredits();

    /**
     * returns a short description of this Command.
     * Descriptions should be no longer than 20 characters or one sentence.
     * This string will be displayed in the list of all commands for this bot.
     * 
     * @return a string with a short description of this Command.
     */
    public String getDescription();

    /**
     * returns a detailed description of this Command.
     * Help Strings should give a description of all functions of this Command.
     * 
     * Here is an example of a proper Help String:
     * 
     *     usage: !help
     *          provides a list of all Commands this bot has.
     *     usage: !help <Command>
     *          provides a detailed instruction on how to use the given <Command>.
     * 
     * @return a string with a detailed description of the Command.
     *
     */
    public String getHelp();

    /**
     * Returns a string that is used to invoke this Command in a text channel.
     * This should uniquely identify this Command. If two Commands have the same trigger,
     * preference will be given to the Command already loaded into the bot.
     * 
     * The user will be able to invoke this Command with the prefix "!" and the trigger string.
     * 
     * @return a string that is used to invoke the Command.
     * 
     */
    public String getTrigger();

    /**
     * Executes the given Command in response to the given event from a user.
     * This method is responsible for parsing the message of the user and responding by
     * accessing the user and channel through the given event.
     * 
     * @param event the discord MessageReceivedEvent to process.
     * @param message the message string the user sent to invoke the bot.
     */
    public void execute(MessageReceivedEvent event, String message);

    /**
     * attempt to send a private message to the author of this event.
     * 
     * @param event   the event object received.
     * @param message the message to return to the user.
     * 
     */
    public static void sendPrivateMessageToAuthor(MessageReceivedEvent event, String message){
        try{
            event.getAuthor().openPrivateChannel().complete().sendMessage(message).queue();
            }catch(Exception e){
                e.printStackTrace();
            }

    }//sendPrivateMessageToChannel

    /**
     * sends a message to the channel that this message was received.
     * 
     * @param event   the event object received.
     * @param message the message to return to the user.
     * 
     */
    public static void sendMessageToChannel(MessageReceivedEvent event, String message){
        try{
        event.getChannel().sendMessage(message).queue();
        }catch(Exception e){
            e.printStackTrace();
        }
    }//sendMessageToChannel

    /**
     * sends a message to the channel that this message was received, with an @user notification
     * 
     * @param event   the event object received.
     * @param message the message to return to the user.
     * 
     */
    public static void sendMessageWithMention(MessageReceivedEvent event, String message){
        sendMessageToChannel(event, event.getAuthor().getAsMention() + "\n"+message);
    }//sendMessageToChannel

    /**
     * modifies the given string with discord formatting.
     * 
     * @param toFormat the string to format
     * @returns the formatted string
     */
    public static String formatText(String toFormat){
        return "```\n" + toFormat + "\n```";
    }//formatText

}