package csbot.core;

import java.io.File;
import java.util.List;



import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
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


    /**
     * Assigns the role with the given name to the author of this message
     * Will not assign roles with dangerous permissions to the author.
     * Dangerous permissions include: managing permissions, managing roles, and administrator access.
     * 
     * @param event   the event object received.
     * @param role    the unique role to be assigned to the user.
     * 
     */
    public void assignRoleToAuthor( MessageReceivedEvent event, String role){

        if(event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)){
            List<Role> roles =  event.getGuild().getRolesByName(role, true);

            if(roles.size() == 1){
                if(
                   !roles.get(0).hasPermission(Permission.MANAGE_PERMISSIONS) &&
                   !roles.get(0).hasPermission(Permission.MANAGE_ROLES) &&
                   !roles.get(0).hasPermission(Permission.ADMINISTRATOR) 
                   ){
                      
                    event.getGuild().getController().addRolesToMember(event.getMember(), roles.get(0));
                }else{
                   throw new RuntimeException("MessageFacade.assignRoleToAuthor: cannot add dangerous permission. ");
                }
            }else{
                throw new RuntimeException("MessageFacade.assignRoleToAuthor: Could not find unique role: " + role);
            }
        }else{
            throw new RuntimeException("MessageFacade.assignRoleToAuthor: this bot doesn't have manage roles permissions.");
        }
    }//AssignRoleToAuthor

    public static File getPluginDataDirectory(){
        File result = null;
        try{
           
            result = new File(File.separatorChar + "data").getAbsoluteFile();

        }catch(Exception e){
            throw new RuntimeException("error accessing plugin directory",e);
        }

        return result;
    }//File

}//class