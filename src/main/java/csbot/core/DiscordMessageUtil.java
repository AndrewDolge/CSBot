package csbot.core;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Static Utility class that handles discord messages properly.
 * @author Andrew Dolge
 * 
 */
public class DiscordMessageUtil{

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





}//class