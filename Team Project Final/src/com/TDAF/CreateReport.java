package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateReport {
    private static JFrame frame = new JFrame("CREATE REPORT");
    private JButton printReportButton;
    private JButton backButton;
    private JPanel CreateReportPanel;
    private JButton customerReportButton;
    private JButton individualStaffPerformanceReportButton;
    private JButton summaryReportButton;
    public user Logged_in;

    public user getLogged_in() {
        return this.Logged_in;
    }
//Window that allows the user to choose a type of report to generate, written by Joe
    public CreateReport(user loggedin) {
        this.Logged_in = loggedin;

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShiftManager shiftManager = new ShiftManager(Logged_in);
                shiftManager.main();
                frame.dispose();
            }
        });
        summaryReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createSummaryReport csr = new createSummaryReport(Logged_in);
                csr.main();
                frame.dispose();

            }
        });
        individualStaffPerformanceReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createStaffReport csr = new createStaffReport(Logged_in);
                csr.main();
                frame.dispose();

            }
        });
        customerReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createCustomerReport cr = new createCustomerReport(Logged_in);
                cr.main();
                frame.dispose();
            }
        });
    }

    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new CreateReport(Logged_in).CreateReportPanel);
        frame.setVisible(true);
    }
}
