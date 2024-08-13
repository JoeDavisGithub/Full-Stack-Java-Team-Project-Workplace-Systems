package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class CreateCustomerAccount {
    private static JFrame frame = new JFrame("Customer Creator");
    private JPanel customerCreation;
    private JTextField customerNameField;
    private JTextField contactNameField;
    private JTextField addressField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton backButton;
    private JButton createCustomerButton;

    //Code and GUI by Joe, GUI and Code re-edited and cross-checked by Safee

    public user Logged_in;

    public user getLoggedin() {
        return this.Logged_in;
    }

    public void main(){
        int width = 550;
        int height = 400;

        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new CreateCustomerAccount(Logged_in).customerCreation);
        frame.setVisible(true);
    }

    public CreateCustomerAccount(user loggedin) {
        this.Logged_in = loggedin;
        createCustomerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(validationCheck() == true){
                    saveCustomer();
                }
                /* RESETS THE FIELDS WHEN BUTTON IS PRESSED */
                //CustomerNameField.setText("");
                //AddressField.setText("");
                //ContactNameField.setText("");
                //PhoneField.setText("");
                //EmailField.setText("");
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receptionist receptionist = new Receptionist(getLoggedin());
                receptionist.main();
                frame.dispose();
            }
        });
        // Can only type numbers in the Phone Number field
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
        // only types letters
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
    // validations checks if fields are empty then a pop up will show
    public boolean validationCheck() {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (customerNameField.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Customer Field Empty");
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





    //method to create a new customer account in the database, to charge jobs to.
    //original method written by Tayo, new method with MySQL database integration written by Joe
    public void saveCustomer(){
        //creates a connection
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            //reads the highest ID in the system to generate the ID of the new user
            Statement stmt = connection.createStatement();
            //prepares statement to input values from the text fields in the form.
            PreparedStatement pst = connection.prepareStatement("INSERT INTO customer (customerID,customerName,contactName,customerAddress,customerPhone,customerEmail,accountType) VALUES (?,?,?,?,?,?,?)");
            ResultSet rs = stmt.executeQuery("SELECT MAX(customerID) FROM customer");
            int new_ID = 0;
            while(rs.next()){new_ID = rs.getInt(1)+1;}
            pst.setInt(1,new_ID);
            pst.setString(2, customerNameField.getText());
            pst.setString(3, contactNameField.getText());
            pst.setString(4, addressField.getText());
            pst.setString(5, phoneField.getText());
            pst.setString(6,"Non-Valued");
            pst.setString(7, emailField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(frame,"CUSTOMER CREATED");
            connection.close();
            new Receptionist(Logged_in).main();
            frame.dispose();

        }catch (SQLException e){
            System.out.println(e);
        }
    }
}
