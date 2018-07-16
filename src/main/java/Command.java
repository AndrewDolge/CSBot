import java.util.concurrent.atomic.AtomicBoolean;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Represents the basic components of all commands.
 * 
 * 
 * 
 * @author Andrew Dolge
 * 
 */
public abstract class Command implements Comparable<Command>{

    private String        trigger;
    private int           cooldown;
    private AtomicBoolean onCooldown;

    /**
     * High level Constructor for the Command Class
     * 
     * @param trigger the text string that identifies this command.
     * @param cooldown the amount of time, in seconds, that this command remains on cooldown after it is used.
     */
    public Command(String trigger, int cooldown){

        if(trigger == null) {throw new IllegalArgumentException("Command.constructor: trigger is null");}
        if(trigger == ""  ) {throw new IllegalArgumentException("Command.constructor: trigger is empty");}
        if(cooldown < 0   ) {throw new IllegalArgumentException("Command.constructor: cooldown is negative");}

        this.trigger = trigger;
        this.cooldown = cooldown;
        this.onCooldown = new AtomicBoolean(false);

    }//constructor

    /**
     * Getter method for the trigger string.
     * 
     * @return the trigger of this command.
     */
    public String getTrigger(){
        return this.trigger;

    }//getTrigger

    /**
     * Getter method for the cooldown time of this command.
     * 
     * @return the time, in seconds, that this second stays on cooldown.
     */
    public int getCooldown(){
        return this.cooldown;

    }//getCooldown

    /**
     * Checks to see if this command is on Cooldown.
     * 
     * @return true, if this command is on cooldown. False otherwise.
     */
    public boolean isOnCooldown(){
        return this.onCooldown.get();

    }//isOnCooldown
    
    /**
     * Sets whether this command is on Command
     * 
     * @param value the cooldown status
     */
    public void setOnCooldown(boolean value) {
       onCooldown.set(value);
    }

    @Override
    /**
     * Compares two Commands by their triggers. 
     * 
     * @returns 0 if the triggers are the same, a negative number if this command's trigger is lexographically less than the other commands, and positive otherwise.
     */
    public int compareTo(Command other){

        return this.getTrigger().compareTo(other.getTrigger());
    }//compareTo


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
            //TODO: log exception.
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
     * modifies the given string to use CSS style onto a message string
     * 
     * @param toFormat the string to format
     * @returns the formatted string
     */
    public static String formatText(String toFormat){
        return "```\n" + toFormat + "\n```";
    }//formatText

    /**
     * places this command on cooldown.
     * sleeps the current thread that this command runs on.
     * 
     */
    public void startCooldown(){
        setOnCooldown(true);
        try{
            Thread.sleep(cooldown * 1000);
        }catch(InterruptedException ie){
            
        }

        setOnCooldown(false);
    }//startCoolDown

    /**
     * Processes the current given Event.
     * The text of the message can be found by calling getMessage().getContentRaw().
     * 
     * This method is also responsible for responding to the user by queueing a message in the
     * channel this message was received in.
     * This can be accomplished 
     * 
     * @param event the incoming event that contains the message
     * 
     */
    public abstract void process(MessageReceivedEvent event);

    /**
     * Retrieves the Help String for this command.
     * The Help string should include detailed instructions on how to use this command,
     * including all the different usages of this command.
     * 
     * @return The Help String of this command.
     */
    public abstract String getHelpString();

    /**
     * returns a brief description of this command.
     * This string should be less than 20 characters, and give the user a basic idea of what the command does.
     * 
     * @return a brief description of this command.
     */
    public abstract String getDescription();


}//class