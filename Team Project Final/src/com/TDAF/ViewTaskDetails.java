package com.TDAF;

//import sun.security.krb5.internal.crypto.Des;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class ViewTaskDetails {
    private static JFrame frame = new JFrame("View Task Details");
    private JPanel viewtaskdetailpanel;
    private JComboBox detailselection;
    private JTextField priceField;
    private JTextField DescriptionField;
    private JTextField LocationField;
    private JTextField durationfield;
    private JTextField idfield;
    private JButton backButton;
    private JButton updateTaskDetailButton;
    private JButton deleteTaskDetailButton;
    private user Logged_in;
    private boolean switcher =false;
//used to edit task Details created by Joe
    public ViewTaskDetails(user loggedin) {
        this.Logged_in = loggedin;
        populateSelection();
        populateDetails();
        detailselection.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(switcher==false){populateDetails();}

            }
        });
        updateTaskDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTask();
                switcher = true;
                detailselection.removeAllItems();
                populateSelection();
                populateDetails();
                switcher = false;
                JOptionPane.showMessageDialog(frame, "Task Detail Updated");
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OfficeManager t = new OfficeManager(Logged_in);
                t.main();
                frame.dispose();
            }
        });
        deleteTaskDetailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTask();
                switcher = true;
                detailselection.removeAllItems();
                populateSelection();
                populateDetails();
                switcher = false;
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

        durationfield.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                if (!Character.isDigit(e.getKeyChar())){
                    e.consume();
                }
            }

        });
    }
    public void populateSelection(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement stmt1 = connection.prepareStatement("SELECT task_detailID,description FROM taskdetails");
            ResultSet rs = stmt1.executeQuery();
            while (rs.next()){
                detailselection.addItem("ID: "+rs.getString("task_detailID")+" Name: "+rs.getString("description"));
            }
            connection.close();
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }
    public void populateDetails(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            String[] TDIDsplit = detailselection.getSelectedItem().toString().split(" ");
            int TDID = Integer.parseInt(TDIDsplit[1]);
            PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM taskdetails WHERE task_detailID=?");
            stmt1.setInt(1,TDID);
            ResultSet rs = stmt1.executeQuery();
            while (rs.next()){
                idfield.setText(rs.getString("task_detailID"));
                durationfield.setText(rs.getString("duration"));
                DescriptionField.setText(rs.getString("description"));
                LocationField.setText(rs.getString("location"));
                priceField.setText(rs.getString("price"));
            }
            connection.close();
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }
    public void deleteTask(){
        int response = JOptionPane.showConfirmDialog(frame,"Are you sure you want to delete this account?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            getConnector connector = new getConnector();
            try (Connection connection = connector.getConnection()) {
                String[] TDIDsplit = detailselection.getSelectedItem().toString().split(" ");
                int TDID = Integer.parseInt(TDIDsplit[1]);
                PreparedStatement stmt1 = connection.prepareStatement("DELETE FROM taskdetails WHERE task_detailID=?");
                stmt1.setInt(1, TDID);
                stmt1.executeUpdate();
                JOptionPane.showMessageDialog(frame, "Task Detail Deleted");
                connection.close();
            } catch (SQLException es) {
                es.printStackTrace();
            }
        }
        else {
            JOptionPane.showMessageDialog(frame, "Delete Cancelled");
        }
    }

    public void updateTask(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            String[] TDIDsplit = detailselection.getSelectedItem().toString().split(" ");
            int TDID = Integer.parseInt(TDIDsplit[1]);
            PreparedStatement stmt1 = connection.prepareStatement("UPDATE taskdetails SET duration=?,description=?,location=?,price=? WHERE task_detailID=?");
            PreparedStatement taskst = connection.prepareStatement("UPDATE task SET department=? WHERE task_detailID=?");
            stmt1.setInt(1,Integer.parseInt(durationfield.getText()));
            stmt1.setString(2,DescriptionField.getText());
            stmt1.setString(3,LocationField.getText());
            stmt1.setFloat(4,Float.parseFloat(priceField.getText()));
            stmt1.setInt(5,TDID);
            taskst.setString(1,LocationField.getText());
            taskst.setInt(2,TDID);
            stmt1.executeUpdate();
            taskst.executeUpdate();

            connection.close();
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }
    public void main(){
        int width = 600;
        int height = 500;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new ViewTaskDetails(Logged_in).viewtaskdetailpanel);
        frame.setVisible(true);
    }

}
