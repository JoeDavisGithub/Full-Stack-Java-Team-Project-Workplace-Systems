package com.TDAF;

//import com.sun.xml.internal.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;
import java.util.Properties;

public class Login {
    private JLabel titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private static JFrame frame = new JFrame("BAPERS");
    private JPanel loginPanel;
    private JTextField IDfield;
    private Scanner reader;
    private String data;
    private static int width = 550;
    private static int height = 400;
    boolean validationCheck2 = false;
    // GUI created by Tayo, database connection set up by Joe, validation created by Safee
    public Login() {
        create_admin();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (validationCheck() == true) {
                    //method of connecting to database and logging in created by Joe, by rebuilding Tayo's version which operated on a text based local database.
                    // Converted to specific search, old version available at bottom.
                    String username = usernameField.getText();
                    String password = passwordField.getText();
                    int id = Integer.parseInt(IDfield.getText());
                    getConnector connector = new getConnector();
                    try (Connection connection = connector.getConnection()) {
                        Statement statement = connection.createStatement();
                        String query = "SELECT Name,password,role,StaffID FROM staff WHERE StaffID='" + id + "' AND Name='" + username + "' AND password='" + password + "'";
                        ResultSet rs = statement.executeQuery(query);
                        while (rs.next()) {
                            String inputName = rs.getString(1);
                            String inputPassword = rs.getString(2);
                            String accRole = rs.getString(3);
                            int accID = rs.getInt(4);
                            if (inputName.equals(username) && inputPassword.equals(password) && accID == id) {
                                //logged in user details stored upon successful credentials matched. This will be passed as a parameter in all future windows generated. until Logged out.
                                user logged_in = new user(inputName, inputPassword, accRole, accID);
                                //generates user role-based window limiting user access to the system and preventing from unnecessary clutter to lower level users.
                                if (accRole.equals("Office Manager")) {
                                    OfficeManager officeManager = new OfficeManager(logged_in);
                                    officeManager.main();
                                } else if (accRole.equals("Receptionist")) {
                                    Receptionist receptionist = new Receptionist(logged_in);
                                    receptionist.main();
                                } else if (accRole.equals("Shift Manager")) {
                                    ShiftManager shiftManager = new ShiftManager(logged_in);
                                    shiftManager.main();
                                } else if (accRole.equals("Technician")) {
                                    Technician technician = new Technician(logged_in);
                                    technician.main();
                                }
                                //disposes of the frame preventing a conflict.
                                connection.close();
                                frame.dispose();
                                validationCheck2 = true;
                                break;
                            }else{
                                JOptionPane.showMessageDialog(frame,"INCORRECT USERNAME OR PASSWORD");
                            }

                        }

                    } catch (SQLException es) {
                        es.printStackTrace();
                    }
                }
            }
        });

        usernameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (usernameField.getText().trim().toLowerCase().equals("username"));
                {
                    usernameField.setText("");
                    usernameField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (usernameField.getText().trim().equals("") || usernameField.getText().trim().toLowerCase().equals("username")){
                    usernameField.setText("username");
                    usernameField.setForeground(new Color(153,153,153));
                }
            }
        });
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                String pass = String.valueOf(passwordField.getPassword());
                if (pass.trim().toLowerCase().equals("password"));
                {
                    passwordField.setText("");
                    passwordField.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String pass = String.valueOf(passwordField.getPassword());
                if (pass.trim().equals("") || pass.trim().toLowerCase().equals("password")){
                    passwordField.setText("password");
                    passwordField.setForeground(new Color(153,153,153));
                }
            }
        });
        // Can only type numbers in the ID field
        IDfield.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if (IDfield.getText().trim().toLowerCase().equals("ID"));
                {
                    IDfield.setText("");
                    IDfield.setForeground(Color.black);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if (IDfield.getText().trim().equals("") || IDfield.getText().trim().toLowerCase().equals("ID")){
                    IDfield.setText("ID");
                    IDfield.setForeground(new Color(153,153,153));
                }
            }
        });
        // Can only type numbers in the ID field
        IDfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())){
                    e.consume();
                }

            }
        });
    }

    public void staffLogin(){

    }

    public static Login start(){
        Login l = new Login();
        frame.setContentPane(l.loginPanel);
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        return l;
    }

    public boolean validationCheck() {
        if (IDfield.getText().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "ID needs atleast 1 character");
            return false;
        }
        else if (usernameField.getText().isEmpty()){
            JOptionPane.showMessageDialog(frame, "Username needs atleast 1 character");
            return false;
        }
        else if (passwordField.getText().isEmpty()){
            JOptionPane.showMessageDialog(frame, "Password needs atleast 1 character");
            return false;
        }
        else { return true; }
    }
    public void create_admin(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            //finds the customer with the specific input ID
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM staff");
            PreparedStatement ps = connection.prepareStatement("INSERT INTO staff (StaffID,Name,email,password,role) VALUES (0,'Admin','ADMININVEMAIL','AdminPassword','Office Manager') ");
            while (rs.next()){
                if (rs.getInt(1)==0){
                    ps.executeUpdate();
                }
            }

        }catch (SQLException e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        Login main = start();
    }
}
