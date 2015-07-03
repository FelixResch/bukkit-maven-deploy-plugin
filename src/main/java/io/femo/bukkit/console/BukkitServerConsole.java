package io.femo.bukkit.console;

import com.google.rcon.AuthenticationException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by felix on 6/29/15.
 */
public class BukkitServerConsole {
    private JList players;
    private JList servers;
    private JList commands;
    private JButton addButton;
    private JPanel panel;
    private JButton stopServer;

    private ServerListModel serverListModel;

    public BukkitServerConsole() {
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                NewServerDialog dialog = new NewServerDialog(new NewServerListener() {
                    public void newServer(String name, String hostname, int rconPort, String rconPwd, int queryPort) {
                        Server server = new Server(name, hostname, rconPwd, rconPort, queryPort);
                        serverListModel.addElement(server);
                    }
                });
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                dialog.setVisible(true);
            }
        });
        serverListModel = new ServerListModel();
        servers.setModel(serverListModel);
        servers.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Server server = (Server) servers.getSelectedValue();
                if(server != null) {
                    try {
                        server.connect();
                        stopServer.setEnabled(true);
                        players.setModel(server.getPlayers());
                        players.setEnabled(true);
                    } catch (IOException e) {
                        JOptionPane.showMessageDialog(null, "Could not connect to the server!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    } catch (AuthenticationException e) {
                        JOptionPane.showMessageDialog(null, "You entered the wrong password", "Wrong Password", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        stopServer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Server server = (Server) servers.getSelectedValue();
                try {
                    server.stop();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Could not connect to the server!", "Connection Error", JOptionPane.ERROR_MESSAGE);
                } catch (AuthenticationException e) {
                    JOptionPane.showMessageDialog(null, "You entered the wrong password", "Wrong Password", JOptionPane.ERROR_MESSAGE);
                }
                servers.clearSelection();
                stopServer.setEnabled(false);
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("BukkitServerConsole");
        frame.setContentPane(new BukkitServerConsole().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
