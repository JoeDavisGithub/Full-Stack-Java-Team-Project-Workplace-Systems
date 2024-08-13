package com.TDAF;

//import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

//window that allows new users to be made, created by Joe
public class CreateUserAccount {
    private static JFrame frame = new JFrame("Create BAPERS User");
    private JButton createAccountButton;
    private JTextField usernameField;
    private JComboBox Roles;
    private JTextField passwordField;
    private JPanel createUserAccountPanel;
    private JButton backButton;
    private JTextField emailField;
    private user Logged_in;

    //window that allows new users to be made, GUI created by Tayo, email added by Joe, Validation added by Safee
    public CreateUserAccount(user loggedin) {
        this.Logged_in = loggedin;
        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validationCheck() == true) {
                    if (checkUsername(usernameField.getText()) == false && checkEmail(emailField.getText()) == false)
                        saveStaff();
                    // I attempted to send myself an email but it was unsuccessful, but you can swap out team18tester@outlook.com to EmailTextField.getText()
//                    try {
//                        emailConfirmation.main("team18tester@outlook.com","successful","user email vertified");
//                    } catch (MessagingException messagingException) {
//                        messagingException.printStackTrace();
//                    }

                }
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
    // validations checks if fields are empty then a pop up will show

    public boolean validationCheck() {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (usernameField.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Username Field Empty");
            return false;
        }
        else if (usernameField.getText().equals("username")){
            JOptionPane.showMessageDialog(frame, "Please pick a different username");
            return false;
        }
        else if (passwordField.getText().equals("")){
            JOptionPane.showMessageDialog(frame, "Password Field Empty");
            return false;
        }
        else if (passwordField.getText().equals("password")) {
            JOptionPane.showMessageDialog(frame, "Please pick a different password");
            return false;
        }
        else if (emailField.getText().equals("")){
            JOptionPane.showMessageDialog(frame, "Email Addresss Field Empty");
            return false;
        }
        else if (!emailField.getText().matches(emailRegex)) {
            JOptionPane.showMessageDialog(frame, "Invalid Email Address");
            return false;
        }
        else { return true; }
    }

    public void main(){
        int width = 550;
        int height = 400;

        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new CreateUserAccount(Logged_in).createUserAccountPanel);
        frame.setVisible(true);
    }


    //method of creating and adding a new user to the database created by Joe based on Tayo's original method.
    public void saveStaff(){
        //creates a connection
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            //reads the highest ID in the system to generate the ID of the new user
            Statement stmt = connection.createStatement();
            //prepares statement to input values from the text fields in the form.
            PreparedStatement pst = connection.prepareStatement("INSERT INTO staff (StaffID,Name,email,password,role) VALUES (?,?,?,?,?)");
            ResultSet rs = stmt.executeQuery("SELECT MAX(StaffID) FROM staff");
            int new_ID = 0;
            while (rs.next()) {
                new_ID = rs.getInt(1) + 1;
            }
            pst.setInt(1, new_ID);
            pst.setString(2, usernameField.getText());
            pst.setString(3, emailField.getText());
            pst.setString(4, passwordField.getText());
            pst.setString(5, (String) Roles.getSelectedItem());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(frame, "STAFF ACCOUNT CREATED" + "\n" + "Staff ID: " + new_ID  );
            //JOptionPane.showMessageDialog(frame, "Staff ID: " + new_ID);
            connection.close();
            new OfficeManager(Logged_in).main();
            frame.dispose();

        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    // Checks if username is taken, goes over the whole database to check
    public boolean checkUsername(String username){
        PreparedStatement st;
        ResultSet rs;
        boolean username_exist = false;

        String query = "SELECT * FROM `staff` WHERE `Name`= ?";
        getConnector connector = new getConnector();
        try(Connection connection = connector.getConnection()) {
            st = connector.getConnection().prepareStatement(query);
            st.setString(1,username);
            rs = st.executeQuery();

            if (rs.next()){
                username_exist = true;
                JOptionPane.showMessageDialog(null, "Username already taken, choose another","Username failed", 2);
            }
        }
        catch (SQLException ex){
            Logger.getLogger(CreateUserAccount.class.getName()).log(Level.SEVERE,null,ex);
        }
        return username_exist;
    }
    // Checks if Email is taken, goes over the whole database to check
    public boolean checkEmail(String email){
        PreparedStatement st;
        ResultSet rs;
        boolean email_exist = false;

        String query = "SELECT * FROM `staff` WHERE `Name`= ?";
        getConnector connector = new getConnector();
        try(Connection connection = connector.getConnection()) {
            st = connector.getConnection().prepareStatement(query);
            st.setString(1,email);
            rs = st.executeQuery();

            if (rs.next()){
                email_exist = true;
                JOptionPane.showMessageDialog(null, "Email already taken, choose another","Email failed", 2);
            }

        }
        catch (SQLException ex){
            Logger.getLogger(CreateUserAccount.class.getName()).log(Level.SEVERE,null,ex);
        }
        return email_exist;
    }
}


