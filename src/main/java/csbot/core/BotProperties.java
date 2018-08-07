package csbot.core;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class BotProperties{

    private File       propertyFile;
    private static final Logger logger = LoggerFactory.getLogger("BotProperties");

    public BotProperties(File file){

        if(file == null){throw new IllegalArgumentException("BotProperties.constructor: file is null");}

        this.propertyFile = file;
    }//constructor


    /**
     * Retrieves the Discord bot token from the properties file.
     * 
     * @return the token of the bot, null otherwise.
     */
    public String getToken(){

        Properties prop = new Properties();
        String result   = null;
        
        try( FileReader reader = new FileReader(propertyFile)){
        
            prop.load(reader);
            result = prop.getProperty("token");

        }catch(IOException ioe){
            logger.error("BotProperties.getToken: IOException thrown.", ioe);
        }

       return result;

    }


}