package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class CreateTaskDetail {
    private static JFrame frame = new JFrame("Create Task Detail");
    private JPanel createTaskDetailPanel;
    private JButton createTaskDetailButton;
    private JButton backButton;
    private JTextField descriptionField;
    private JTextField locationField;
    private JTextField durationField;
    private JTextField priceField;
    private user Logged_in;
//window used to create new task details which allow for new tasks to be implemented
    // into jobs in the future as BIPL advances their technologies.
    //Created by Joe (GUI and code)
    public CreateTaskDetail(user loggedin) {
        this.Logged_in = loggedin;
        createTaskDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createTaskDetail();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManager technician = new OfficeManager(Logged_in);
                technician.main();
                frame.dispose();
            }
        });
        durationField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())){
                    e.consume();
                }
            }
        });
        priceField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())){
                    e.consume();
                }
            }
        });
    }
    //appends the task detail to the database
    public void createTaskDetail(){
        if (validationCheck() == true /*&& checkDescription(descriptionField.getText()) == false*/) {
            //creates a connection
            getConnector connector = new getConnector();
            try (Connection connection = connector.getConnection()) {
                //reads the highest ID in the system to generate the ID of the new user
                Statement stmt = connection.createStatement();
                //prepares statement to input values from the text fields in the form.
                PreparedStatement pst = connection.prepareStatement("INSERT INTO taskdetails (task_detailID,price,description,location,duration) VALUES (?,?,?,?,?)");
                ResultSet rs = stmt.executeQuery("SELECT MAX(task_detailID) FROM taskdetails");
                int new_ID = 0;
                while (rs.next()) {
                    new_ID = rs.getInt(1) + 1;
                }
                pst.setInt(1, new_ID);
                pst.setFloat(2, Float.parseFloat(priceField.getText()));
                pst.setString(3, descriptionField.getText());
                pst.setString(4, locationField.getText());
                pst.setInt(5, Integer.parseInt(durationField.getText()));
                pst.executeUpdate();
                connection.close();
                new OfficeManager(Logged_in).main();
                frame.dispose();

            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }

    public boolean validationCheck() {
        if (descriptionField.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Description Field Empty");
            return false;
        }
        else if (locationField.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Location Field Empty");
            return false;
        }
        else if (durationField.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Duration Field Empty");
            return false;
        }
        else if (priceField.getText().equals("")) {
            JOptionPane.showMessageDialog(frame, "Price Field Empty");
            return false;
        }
        else { return true; }
    }
    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new CreateTaskDetail(Logged_in).createTaskDetailPanel);
        frame.setVisible(true);
    }
}
