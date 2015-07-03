package io.femo.bukkit.console;

/**
 * Created by felix on 6/29/15.
 */
public interface NewServerListener {

    void newServer(String name, String hostname, int rconPort, String rconPwd, int queryPort);
}
