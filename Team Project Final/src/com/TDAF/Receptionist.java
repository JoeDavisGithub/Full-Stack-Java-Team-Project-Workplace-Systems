package com.TDAF;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Receptionist {
    private static JFrame frame = new JFrame("RECEPTIONIST");
    private JButton createCustomerAccountButton;
    private JButton viewCustomerAccountButton;
    private JButton createJobButton;
    private JButton recordPaymentButton;
    private JPanel receptionistPanel;
    private JButton button1;
    private user Logged_in;

    public user getLoggedin() {
        return this.Logged_in;
    }
    // GUI created by Tayo, code created by Tayo
    public Receptionist(user loggedin) {
        this.Logged_in = loggedin;
        if (getLoggedin().getrole().equals("Receptionist")){
            button1.setText("Log Out");
        }else{
            button1.setText("Back");
        }

        createCustomerAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateCustomerAccount createCustomerAccount = new CreateCustomerAccount(Logged_in);
                createCustomerAccount.main();
                frame.dispose();
            }
        });
        viewCustomerAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ViewCustomerAccount viewCustomerAccount = new ViewCustomerAccount(Logged_in);
                viewCustomerAccount.main();
                frame.dispose();
            }
        });
        createJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JobCreation jobCreation = new JobCreation(Logged_in);
                jobCreation.main();
                frame.dispose();
            }
        });
        recordPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RecordPayment recordPayment = new RecordPayment(Logged_in);
                recordPayment.main();
                frame.dispose();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login();
                if (button1.getText().equals("Back") && getLoggedin().getrole().equals("Office Manager")){
                    OfficeManager officeManager = new OfficeManager(Logged_in);
                    officeManager.main();
                }
                else if (button1.getText().equals("Back") && getLoggedin().getrole().equals("Shift Manager")){
                    ShiftManager shiftManager = new ShiftManager(Logged_in);
                    shiftManager.main();
                }
                else{
                    login.start();
                }
                frame.dispose();
            }
        });
    }

    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new Receptionist(getLoggedin()).receptionistPanel);
        frame.setVisible(true);
        DeadlineAlert deadlineAlert = new DeadlineAlert();
        deadlineAlert.main(frame,Logged_in);
    }
}