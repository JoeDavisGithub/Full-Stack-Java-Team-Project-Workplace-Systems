package com.TDAF;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Technician {
    private static JFrame frame = new JFrame("TECHNICIAN");
    private JButton viewTaskButton;
    private JButton deleteTaskButton;
    private JPanel technicianPanel;
    private JButton logOutButton;
    private JButton createTaskDetailButton;
    private JButton viewJobsButton;
    private JButton editTaskDetailButton;
    public user Logged_in;

    public user getLogged_in() {
        return this.Logged_in;
    }
    // GUI and code created by Tayo
    public Technician(user loggedin) {
        this.Logged_in = loggedin;
        if (Logged_in.getrole().equals("Technician")){
            logOutButton.setText("Log Out");

        }else{
            logOutButton.setText("Back");

        }
        viewTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewTasks viewTasks = new ViewTasks(Logged_in);
                viewTasks.main();
                frame.dispose();
            }
        });


        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Logged_in.getrole().equals("Office Manager")){
                    new OfficeManager(Logged_in).main();
                }
                else if (Logged_in.getrole().equals("Shift Manager")){
                    new ShiftManager(Logged_in).main();
                }
                else{
                    Login.start();
                }
                frame.dispose();
            }
        });

        viewJobsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewJob vj = new viewJob(Logged_in);
                vj.main();
                frame.dispose();
            }
        });

    }

    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Technician(Logged_in).technicianPanel);
        frame.setVisible(true);
    }
}
