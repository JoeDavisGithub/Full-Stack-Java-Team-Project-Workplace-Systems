package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class variableDiscount {
    private static JFrame frame = new JFrame("Variable Discount Creator");
    private JPanel variableDiscountPanel;
    private JTextArea custdetails;
    private JTextField discountField;
    private JButton createDiscountButton;
    private JButton backButton;
    private JComboBox taskSelection;
    private user Logged_in;
    private int customer;
    // GUI and code created by Joe
    //window used to create a variable discount for a given customer. Written by Joe.
    public variableDiscount(user loggedin, int custID) {
        this.Logged_in = loggedin;
        this.customer = custID;
        //creates a connection
        getConnector connector = new getConnector();
        try (
                Connection connection = connector.getConnection()) {
            //finds the details about the customer specified previously.
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT customerID,customerName,accountType FROM customer WHERE customerID = " + Integer.toString(customer));
            while (rs.next()) {
                int cusID = rs.getInt(1);
                String cusName = rs.getString(2);
                String cusVal = rs.getString(3);
                custdetails.setText("ID: " + Integer.toString(cusID) + " Name: " + cusName + " Account Type: " + cusVal);
            }
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt.executeQuery("SELECT description FROM taskdetails");
            while (rs2.next()) {
                taskSelection.addItem(rs2.getString(1));
            }
            connection.close();

        } catch (
                SQLException e) {
            System.out.println(e);
        }
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDiscount createdisc = new createDiscount(Logged_in);
                createdisc.main();
                frame.dispose();
            }
        });
        createDiscountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDiscount();
            }
        });
    }
    public void addDiscount(){
        //creates a connection
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            //reads the highest ID in the system to generate the ID of the new user
            Statement stmt = connection.createStatement();
            Statement stmt2 = connection.createStatement();
            ResultSet rs2 = stmt2.executeQuery("SELECT task_detailID from taskdetails WHERE description= '"+taskSelection.getSelectedItem()+"'");
            //prepares statement to input values from the text fields in the form.
            PreparedStatement pst = connection.prepareStatement("INSERT INTO variable_discounts (customerID,task_detailID,task_discount) VALUES (?,?,?)");
            pst.setInt(1,customer);
            while(rs2.next()){
                pst.setInt(2,Integer.parseInt(rs2.getString(1)));
            }
            pst.setFloat(3,Float.parseFloat(discountField.getText()));
            pst.executeUpdate();
            connection.close();
            new OfficeManager(Logged_in).main();
            frame.dispose();

        }catch (SQLException e){
            System.out.println(e);
        }

    }
        public void main(){
            int width = 550;
            int height = 400;
            frame.setSize(width, height);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new variableDiscount(Logged_in, customer).variableDiscountPanel);
            frame.setVisible(true);
        }

    }

