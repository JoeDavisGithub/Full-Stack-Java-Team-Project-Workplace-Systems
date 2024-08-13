package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class createStaffReport {
    private static JFrame frame = new JFrame("CREATE STAFF REPORT");
    private JPanel staffperformancepanel;
    private JButton backButton;
    private JComboBox staffselection;
    private JTextField endfield;
    private JTextField startfield;
    private JButton generateReportButton;
    private user Logged_in;
//window used to create a staff report, created by Joe
    public createStaffReport(user loggedin) {
        this.Logged_in = loggedin;
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startfield.getText();
                String endDate = endfield.getText();
                staffReport sr = new staffReport(Logged_in,startDate,endDate);
                sr.main();
                frame.dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateReport cr = new CreateReport(Logged_in);
                cr.main();
                frame.dispose();
            }
        });
    }

    public void main(){
        int width = 600;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new createStaffReport(Logged_in).staffperformancepanel);
        frame.setVisible(true);
    }
}
