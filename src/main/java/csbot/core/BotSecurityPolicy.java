package csbot.core;

import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.AllPermission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * A subclass of {@code Policy} that assigns permissions
 * for external plugins.
 * 
 * {@link https://blog.jayway.com/2014/06/13/sandboxing-plugins-in-java/}
 * @author Andrew Dolge
 * 
 */
public class BotSecurityPolicy extends Policy{


    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {

        System.out.println("Policy getPermissions codesource location: " + domain.getCodeSource().getLocation().getFile());
     
         
            if (isPlugin(domain)) {
                System.out.println("returning plugin permissions with file");
                return pluginPermissions();
            }
            else {
                System.out.println("returning application permissions with file");
                return applicationPermissions();
            }        
    }
 
    private boolean isPlugin(ProtectionDomain domain) {
        System.out.println("Checking is domain plugin: " + domain.getClassLoader().getName() + "result: " + (domain.getClassLoader() instanceof CommandClassLoader) );
        return domain.getClassLoader() instanceof CommandClassLoader;
    }
 
    private PermissionCollection pluginPermissions(String path) {

    
        Permissions permissions = new Permissions(); // No permissions
        //permissions.add(new FilePermission("C:\\Users\\Curious Sight\\Desktop\\java_testing\\pluginArchitecture\\apptest\\data\\plugin\\data.txt", "read,write,delete"));
        return permissions;
    }
    private PermissionCollection pluginPermissions() {

        Permissions permissions = new Permissions(); // No permissions
       return permissions;
    }
 
 
    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }




}