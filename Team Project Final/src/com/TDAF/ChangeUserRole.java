package com.TDAF;



import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class ChangeUserRole {
    private static JFrame frame = new JFrame();


    private JTextField currentRoleField;
    private JComboBox setRoleComboBox;
    private JButton searchButton;
    private JButton setButton;
    private JButton backButton;
    private JPanel changeUserRolePanel;
    private JTextField usernameField;
    private JComboBox searchUserIDComboBox;
    private user Logged_in;
    private int staffID;

    public user getLoggedin() {
        return this.Logged_in;
    }

    public ChangeUserRole(user logged_in) {
        this.Logged_in = logged_in;
        populateUserIDComboBox();
        //GUI and database connection set up by Tayo, code by Tayo
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManager officeManager = new OfficeManager(Logged_in);
                officeManager.main();
                frame.dispose();
            }
        });

        setButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                roleChange();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                staffSearch();
            }
        });
        // Can not type anything in this field
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar()) || !Character.isDigit(e.getKeyCode())){
                    e.consume();
                }
            }
        });
        // Can not type anything in this field
        currentRoleField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar()) || !Character.isDigit(e.getKeyCode())){
                    e.consume();
                }
            }
        });
    }
  /*
    public boolean validationCheck() {
        if (searchBar.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Please enter the Staff ID");
            return false;
        }
        else { return true; }
    }
    */

    
    private void populateUserIDComboBox() {
        // Connected to the database
        try (Connection connection = new getConnector().getConnection()) {
            String staffQuery = "SELECT staffID FROM staff";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(staffQuery)) {
                while (rs.next()) {
                    searchUserIDComboBox.addItem(rs.getString("StaffID"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving job IDs: " + e.getMessage());
        }

    }
    
    // validations checks if fields are empty then a pop up will show
    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ChangeUserRole(Logged_in).changeUserRolePanel);
        frame.setVisible(true);
    }



    public void staffSearch(){
        String staffIDText = (String) searchUserIDComboBox.getSelectedItem();
        try (Connection connection = new getConnector().getConnection()) {
            String staffQuery = "SELECT * FROM staff WHERE StaffID = ?";
            try (PreparedStatement staffStmt = connection.prepareStatement(staffQuery)) {
                staffStmt.setInt(1, Integer.parseInt(staffIDText));
                try (ResultSet rs = staffStmt.executeQuery()) {
                    if (rs.next()) {
                        currentRoleField.setText(rs.getString("role"));
                        usernameField.setText(rs.getString("Name"));
                    } else {
                        JOptionPane.showMessageDialog(frame, "Staff not found");
                    }
                }
            }
        } catch (SQLException es) {
            es.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving staff user role: " + es.getMessage());
        }
        catch (NumberFormatException es) {
            JOptionPane.showMessageDialog(frame, "Invalid Staff ID format: " + es.getMessage());
        }
    }



    public void roleChange(){
        try (Connection connection = new getConnector().getConnection()) {
            String roleQuery = "UPDATE staff SET role = ? WHERE StaffID = ?";
            try(PreparedStatement roleStmt = connection.prepareStatement(roleQuery)) {
                roleStmt.setString(1, setRoleComboBox.getSelectedItem().toString());
                roleStmt.setString(2, searchUserIDComboBox.getSelectedItem().toString());

                roleStmt.executeUpdate();
                if (usernameField.getText().equals("")) {
                    JOptionPane.showMessageDialog(frame,"Please search the correct Staff ID");
                } else {
                    JOptionPane.showMessageDialog(frame, "Role Change Successful" + "\n" + "New Role: " + setRoleComboBox.getSelectedItem().toString());
                    staffSearch();
                }
            }

        } catch (SQLException es) {
            es.printStackTrace();
        }
    }



}
