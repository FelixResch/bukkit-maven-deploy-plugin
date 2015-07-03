package io.femo.bukkit.console;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NewServerDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField serverHost;
    private JTextField rconPort;
    private JPasswordField rconPwd;
    private JTextField queryPort;
    private JLabel lserverHost;
    private JLabel lrconPort;
    private JLabel lrconPwd;
    private JLabel lqueryPort;
    private JTextField serverName;
    private JLabel lserverName;
    private NewServerListener listener;

    public NewServerDialog(NewServerListener listener) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        this.listener = listener;

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        if(listener != null) {
            String name = serverName.getText();
            if(name.length() == 0) {
                lserverName.setForeground(Color.RED);
                return;
            }
            String host = serverHost.getText();
            if(host.length() == 0) {
                lserverHost.setForeground(Color.RED);
                return;
            }
            int rconPort;
            try {
                rconPort = Integer.parseInt(this.rconPort.getText());
            } catch (NumberFormatException e) {
                lrconPort.setForeground(Color.RED);
                return;
            }
            String pwd = new String(rconPwd.getPassword());
            if(pwd.length() == 0) {
                lrconPwd.setForeground(Color.RED);
                return;
            }
            int queryPort;
            try {
                queryPort = Integer.parseInt(this.queryPort.getText());
            } catch (NumberFormatException e) {
                lqueryPort.setForeground(Color.RED);
                return;
            }
            listener.newServer(name, host, rconPort, pwd, queryPort);
        }
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

}
