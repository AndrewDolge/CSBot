package csbot.core;

import java.io.File;
import java.io.FilePermission;

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
    private String dataDir;

    public BotSecurityPolicy(String dataDir){
        super();
        if(dataDir== null){throw new IllegalArgumentException("path is null");}
        this.dataDir = dataDir;

    }

    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
    
        System.out.println("ProtectionDomain: " + domain.getCodeSource().getLocation().getPath() + " is plugin: " + isPlugin(domain));
            
        if (isPlugin(domain)) {
            String pluginPath = domain.getCodeSource().getLocation().getPath();

            String dataPluginPath = dataDir + File.separatorChar + pluginPath.substring(pluginPath.lastIndexOf("/") + 1,pluginPath.lastIndexOf("."));

            return pluginPermissions(dataPluginPath);
            }
            else {
                
                return applicationPermissions();
            }        
    }
 
    private boolean isPlugin(ProtectionDomain domain) {
      
        return domain.getClassLoader() instanceof CommandClassLoader;
    }
 

    private PermissionCollection pluginPermissions(String pluginDataDir) {

        Permissions permissions = new Permissions(); // No permissions
       
            System.out.println("Giving File permission to: " + pluginDataDir + File.separatorChar+ "*");
            permissions.add(new FilePermission(pluginDataDir + File.separatorChar + "*", "read,write,delete"));

      
       return permissions;
    }
 
 
    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }




}