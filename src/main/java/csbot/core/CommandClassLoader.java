package csbot.core;


import java.net.URL;
import java.net.URLClassLoader;

public class CommandClassLoader extends URLClassLoader {


    public CommandClassLoader(URL jarFileUrl) {
        super(new URL[] {jarFileUrl}); 
    }


 
}