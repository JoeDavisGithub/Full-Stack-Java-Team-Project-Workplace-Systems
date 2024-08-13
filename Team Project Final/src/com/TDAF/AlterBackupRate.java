package com.TDAF;

//import sun.rmi.runtime.Log;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AlterBackupRate {
    // Created by Tayo
    private static JFrame frame = new JFrame("ALTER BACKUP RATE");
    private JButton setButton;
    private JButton backButton;
    private JComboBox comboBox1;
    private JTextArea textArea1;
    private JPanel AlterBackUpRatePanel;
    private user Logged_in;

    public AlterBackupRate(user loggedin) {
        this.Logged_in = loggedin;
        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,"BACKUP RATE ALTERED");
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManager officeManager = new OfficeManager(Logged_in);
                officeManager.main();
                frame.dispose();
            }
        });
    }

    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new AlterBackupRate(Logged_in).AlterBackUpRatePanel);
        frame.setVisible(true);
    }
}
