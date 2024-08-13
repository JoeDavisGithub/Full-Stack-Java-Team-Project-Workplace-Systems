package com.TDAF;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ShiftManager{
    // GUI and code created by Tayo
    private static JFrame frame = new JFrame("SHIFT MANAGER");
    private JButton createReportButton;
    private JButton setAutomaticReportButton;
    private JPanel shiftManagerPanel;
    private JButton logOutButton;
    private JButton receptionistButton;
    private JButton technicianButton;
    public user Logged_in;
    public static String currentlyLoggedin;


    public user getLogged_in() {
        return Logged_in;
    }


    public ShiftManager(user logged) {
        this.Logged_in = logged;
        if (Logged_in.getrole().equals("Shift Manager")){
            logOutButton.setText("Log Out");
        }else{
            logOutButton.setText("Back");
        }

        setAutomaticReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetAutomaticReport setAutomaticReport = new SetAutomaticReport(Logged_in);
                setAutomaticReport.main();
                frame.dispose();
            }
        });
        createReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateReport createReport = new CreateReport(Logged_in);
                createReport.main();
                frame.dispose();
            }
        });

        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (logOutButton.getText().equals("Log Out")) {
                    Login.start();
                }else{
                    new OfficeManager(Logged_in).main();
                }
                frame.dispose();
            }
        });

        receptionistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receptionist receptionist = new Receptionist(Logged_in);
                receptionist.main();
                frame.dispose();
            }
        });
        technicianButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Technician technician = new Technician(Logged_in);
                technician.main();
                frame.dispose();
            }
        });

    }


    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ShiftManager(Logged_in).shiftManagerPanel);
        frame.setVisible(true);
        DeadlineAlert deadlineAlert = new DeadlineAlert();
        deadlineAlert.main(frame,Logged_in);
    }


}
