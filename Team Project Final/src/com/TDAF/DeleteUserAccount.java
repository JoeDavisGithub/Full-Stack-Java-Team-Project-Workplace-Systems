package com.TDAF;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class DeleteUserAccount{
    private static JFrame frame = new JFrame("Delete User Account");
    private JTextField SearchBar;
    private JButton deleteButton;
    private JPanel DeleteUserAccountPanel;
    private JButton backButton;
    private JTextArea detailarea;
    private JComboBox userSelection;
    private String data;
    private String[] staff;
    private user Logged_in;
    private boolean writing;
    private boolean switcher;

    // GUI created by Tayo, code created by Tayo, (Cross checked by Safee, Tested and Extra code added by Safee)
    public DeleteUserAccount(user loggedin) {
        this.Logged_in = loggedin;
        switcher=false;
        Start();

        populatedetails();
        populateChoice();
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
                switcher = true;
                userSelection.removeAllItems();
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


        userSelection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(switcher == false){populateChoice();}
            }
        });
    }

    public void main(){
        int width = 550;
        int height = 400;

        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new DeleteUserAccount(Logged_in).DeleteUserAccountPanel);
        frame.setVisible(true);
    }
    public void populatedetails() {
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement grab = connection.prepareStatement("SELECT * FROM staff WHERE StaffID!=?");
            grab.setInt(1, Logged_in.getID());
            ResultSet grabbed = grab.executeQuery();
            while (grabbed.next()) {
                userSelection.addItem("ID: " + grabbed.getString("StaffID") + " User Name: " + grabbed.getString("Name"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void populateChoice(){
        detailarea.setText("");
        try (Connection connection = new getConnector().getConnection()) {
            String staffIDQuery = "SELECT * FROM staff WHERE StaffID=?";
            try (PreparedStatement pst = connection.prepareStatement(staffIDQuery)) {
                String[] staffIDsplit = userSelection.getSelectedItem().toString().split(" ");
                int StaffID = Integer.parseInt(staffIDsplit[1]);
                pst.setInt(1, StaffID);
                ResultSet rc = pst.executeQuery();
                if (rc.next()) {
                    detailarea.append("ID: " + rc.getString("StaffID") + "\n");
                    detailarea.append("Name: " + rc.getString("Name") + "\n");
                    detailarea.append("Password: " + rc.getString("password") + "\n");
                    detailarea.append("Email: " + rc.getString("email") + "\n");
                    detailarea.append("Role: " + rc.getString("role") + "\n");
                }
                else {
                    detailarea.append("No staff member found with the given ID.");
                }
            }
        }
        catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void deleteUser(){
        int response = JOptionPane.showConfirmDialog(frame,"Are you sure you want to delete this account?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            //connect to database
            try (Connection connection = new getConnector().getConnection()) {
                //create a string query for deleting
                String dltQuery = "DELETE FROM staff WHERE StaffID=?";
                //prepare statment for the query to validate the string dltQuery
                try (PreparedStatement dltStmt = connection.prepareStatement(dltQuery)) {
                    //use the jcombobox as an array (userSelection) as it has both staffId and username
                    String[] staffIDsplit = userSelection.getSelectedItem().toString().split(" ");
                    // take out the staffID from the userSelection making it a normal Int to use for the parse on String dltQuery
                    int StaffID = Integer.parseInt(staffIDsplit[1]);
                    // as shown in the string dltQuery, the "?" you need a paraemeter index for StaffID which is the statement below
                    dltStmt.setInt(1, StaffID);
                    //execute and update, deleteing the staff
                    dltStmt.executeUpdate();
                    JOptionPane.showMessageDialog(frame, "STAFF ACCOUNT DELETED");
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error deleting account: " + e.getMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(frame, "Delete Cancelled");
        }
    }


    public void Start(){
        detailarea.setEditable(false);
    }






}
