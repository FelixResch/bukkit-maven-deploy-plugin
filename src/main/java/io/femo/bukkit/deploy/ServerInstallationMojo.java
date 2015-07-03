package io.femo.bukkit.deploy;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Created by felix on 6/28/15.
 */
@Mojo(name = "install-server")
public class ServerInstallationMojo extends AbstractMojo {

    @Parameter(property = "server.path", defaultValue = "server/")
    private File serverPath;

    @Parameter(property = "server.tmp", defaultValue = "tmp/")
    private File tmpPath;

    @Parameter
    private String serverPassword;

    public void execute() throws MojoExecutionException, MojoFailureException {
        if(!serverPath.exists()) {
            ServerInstaller.install(serverPath, tmpPath, getLog(), serverPassword);
        }
    }

}
