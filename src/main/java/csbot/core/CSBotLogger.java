package csbot.core;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSBotLogger{

    private  String tag;

    public CSBotLogger(String name){

        if(name == null){throw new IllegalArgumentException("CustomLogger.Constructor: tag is null.");}
        this.tag = name;
    }//CustomLogger


    private static String getTimestamp(){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd:HH:mm");
        return format.format(new Date());
    }

    private void writeMessage(String message){
        System.out.println("[" + getTimestamp() + "] (" + this.tag + ") " + message);
    }//writeToStream


    public  void debug(String message){
        this.writeMessage("[DEBUG] " + message);

    }

    public  void warn(String message){
        this.writeMessage("[WARN]  " + message);

    }

    public  void error(String message){
        this.writeMessage("[ERROR] " + message);

    }

    public void error(String message, Exception e){

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        e.printStackTrace(pw);

        error(message + "\n" + sw.toString());

    }





}


