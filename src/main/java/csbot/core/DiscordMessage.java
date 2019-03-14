package csbot.core;

import java.util.ArrayList;
import java.util.List;



import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * Static Utility class that handles discord messages properly.
 * @author Andrew Dolge
 * 
 */
public class DiscordMessage{


    private String filePath;
    private MessageReceivedEvent event;
    

    public DiscordMessage(String filePath, MessageReceivedEvent event){

        if(filePath == null){throw new IllegalArgumentException("dataFile is null");}
        if(event == null){throw new IllegalArgumentException("event is null");}

        this.filePath = filePath;
        this.event    = event;
    }//constructor

    /**
     * attempt to send a private message to the author of this event.
     * 
     * @param event   the event object received.
     * @param message the message to return to the user.
     * 
     */
    public  void sendPrivateMessageToAuthor(String message){
        try{
            this.event.getAuthor().openPrivateChannel().complete().sendMessage(message).queue();
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
    public  void sendMessageToChannel( String message){
        try{
        this.event.getChannel().sendMessage(message).queue();
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
    public  void sendMessageWithMention( String message){
        sendMessageToChannel(this.event.getAuthor().getAsMention() + "\n"+message);
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
     * Gets the message author's name.
     * @return the authors name.
     */
    public String getAuthorName(){
        return event.getAuthor().getName();
    }//getAuthorName

    /**
     * Gets the message author's name with the 4 digit discriminator id.
     * @return the author's name with the discriminator
     * 
     */
    public String getAuthorNameWithDiscriminator(){
        return event.getAuthor().getName() + event.getAuthor().getDiscriminator();
    }//getAuthorNameWithDiscriminator

    /**
     * 
     * @return the unique snowflake of the message author. Guaranteed never to change.
     */
    public String getAuthorID(){
        return event.getAuthor().getId();
    }//getAuthorID

    /**
     * Gets the Exact text sent by the user.
     * @return a string containing the text of the message
     * 
     */
    public String getText(){
        return this.event.getMessage().getContentRaw();
    }//getText

    /**
     * Checks whether the author has admin permissions in this guild.
     * @return true, if the author has administrator privledges in this guild., false otherwise.
     * 
     */
    public boolean isAuthorAdmin(){

        boolean result = false;

        if(event.getMember() != null){
            
            result = event.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
        }

        return result;

    }//isAuthorAdmin

    /**
     * Returns a list of the roles of the Author.
     * @return a list of strings with the roles of the user, or an empty list if the user has none or is not in a guild.
     * 
     */
    public List<String> getAuthorRoles(){
        List<String> result = new ArrayList<String>();

        if(event.getMember() != null){
            
            for(Role role: event.getMember().getRoles()){
                result.add(role.getName());
            }
        }

        return result;
    }//getAuthorRoles

    /**
     * Determines if the author is the given role on the server.
     * @param role the role the user might have
     * @return true, if the user has the given role, false otherwise.
     * 
     */
    public boolean isAuthorRole(String role){
        boolean result = false;

        if(event.getMember() != null){

            for(Role current : event.getMember().getRoles()){
               if( current.getName().equals(role)){
                   result = true;
               }
            }
        }

        return result;

    }//isAuthorRole

    /**
     * Assigns the role with the given name to the author of this message
     * Will not assign roles with dangerous permissions to the author.
     * Dangerous permissions include: managing permissions, managing roles, and administrator access.
     * 
     * @param event   the event object received.
     * @param role    the unique role to be assigned to the user.
     * 
     */
    public void assignRoleToAuthor(String role) throws Exception{

        if(event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)){
            List<Role> roles =  event.getGuild().getRolesByName(role, true);

            if(roles.size() == 1){
                if(
                   !roles.get(0).hasPermission(Permission.MANAGE_PERMISSIONS) &&
                   !roles.get(0).hasPermission(Permission.MANAGE_ROLES) &&
                   !roles.get(0).hasPermission(Permission.ADMINISTRATOR) 
                   ){
                      
                    event.getGuild().getController().addSingleRoleToMember(event.getMember(), roles.get(0)).queue();
                }else{
                   throw new Exception("DiscordMessage.assignRoleToAuthor: cannot add role that manages permissions, roles, or admin status: " + role);
                }
            }else{
                throw new Exception("DiscordMessage.assignRoleToAuthor: Could not find unique role: " + role);
            }
        }else{
            throw new Exception("DiscordMessage.assignRoleToAuthor: this bot doesn't have manage roles permissions.");
        }
    }//AssignRoleToAuthor

    /**
     * Removes the given role from the author.
     * @param role the role to take away from the author.
     * 
     */
    public void removeRoleFromAuthor(String role) throws Exception{
      

        if(event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_ROLES)){
            List<Role> roles =  event.getGuild().getRolesByName(role, true);

            if(roles.size() == 1){
                event.getGuild().getController().removeSingleRoleFromMember(event.getMember(), roles.get(0)).queue();
            } else{
                throw new Exception("DiscordMessage.assignRoleToAuthor: Could not find unique role: " + role);
            }
        }else{
            throw new Exception("DiscordMessage.assignRoleToAuthor: This bot doesn't have manage role permissions.");
        }

    }///removeRoleFromAuthor

    /** 
     * @return  a string representation of a path to the data directory assigned to this plugin.
     * 
     */
    public  String getPluginDataDirectory(){
       return this.filePath;
    }//getPluginDataDirectory

}//class