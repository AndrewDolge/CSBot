package csbot.core;

import java.io.FilePermission;
import java.security.AllPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A subclass of {@code Policy} that assigns permissions
 * for external plugins.
 * 
 * {@link https://blog.jayway.com/2014/06/13/sandboxing-plugins-in-java/}
 * @author Andrew Dolge
 * 
 */
public class BotSecurityPolicy extends Policy{

    private static final Logger logger = LoggerFactory.getLogger("BotSecurityPolicy");
 
    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {


        logger.debug("inside get permissions");
    
        if(isCommandPlugin(domain)){
            return commandPermissions();
        }else{
            return applicationPermissions();
        }
        
    }


    private boolean isCommandPlugin(ProtectionDomain domain){
       logger.debug( "isCommandPlugin: domain: " + domain.getCodeSource().toString() + " value: " + String.valueOf(domain.getClassLoader() instanceof CommandClassLoader));
        return domain.getClassLoader() instanceof CommandClassLoader;
    }


    private PermissionCollection commandPermissions(){
        Permissions permissions = new Permissions();
        //permissions.add(new FilePermission(CSBot.getPluginDataDirectory().toString(), "read,write,delete"));
        return permissions;
    }

    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }


}