package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class DeleteTask {
    private static JFrame frame = new JFrame("DELETE TASK");
    private JTextField SearchBar;
    private JButton searchButton;
    private JButton deleteButton;
    private JButton backButton;
    private JPanel DeleteTaskPanel;
    private JTextArea TaskDetailsField;
    public user Logged_in;

    public user getLogged_in() {
        return this.Logged_in;
    }
    // GUI created by Tayo, code created by Tayo, Validation created by Safee
    public DeleteTask(user loggedin) {
        this.Logged_in = loggedin;
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Technician technician = new Technician(Logged_in);
                technician.main();
                frame.dispose();
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validationCheck() && validationCheck2() == true) {
                    getConnector connector = new getConnector();
                    try (Connection connection = connector.getConnection()) {
                        Statement stmt = connection.createStatement();
                        PreparedStatement pst = connection.prepareStatement("DELETE FROM `task` WHERE `taskID` = " + SearchBar.getText());
                        ResultSet rs = stmt.executeQuery("SELECT MAX(taskID) FROM task");
                        pst.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "TASK DELETED");
                        new OfficeManager(Logged_in).main();
                        frame.dispose();
                    } catch (SQLException es) {
                        System.out.println(es);
                    }
                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validationCheck() == true) {
                    TaskDetailsField.setText("");
                    getConnector connector = new getConnector();
                    try (Connection connection = connector.getConnection()) {
                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM task Where taskID = " + SearchBar.getText());
                        if (!rs.isBeforeFirst()) {
                            JOptionPane.showMessageDialog(frame, "Task ID not found");
                            TaskDetailsField.setText("");
                        } else {
                            while (rs.next()) {
                                TaskDetailsField.setText(
                                        "Job ID = " + rs.getString("jobID") + "\n" +
                                                "Task ID = " + rs.getString("taskID") + "\n" +
                                                "Staff ID = " + rs.getString("StaffID") + "\n" +
                                                "Completion Date = " + rs.getString("completionDate") + "\n" +
                                                "Completion Time = " + rs.getString("completionTime") + "\n" +
                                                "Department = " + rs.getString("department") + "\n" +
                                                "Start Time = " + rs.getString("startTime") + "\n" +
                                                "Status = " + rs.getString("status") + "\n");
                            }
                        }

                    } catch (SQLException es) {
                        es.printStackTrace();
                    }
                }
            }
        });
        SearchBar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())){
                    e.consume();
                }
            }
        });

    }
    public boolean validationCheck() {
        if (SearchBar.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Please enter the Task ID");
            return false;
        }
        else { return true; }
    }

    public boolean validationCheck2(){
        if(TaskDetailsField.getText().equals("")){
            JOptionPane.showMessageDialog(frame,"Please search the correct Task ID");
            return false;
        }
        else{ return true; }
    }


    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new DeleteTask(Logged_in).DeleteTaskPanel);
        frame.setVisible(true);
    }
}
