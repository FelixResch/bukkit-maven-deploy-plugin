package io.femo.bukkit.console;

import com.google.rcon.IncorrectRequestIdException;
import com.google.rcon.RCon;

import java.io.IOException;

/**
 * Created by felix on 6/29/15.
 */
public class Command {

    private String name;

    public Command(String name) {
        this.name = name;
    }

    public void resolve(RCon rCon) throws IOException, IncorrectRequestIdException {
        String var = rCon.send("help " + name);
        System.out.println(var);
    }
}
