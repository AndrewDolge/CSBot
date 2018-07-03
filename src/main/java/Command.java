import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Represents the basic components of all commands.
 * 
 * 
 * 
 * @author Andrew Dolge
 * 
 */
public abstract class Command{

    private String  trigger;
    private int     cooldown;
    private boolean onCooldown;

    /**
     * High level Constructor for the Command Class
     * 
     * @param trigger the text string that identifies this command
     * @param cooldown the amount of time, in seconds, that this command remains on cooldown after it is used.
     */
    private Command(String trigger, int cooldown){

        if(trigger == null) {throw new IllegalArgumentException("Command.constructor: trigger is null");}
        if(trigger == ""  ) {throw new IllegalArgumentException("Command.constructor: trigger is empty");}
        if(cooldown < 0   ) {throw new IllegalArgumentException("Command.constructor: cooldown is negative");}

        this.trigger = trigger;
        this.cooldown = cooldown;

    }//constructor

    /**
     * Retrieves the Help String for this command.
     * The Help string should include detailed instructions on how to use this command,
     * including all the different usages of this command.
     * 
     * @return The Help String of this command.
     */
    public abstract String getHelpString();

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
    public abstract void  process(MessageReceivedEvent event);



}//class