package csbot.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * Loads in values from a given property file
 */
public class BotPropertyLoader{

    private Properties properties;
    

    public BotPropertyLoader(InputStream inputStream) throws IOException{

        if(inputStream == null){throw new IllegalArgumentException("BotPropertyLoader.constructor: inputStream is null");}
        properties = new Properties();
        properties.load(inputStream);

    }

    public BotPropertyLoader(File file) throws IOException{
        properties = new Properties();
        FileReader reader = new FileReader(file);
        properties.load(reader);
    }

    /**
     * Gets The Token from the property file
     * @return the token, or null if it doesn't exist.
     * 
     */
    public String getToken(){
        return properties.getProperty("Token");

    }//getToken
    
    /**
     * Returns the cooldown in seconds for the given command from the properties file.
     * 
     */
    public int getCooldown(String command, int defaultCooldown){
        int result = getDefaultCooldown(defaultCooldown);
        try{
            String value = properties.getProperty(command + "Cooldown");
            result = Integer.max(1,Integer.valueOf(value));
        }catch(Exception e){}
      
        return result;

    }//getCooldown

    /**
     * Returns the default cooldown in seconds for all command from the properties file.
     * 
     */
    public int getDefaultCooldown(int defaultCooldown){
        int result = defaultCooldown;
        try{
            String value = properties.getProperty("DefaultCooldown");
            result = Integer.max(1,Integer.valueOf(value));
        }catch(Exception e){}
      
        return result;

    }

}