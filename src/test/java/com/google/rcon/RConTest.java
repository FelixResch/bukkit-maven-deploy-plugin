package com.google.rcon;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by felix on 6/28/15.
 */
public class RConTest {

    @Test
    @Ignore
    public void testConnect() throws Exception {
        RCon rCon = new RCon("localhost", 25575, "1234".toCharArray());
        rCon.close();
    }
}