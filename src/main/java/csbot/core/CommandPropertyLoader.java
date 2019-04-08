package csbot.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Loads in values from a given property file from a plugin
 */
public class CommandPropertyLoader{

    private Properties properties;

    public CommandPropertyLoader(InputStream inputStream) throws IOException{

        if(inputStream == null){throw new IllegalArgumentException("PropertyLoader.constructor: inputStream is null");}
        properties = new Properties();
        properties.load(inputStream);

    }

    public CommandPropertyLoader(File file) throws IOException{
        properties = new Properties();
        FileReader reader = new FileReader(file);
        properties.load(reader);
    }

    /**
     * Gets a list of all the commands in the command.properties file.
     * 
     * 
     */
    public String[] getCommands(){

       String[] result = new String[0]; 
       
       String commands = properties.getProperty("Commands");

       if(commands != null){
            result = commands.split(",");
       }

       return result;
    }
    

    public String getVersion(){
        return properties.getProperty("Version", "unspecified");
    }

    public String getName(){
        return properties.getProperty("Name", "unspecified");
    }

    public String getAuthor(){
        return properties.getProperty("Author", "unspecified");
    }

}