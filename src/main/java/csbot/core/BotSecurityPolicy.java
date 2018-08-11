package csbot.core;

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
    
            if (isPlugin(domain)) {
                return pluginPermissions();
            }
            else {
                
                return applicationPermissions();
            }        
    }
 
    private boolean isPlugin(ProtectionDomain domain) {
      
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