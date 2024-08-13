package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class flexibleDiscount {
    private static JFrame frame = new JFrame("Create Flexible Discount");
    private JPanel flexibleDiscountPanel;
    private JTextArea custdetails;
    private JButton backButton;
    private JButton createDiscountBracketButton;
    private JTextField lowerbound;
    private JTextField upperbound;
    private JTextField discountrate;
    private int customer;
    private user Logged_in;
//window used to create bounds for flexible discounts, Made by Joe
    public flexibleDiscount(user loggedin, int customer) {
        this.Logged_in = loggedin;
        this.customer = customer;
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
            connection.close();

        } catch (
                SQLException e) {
            System.out.println(e);
        }
        createDiscountBracketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDiscount();
                lowerbound.setText("");
                upperbound.setText("");
                discountrate.setText("");
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
    public void addDiscount(){
        //creates a connection
        getConnector connector = new getConnector();
        try(
                Connection connection = connector.getConnection())
        {
            PreparedStatement pst = connection.prepareStatement("INSERT INTO flexible_discounts(customerID, lower_bound,upper_bound,discountrate) VALUES(?,?,?,?)");
            pst.setInt(1,customer);
            pst.setFloat(2,Float.parseFloat(lowerbound.getText()));
            pst.setFloat(3,Float.parseFloat(upperbound.getText()));
            pst.setFloat(4,Float.parseFloat(discountrate.getText()));
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
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new flexibleDiscount(Logged_in, customer).flexibleDiscountPanel);
        frame.setVisible(true);
    }
}
