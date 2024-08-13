package com.TDAF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class ChangeValuedCustomer {
    private static JFrame frame = new JFrame("Change Customer Value");
    //private JTextField SearchBar;
    private JButton searchButton;
    private JTextArea currentVal;
    private JButton setVal;
    private JButton backButton;
    private JPanel changeValuePanel;
    private JButton setNon;
    private JComboBox customerSelection;
    private int current_customer;
    private user Logged_in;
    private boolean writing;
    Font font;
    //function created by Joe by adapting Tayos old version to JDBC compliant methods. Searches for the user with the ID entered, stores the ID of this customer if found to be accessed
    //by the buttons which alter the customer's account type.

    public ChangeValuedCustomer(user loggedin) {
        this.Logged_in = loggedin;
        populateSelection();
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerSearch();
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

        setVal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valueChange(true);
                customerSearch();
            }
        });
        setNon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                valueChange(false);
                customerSearch();

            }
        });
        // Can not type anything in this field
        currentVal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar()) || !Character.isDigit(e.getKeyCode())){
                    e.consume();
                }
            }
        });
    }


    public void populateSelection(){
        try (Connection connection = new getConnector().getConnection()) {
            String customerIDNameQuery = "SELECT customerID,customerName FROM customer";
            try (PreparedStatement grab = connection.prepareStatement(customerIDNameQuery)) {
                ResultSet rs = grab.executeQuery();
                while (rs.next()){
                    customerSelection.addItem("ID: "+rs.getString("customerID") + " Name: "+rs.getString("customerName"));
                }
            }
        }
        catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void customerSearch(){
        //creates a connection
        try (Connection connection = new getConnector().getConnection()) {
            //finds the customer with the specific input ID
            String sql = "SELECT customerID,customerName,accountType FROM customer WHERE customerID=?";
            try (PreparedStatement grab = connection.prepareStatement(sql)) {
                String[] customerIDsplit = customerSelection.getSelectedItem().toString().split(" ");
                int customerID = Integer.parseInt(customerIDsplit[1]);
                grab.setInt(1, customerID);
                ResultSet rs = grab.executeQuery();
                    while (rs.next()) {
                        int cusID = rs.getInt(1);
                        this.current_customer = cusID;
                        String cusName = rs.getString(2);
                        String cusVal = rs.getString(3);
                        currentVal.setText("ID: " + Integer.toString(cusID) + "\n" + "Name: " + cusName + "\n" + "Account Type: " + cusVal);
                    }
            }
        }
            catch (SQLException e){
            System.out.println(e);
        }
    }
//method updates the currently selected customer's role based off the button selected by the user.
public void valueChange(boolean y){
    //creates a connection
    getConnector connector = new getConnector();
    try (Connection connection = connector.getConnection()) {
        //reads the highest ID in the system to generate the ID of the new user
        Statement stmt = connection.createStatement();
        //prepares statement so that the value
        PreparedStatement pst = connection.prepareStatement("UPDATE customer SET accountType = ? WHERE customerID=?");
        if(y==true){
            pst.setString(1,"Valued");
            pst.setInt(2,this.current_customer);
            pst.executeUpdate();
        }
        else{
            pst.setString(1,"Non-Valued");
            pst.setInt(2,this.current_customer);
            pst.executeUpdate();
        }
    }catch (SQLException e){
        System.out.println(e);
    }
}


    public void main(){
        int width = 550;
        int height = 400;

        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ChangeValuedCustomer(Logged_in).changeValuePanel);

        frame.setVisible(true);
    }
}
