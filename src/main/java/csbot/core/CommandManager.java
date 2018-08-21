package csbot.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandManager{

    private HashMap<String, CommandWrapper> commandMap;
    private ExecutorService    commandExecutor;

    private static final CustomLogger logger = new CustomLogger("csbot.CommandManager");


    public CommandManager(){
        this.commandMap = new HashMap<String, CommandWrapper>();
    }

    private class CommandWrapper{

        private Command command;
        private String  dataDir;
        private int     cooldown;
        private AtomicBoolean ready;

        public Command getCommand(){
            return this.command;
        }

        public String getDataDir(){
            return this.dataDir;
        }

        public int getCooldown(){
            return this.cooldown;
        }

        public boolean isReady(){
            return ready.get();
        }

        public void setReady(boolean ready){
            this.ready.set(ready);
        }

        public CommandWrapper(Command command, String dataDir, int cooldown){
            this.dataDir = dataDir;
            this.command = command;
            this.cooldown = cooldown;
            this.ready    = new AtomicBoolean(true);
        }
    }//class

    /**
     * Attempts to execute a Command with the specified trigger.
     * This will fail to execute the Command if:
     *      the Command is not ready to be used or
     *      the trigger does not map to a Command.
     * 
     * @param trigger the trigger string of a potential command.
     */
    public synchronized void execute(MessageReceivedEvent event, String trigger){

       CommandWrapper toExecute = commandMap.get(trigger);

       if(toExecute != null ){
            if(toExecute.isReady()){
                toExecute.setReady(false);

                this.commandExecutor.execute(
                    new Runnable(){

						@Override
						public void run() {
                            try {
                                if(!(event.getJDA().getStatus() == JDA.Status.SHUTDOWN || event.getJDA().getStatus() == JDA.Status.SHUTTING_DOWN)){

                                    logger.debug("Executing Command (" + toExecute.getCommand().getTrigger() +") for user: " + event.getAuthor().getName());
                                    
                                    DiscordMessage message = new DiscordMessage(toExecute.getDataDir(), event );

                                    toExecute.getCommand().execute(message);

                                    Thread.sleep(toExecute.getCooldown() * 1000);
                                }
                              
                            } catch (Exception e) {
                                logger.error("\tException thrown Executing Command (" + toExecute.getCommand().getTrigger() +") for user: " + event.getAuthor().getName(),e);
                            } finally{
                                toExecute.setReady(true);
                            }//finally
                        }//run 
                    }//Rnnable
                );//Execute
            }//if isReady()
       }else{
           logger.error("CommandManager.execute: CommandWrapper lookup on " + trigger +" resulted in null.");
       }

    }//execute

    public void start(){
        this.commandExecutor = Executors.newCachedThreadPool();
    }

    public boolean isRunning(){
        return this.commandExecutor != null || this.commandExecutor.isShutdown();
    }

    public void shutdown(){
        this.commandExecutor.shutdownNow();
    }//shutdown

    /**
     * Determines whether the CommandManager currently contains a Command that maps to the given trigger.
     * 
     * @param trigger the trigger of the Command.
     * @return true, if the CommandManager contains a Command with the given trigger, false otherwise.
     * 
     */
    public boolean contains(String trigger){
        boolean result = false;

        if (commandMap.get(trigger) != null && 
            commandMap.get(trigger).getCommand().getTrigger().equalsIgnoreCase(trigger) ){

            result = true;

        }
        return result;
    }//contains

    /**
     * adds the Command to this CommandManager.
     * @param command the command to add.
     * @return True, if the Command was unique and added, false otherwise.
     * @throws NullPointerException if command or any of its String methods return null.
     */
    public boolean addCommand(Command command,String dataDir, int cooldown){
        boolean result = false;

        if(command == null){
            throw new NullPointerException("CommandManager.addCommand: command is null.");
        }else if(command.getDescription() == null){
            throw new NullPointerException("CommandManager.addCommand: command's description is null.");
        }else if(command.getHelp() == null){
            throw new NullPointerException("CommandManager.addCommand: command's help string is null.");
        }else if(command.getTrigger() == null){
            throw new NullPointerException("CommandManager.addCommand: command's trigger is null.");
        }

        if(!this.contains(command.getTrigger())){

            this.commandMap.put(command.getTrigger(), new CommandWrapper(command, dataDir, cooldown));
            result = true;
            
        }
        return result;
    }//addCommand

    /**
     * returns a Mapping of Command triggers to Command Help Strings of the Commands in this Manager.
     * 
     * @return a Mapping of Command triggers to help strings.
     * 
     */
    public Map<String, String> getHelpText(){

        HashMap<String, String> result = new HashMap<String, String>();
        
        for(String trigger: commandMap.keySet()){

            if(commandMap.get(trigger) != null){
              result.put(trigger, commandMap.get(trigger).getCommand().getHelp() );
            }
        }

        return result;
    }//getHelpText

    /**
     * returns a string containing all the descriptions of the Commands in this CommandManager.
     * 
     * @return a String with descriptions of all Commands.
     */
    public String getDescriptions(){
        StringBuilder result = new StringBuilder();
        
        for(CommandWrapper current: commandMap.values()){
             result.append(current.getCommand().getTrigger() + ": " + current.getCommand().getDescription() + "\n");
        }//for

        return result.toString();
    }//getDescriptionList

}//class