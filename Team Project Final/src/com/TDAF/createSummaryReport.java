package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class createSummaryReport {
    private static JFrame frame = new JFrame("CREATE SUMMARY REPORT");
    private JPanel summaryreportpanel;
    private JButton backButton;
    private JButton generateReportButton;
    private JTextField startfield;
    private JTextField endfield;
    private user Logged_in;
//window used to create a summary report created by Joe
    public createSummaryReport(user loggedin) {
        this.Logged_in=loggedin;
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startDate = startfield.getText();
                String endDate = endfield.getText();
                summaryReport sr = new summaryReport(Logged_in,startDate,endDate);
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
        frame.setContentPane(new createSummaryReport(Logged_in).summaryreportpanel);
        frame.setVisible(true);
    }

}
