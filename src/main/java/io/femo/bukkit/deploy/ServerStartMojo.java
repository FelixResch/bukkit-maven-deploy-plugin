package io.femo.bukkit.deploy;

import com.google.rcon.AuthenticationException;
import com.google.rcon.RCon;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;

/**
 * Created by felix on 6/28/15.
 */
@Mojo(name = "start-server")
public class ServerStartMojo extends AbstractMojo {

    @Parameter(defaultValue = "server/craftbukkit.jar")
    private File serverJar;

    @Parameter(defaultValue = "25565")
    private int serverPort;

    @Parameter(defaultValue = "localhost")
    private String serverAddress;

    @Parameter
    private String serverPassword;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if(!checkServerAlive()) {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", serverJar.getName());
            processBuilder.directory(serverJar.getParentFile());
            try {
                processBuilder.start();
            } catch (IOException e) {
                getLog().error("Could not start server");
            }
            getLog().info("Waiting for server to come online");
            while (!checkServerAlive()) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    getLog().error("Was interrupted!");
                }
            }
            getLog().info("Server ready");
        }
    }

    private boolean checkServerAlive() {
        try {
            RCon rCon = new RCon(serverAddress, 25575, serverPassword.toCharArray());
            rCon.close();
            return true;
        } catch (IOException e) {
            return false;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    public void setServerJar(File serverJar) {
        this.serverJar = serverJar;
    }
}
