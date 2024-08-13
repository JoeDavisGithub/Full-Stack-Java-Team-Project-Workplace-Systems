package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
//option window which allows a user to add a discount to a user. created by Joe
public class createDiscount {
    private static JFrame frame = new JFrame("Create Customer Discount");
    private JPanel createDiscountPanel;
    private JTextArea display;
    private JButton searchButton;
    private JButton fixedDiscButton;
    private JButton variableDiscButton;
    private JButton flexibleDiscButton;
    private JButton backButton;
    private JComboBox customerSelection;
    private user Logged_in;
    private int current_customer;
    private boolean valueSwitch;


    public createDiscount(user loggedin) {
        this.Logged_in = loggedin;
        populatedetails();

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchforVal();
            }
        });
        fixedDiscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (display.getText().equals("")) {JOptionPane.showMessageDialog(frame, "Please Search a Customer");}
                else if (valueSwitch == true) {
                    fixedDiscount fixd = new fixedDiscount(Logged_in, current_customer);
                    fixd.main();
                    frame.dispose();
                }
                else {JOptionPane.showMessageDialog(frame, "Customer is not Valued");}

            }
        });
        variableDiscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (display.getText().equals("")) {JOptionPane.showMessageDialog(frame, "Please Search a Customer");}
                else if (valueSwitch == true) {
                    variableDiscount vard = new variableDiscount(Logged_in, current_customer);
                    vard.main();
                    frame.dispose();
                }
                else {JOptionPane.showMessageDialog(frame, "Customer is not Valued");}
            }
        });
        flexibleDiscButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (display.getText().equals("")) {JOptionPane.showMessageDialog(frame, "Please Search a Customer");}
                else if (valueSwitch == true) {
                    flexibleDiscount flexd = new flexibleDiscount(Logged_in, current_customer);
                    flexd.main();
                    frame.dispose();
                }
                else {JOptionPane.showMessageDialog(frame, "Customer is not Valued");}

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

    public void populatedetails() {
        try (Connection connection = new getConnector().getConnection()) {
            String customerQuery = "SELECT customerID,customerName FROM customer";
            try (PreparedStatement grab = connection.prepareStatement(customerQuery)) {
                ResultSet grabbed = grab.executeQuery();
                while (grabbed.next()) {
                    customerSelection.addItem("ID: " + grabbed.getString("customerID") +
                            " Customer Name: " + grabbed.getString("customerName"));
                }
            }
        }catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void searchforVal(){
        display.setText("");
        //creates a connection
            try (Connection connection =  new getConnector().getConnection()) {
                //finds customer with their
                String searchCustomerQuery = "SELECT * FROM customer WHERE customerID = ?";
                try (PreparedStatement grab = connection.prepareStatement(searchCustomerQuery)) {
                    String[] customerIDText = customerSelection.getSelectedItem().toString().split(" ");
                    int custID = Integer.parseInt(customerIDText[1]);
                    grab.setInt(1,custID);
                    ResultSet rs = grab.executeQuery();
                        if (rs.next() && (rs.getString("accountType").equals("Valued"))) {
                            this.current_customer = rs.getInt("customerID");
                            display.append("Customer ID: " + rs.getString("customerID") + "\n");
                            display.append("Customer Name: " + rs.getString("customerName") + "\n");
                            display.append("Tele: " + rs.getString("customerPhone")+ "\n");
                            display.append("Email: " + rs.getString("customerEmail")+ "\n");
                            display.append("Value: " + rs.getString("accountType")+ "\n");
                            valueSwitch = true;
                        }
                        else {
                            JOptionPane.showMessageDialog(frame,"Customer not Valued");
                            valueSwitch = false;
                        }
                }
            }catch (
                    SQLException e) {
                System.out.println(e);
            }
    }


    public void main(){
        int width = 800;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new createDiscount(Logged_in).createDiscountPanel);
        frame.setVisible(true);
    }

}




