package com.TDAF;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class createCustomerReport {
    private static JFrame frame = new JFrame("CREATE CUSTOMER REPORT");
    private JPanel createcustomerreportpanel;
    private JComboBox customerSelection;
    private JTextField startfield;
    private JTextField endfield;
    private JButton backButton;
    private JButton generateReportButton;
    private user Logged_in;
//allows the user to configure the details of the customer report, written by Joe
    public createCustomerReport(user loggedin) {
        this.Logged_in = loggedin;
        populateChoice();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateReport cr = new CreateReport(Logged_in);
                cr.main();
                frame.dispose();
            }
        });
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] customerIDsplit = customerSelection.getSelectedItem().toString().split(" ");
                int customerID = Integer.parseInt(customerIDsplit[1]);
                String startDate = startfield.getText();
                String endDate = endfield.getText();
                customerReport cr = new customerReport(Logged_in,customerID,startDate,endDate);
                cr.main();
                frame.dispose();

            }
        });
    }
    public void populateChoice(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement grab = connection.prepareStatement("SELECT customerID,customerName,contactName FROM customer");
            ResultSet grabbed = grab.executeQuery();
            while (grabbed.next()){
                customerSelection.addItem("ID: "+grabbed.getString("customerID")+" Customer: "+grabbed.getString("customerName")+" Contact: "+grabbed.getString("contactName"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }

    }

    public void main(){
        int width = 600;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new createCustomerReport(Logged_in).createcustomerreportpanel);
        frame.setVisible(true);
    }
}
