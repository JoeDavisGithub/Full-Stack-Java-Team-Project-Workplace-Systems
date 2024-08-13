package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;


public class fixedDiscount {
    private static JFrame frame = new JFrame("Create Fixed Discount");
    private JPanel fixedDiscountPanel;
    private JTextField DiscountRate;
    private JTextArea custdetails;
    private JButton backButton;
    private JButton createButton;
    private user Logged_in;
    private int customer;
    //float f=Float.parseFloat("23.6");
    //window used to create a fixed discount for a given customer, made by Joe
    public fixedDiscount(user loggedin,int custID) {
        this.Logged_in=loggedin;
        this.customer = custID;
        //creates a connection
        getConnector connector = new getConnector();
        try(
                Connection connection = connector.getConnection())
        {
            //finds customer with their
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT customerID,customerName,accountType FROM customer WHERE customerID = "+ Integer.toString(customer) );
            while (rs.next()) {
                int cusID = rs.getInt(1);
                String cusName = rs.getString(2);
                String cusVal = rs.getString(3);
                custdetails.setText("ID: " + Integer.toString(cusID) + " Name: " + cusName + " Account Type: " + cusVal);
            }
            connection.close();

        }catch(
                SQLException e)

        {
            System.out.println(e);
        }



        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDiscount();
                OfficeManager offman = new OfficeManager(Logged_in);
                offman.main();
                frame.dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createDiscount createdisc = new createDiscount(Logged_in);
                createdisc.main();
                frame.dispose();
            }
        });
    }
//method to input the user generated discount into the database
    public void addDiscount(){
    //creates a connection
        getConnector connector = new getConnector();
        try(
                Connection connection = connector.getConnection())
        {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO fixed_discounts(customerID, fixed_rate) VALUES(?,?)");
            pst.setInt(1,customer);
            pst.setFloat(2,Float.parseFloat(DiscountRate.getText()));
            pst.executeUpdate();

        }catch(
                SQLException e)

        {
            System.out.println(e);
        }
    }

    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new fixedDiscount(Logged_in, customer).fixedDiscountPanel);
        frame.setVisible(true);
    }

}
