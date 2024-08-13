package com.TDAF;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ViewCustomerAccount {
    private static JFrame frame = new JFrame("View Customer Account");
    //private JTextField SearchBar;
    //private JButton searchButton;
    private JButton backButton;
    private JPanel ViewPanel;
    private JComboBox customerSelection;
    private JTextField contactNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField customerNameField;
    private JButton updateCustomerButton;
    private JTextField statusField;
    private JTextField emailField;
    //private String data;
    //private String[] customer;
    //private boolean writing;
    public user Logged_in;
    public boolean switcher = false;
    public user getLoggedin() {
        return this.Logged_in;
    }

    // GUI and code readapted by Joe from Tayo local Database
    // Code re-adjusted by Safee
    public ViewCustomerAccount(user loggedin) {
        this.Logged_in = loggedin;
        populateSelection();
        populateDetails();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receptionist receptionist = new Receptionist(Logged_in);
                receptionist.main();
                frame.dispose();
            }
        });
        customerSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(switcher==false){populateDetails();}
            }
        });
        updateCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (validationCheck() == true) {
                    updateCustomer();
                    switcher = true;
                    customerSelection.removeAllItems();
                    populateSelection();
                    populateDetails();
                    switcher = false;
                    JOptionPane.showMessageDialog(frame, "Task Detail Updated");
                }
            }
        });
        phoneField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume();
                }
                // prevents more then 11 numbers to be inputed
                if (phoneField.getText().length() >= 11) {
                    e.consume();
                }
            }
        });

        customerNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                // prevents numbers and special chars to be inputed in customername field
                if(!Character.isLetter(c) && c != ' ' && c != '-' && c != '\'') {
                    e.consume();
                }
            }
        });

        contactNameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                // prevents numbers and special chars to be inputed in customername field
                if(!Character.isLetter(c) && c != ' ' && c != '-' && c != '\'') {
                    e.consume();
                }
            }
        });
    }

    // Updated function is called when button is pressed
    public void updateCustomer(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement customerPush = connection.prepareStatement("UPDATE customer SET customerAddress=?,customerName=?,customerPhone=?,contactName=?,customerEmail=? WHERE customerID=?");
            String[] customerIDsplit = customerSelection.getSelectedItem().toString().split(" ");
            int customerID = Integer.parseInt(customerIDsplit[1]);
            customerPush.setInt(6,customerID);
            customerPush.setString(1, addressField.getText());
            customerPush.setString(2, customerNameField.getText());
            customerPush.setString(3, phoneField.getText());
            customerPush.setString(4, contactNameField.getText());
            customerPush.setString(5, emailField.getText());
            customerPush.executeUpdate();
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void populateSelection(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            Statement stmt1 = connection.createStatement();
            ResultSet rs1 = stmt1.executeQuery("SELECT customerID,customerName FROM customer");
            while (rs1.next()){
                customerSelection.addItem("ID: "+rs1.getString("customerID")+" Name: "+rs1.getString("customerName"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void populateDetails(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement customerGrab = connection.prepareStatement("SELECT * FROM customer WHERE customerID = ?");
            String[] customerIDsplit = customerSelection.getSelectedItem().toString().split(" ");
            int customerID = Integer.parseInt(customerIDsplit[1]);
            customerGrab.setInt(1,customerID);
            ResultSet Grabbed = customerGrab.executeQuery();
            while (Grabbed.next()){
                addressField.setText(Grabbed.getString("customerAddress"));
                customerNameField.setText(Grabbed.getString("customerName"));
                phoneField.setText(Grabbed.getString("customerPhone"));
                contactNameField.setText(Grabbed.getString("contactName"));
                emailField.setText(Grabbed.getString("customerEmail"));
                statusField.setText(Grabbed.getString("accountType"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public boolean validationCheck() {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if(customerNameField.getText().equals("")){
            JOptionPane.showMessageDialog(frame,"Address Field Empty");
            return false;
        }
        else if(addressField.getText().equals("")){
            JOptionPane.showMessageDialog(frame,"Address Field Empty");
            return false;
        }
        else if (contactNameField.getText().equals("")){
            JOptionPane.showMessageDialog(frame, "Contact Name Field Empty");
            return false;
        }
        else if (phoneField.getText().equals("")){
            JOptionPane.showMessageDialog(frame, "Phone Number Field Empty");
            return false;
        }
        else if (emailField.getText().equals("")){
            JOptionPane.showMessageDialog(frame, "Email Addresss Field Empty");
            return false;
        }
        else if (phoneField.getText().length() < 11) {
            JOptionPane.showMessageDialog(null, "Phone number must be at least 11 digits long.");
            return false;
        }
        else if (!emailField.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(frame, "Invalid Email Address");
            return false;
        }

        else { return true; }
    }



    public void main(){
        int width = 675;
        int height = 400;

        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ViewCustomerAccount(Logged_in).ViewPanel);
        frame.setVisible(true);
    }


}
