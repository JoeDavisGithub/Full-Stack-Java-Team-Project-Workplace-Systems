package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;

public class DeleteCustomerAccount {
    private static JFrame frame = new JFrame("DELETE CUSTOMER ACCOUNT");
    private JTextField SearchBar;
    private JButton searchButton;
    private JTextArea customerDetails;
    private JButton deleteButton;
    private JButton backButton;
    private JPanel DeleteCustomerPanel;
    private JComboBox customerSelection;
    private user Logged_in;
    private boolean switcher;
// GUI created by Tayo, code created by Tayo and added to by Joe

    public DeleteCustomerAccount(user loggedin) {
        this.Logged_in = loggedin;
        switcher = false;
        populatedetails();
        populateChoice();


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
                switcher = true;
                customerSelection.removeAllItems();
                populatedetails();
                populateChoice();
                switcher=false;
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
        customerSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(switcher == false){populateChoice();}
            }
        });
    }

    public void main(){
        int width = 650;
        int height = 400;

        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new DeleteCustomerAccount(Logged_in).DeleteCustomerPanel);
        frame.setVisible(true);
    }
    public void populatedetails() {
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement grab = connection.prepareStatement("SELECT * FROM customer");
            ResultSet grabbed = grab.executeQuery();

            while (grabbed.next()) {
                customerSelection.addItem("ID: " + grabbed.getString("customerID") + " Customer Name: " + grabbed.getString("customerName")+" Contact Name: "
                        +grabbed.getString("contactName"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }
    public void populateChoice(){
        customerDetails.setText("");
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement pst = connection.prepareStatement("SELECT * FROM customer WHERE customerID=?");
            String[] staffIDsplit = customerSelection.getSelectedItem().toString().split(" ");
            int StaffID = Integer.parseInt(staffIDsplit[1]);
            pst.setInt(1,StaffID);
            ResultSet rc = pst.executeQuery();
            while (rc.next()){
                customerDetails.append("ID: "+rc.getString("customerID")+"\n");
                customerDetails.append("Customer: "+rc.getString("customerName")+"\n");
                customerDetails.append("Contact: "+rc.getString("contactName")+"\n");
                customerDetails.append("Address: "+rc.getString("customerAddress")+"\n");
                customerDetails.append("Phone: "+rc.getString("customerPhone")+"\n");
                customerDetails.append("Value Status: "+rc.getString("accountType"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }

    }

    public void deleteUser(){
        int response = JOptionPane.showConfirmDialog(frame,"Are you sure you want to delete this account?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            boolean paid = true;
            getConnector connector = new getConnector();
            try (Connection connection = connector.getConnection()) {
                Statement stmt = connection.createStatement();
                PreparedStatement pst = connection.prepareStatement("DELETE FROM customer WHERE customerID = ?");
                PreparedStatement delfix = connection.prepareStatement("DELETE FROM fixed_discounts WHERE customerID = ?");
                PreparedStatement delflex = connection.prepareStatement("DELETE FROM flexible_discounts WHERE customerID = ?");
                PreparedStatement delvar = connection.prepareStatement("DELETE FROM variable_discounts WHERE customerID = ?");
                PreparedStatement checkifpaid = connection.prepareStatement("SELECT * FROM job WHERE customerID = ? AND Status!= ?");
                String[] staffIDsplit = customerSelection.getSelectedItem().toString().split(" ");
                int StaffID = Integer.parseInt(staffIDsplit[1]);
                checkifpaid.setInt(1, StaffID);
                checkifpaid.setString(2, "Paid");
                delfix.setInt(1, StaffID);
                delflex.setInt(1, StaffID);
                delvar.setInt(1, StaffID);
                pst.setInt(1, StaffID);
                ResultSet checkpaid = checkifpaid.executeQuery();
                while (checkpaid.next()) {
                    paid = false;
                }
                if (paid == true) {
                    delfix.executeUpdate();
                    delflex.executeUpdate();
                    delvar.executeUpdate();
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "CUSTOMER ACCOUNT DELETED");
                    //new OfficeManager(Logged_in).main();
                    //frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(frame, "This customer still has jobs to be paid for");
                }


            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error deleting account: " + e.getMessage());
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "Delete Cancelled");
        }

    }

    public void staffSearch(){

            getConnector connector = new getConnector();
            try (Connection connection = connector.getConnection()) {
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM customer,job Where customer.CustomerID = " + SearchBar.getText());

                    while (rs.next()) {
                        customerDetails.setText(
                                "Customer id = " + rs.getString("customerID")+"\n"+
                                        "Customer Name = " + rs.getString("customerName")+"\n"+
                                        "Contact Name = " + rs.getString("contactName")+"\n"+
                                        "Customer Address = " + rs.getString("customerAddress")+"\n"+
                                        "Customer Phone Number = " + rs.getString("customerPhone")+"\n"+
                                        "Customer Account Type = " + rs.getString("accountType")+"\n"+
                                        "Task Amount = " + rs.getString("taskamount")+"\n"+
                                        "Tasks Completed = " + rs.getString("amountCompleted")+"\n");
                    }

            } catch (SQLException es) {
                es.printStackTrace();
            }


    }

}
