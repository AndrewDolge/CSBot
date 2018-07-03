import net.dv8tion.jda.core.events.Event;

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


    public Command(String trigger, int cooldown){

        if(trigger == null){throw new IllegalArgumentException("Command.constructor: trigger is null");}
        if(trigger == ""){throw new IllegalArgumentException("Command.constructor: trigger is null");}
        if(cooldown < 0)    {throw new IllegalArgumentException("Command.constructor: cooldown is negative");}

        this.trigger = trigger;
        this.cooldown = cooldown;

    }//constructor


    public abstract String getHelpString();
    public abstract void   process(Event e);



}//class