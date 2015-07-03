package io.femo.bukkit.console;

import com.google.rcon.AuthenticationException;
import com.google.rcon.RCon;
import query.MCQuery;
import query.QueryResponse;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by felix on 6/29/15.
 */
public class Server {

    private RCon rCon;
    private MCQuery mcQuery;
    private String name;
    private String host;
    private String rconPwd;
    private int rconPort;
    private int queryPort;
    private DefaultListModel<Player> players;

    public Server(String name, String host, String rconPwd, int rconPort, int queryPort) {
        this.name = name;
        this.host = host;
        this.rconPwd = rconPwd;
        this.rconPort = rconPort;
        this.queryPort = queryPort;
        this.players = new DefaultListModel<Player>();
    }

    public void connect() throws IOException, AuthenticationException {
        rCon = new RCon(host, rconPort, rconPwd.toCharArray());
        mcQuery = new MCQuery(host, queryPort);
        players.removeAllElements();
        QueryResponse response = mcQuery.fullStat();
        for(String name : response.getPlayerList()) {
            this.players.addElement(new Player(name));
        }
        /*String value = rCon.send("help");
        String[] commands = value.split("\n");
        for(String com : commands) {
            if(com.startsWith("��6")) {
                com = com.substring(3, com.indexOf(":"));
                System.out.println(com);
                Command command = new Command(com);
                command.resolve(rCon);
            } else {
                //System.out.println(com);
            }
        }*/
    }

    public void stop() throws IOException, AuthenticationException {
        if(rCon != null) {
            rCon.stop();
        }
    }

    public void disconnect() throws IOException {
        if(rCon != null) {
            rCon.close();
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public RCon getrCon() {
        return rCon;
    }

    public MCQuery getMcQuery() {
        return mcQuery;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getRconPwd() {
        return rconPwd;
    }

    public int getRconPort() {
        return rconPort;
    }

    public int getQueryPort() {
        return queryPort;
    }

    public DefaultListModel<Player> getPlayers() {
        return players;
    }
}
