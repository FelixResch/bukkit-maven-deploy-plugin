package io.femo.bukkit.deploy;

import com.google.rcon.AuthenticationException;
import com.google.rcon.RCon;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by felix on 7/1/15.
 */
@Mojo(name = "deploy-to-server")
public class DeployMojo extends AbstractMojo {

    @Parameter
    private File finalJar;

    @Parameter
    private File serverPath;

    @Parameter(defaultValue = "localhost")
    private String serverAddress;

    @Parameter
    private String serverPassword;

    public void execute() throws MojoExecutionException, MojoFailureException {
        File plugins = new File(serverPath, "plugins");
        File deployed = new File(plugins, finalJar.getName());
        try {
            FileUtils.copyFile(finalJar, deployed);
        } catch (IOException e) {
            throw new MojoFailureException("Could not copy build target");
        }
        try {
            RCon rCon = new RCon(serverAddress, 25575, serverPassword.toCharArray());
            rCon.send("reload");
            rCon.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
    }
}
