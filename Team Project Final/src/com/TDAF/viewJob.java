package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class viewJob {
    private static JFrame frame = new JFrame("VIEW JOBS");
    private JPanel viewJobPanel;
    private JTextArea jobDisplay;
    private JButton backButton;
    private JButton filterCompletedJobsButton;
    private JButton searchButton;
    private JButton removeFilterButton;
    private JButton filterIncompleteJobsButton;
    private JComboBox jobIdComboBox;
    private user Logged_in;
//Window able to view Jobs, filterable by completed, not completed and by JobID, written by Joe
    public viewJob(user loggedin){
        this.Logged_in = loggedin;
        populateSelection();
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM job ");
            while (rs.next() ) {
                jobDisplay.append(
                        "Job ID = " + rs.getString("jobID")+" / "+
                                "Urgency = " + rs.getString("UrgencyLvl")+" / " +
                                "Price = £"+rs.getString("Price")+" / " +
                                "Status = " + rs.getString("Status") +" / "+
                                "Deadline = " + rs.getString("Deadline")+"\n"+
                                "Job Special Details = " + rs.getString("jobDesc")+" / "+
                                "Task Total =  "+rs.getInt("taskamount")+" / "+
                                "Tasks Completed: = "+rs.getInt("amountcompleted")+"\n"+
                                "Completion Date: = "+rs.getTimestamp("completionDate")+" / "+
                                "Customer ID: = "+rs.getInt("customerID")+"\n"+"\n");

            }

        } catch (SQLException es) {
            es.printStackTrace();
        }
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    jobDisplay.setText("");
                    getConnector connector = new getConnector();
                    try (Connection connection = connector.getConnection()) {
                        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM job Where jobID = ?");
                        stmt.setInt(1, Integer.parseInt(jobIdComboBox.getSelectedItem().toString()));
                        ResultSet rs = stmt.executeQuery();
                        if (!rs.isBeforeFirst()) {
                            JOptionPane.showMessageDialog(frame, "Job ID not found");
                            jobDisplay.setText("");
                        } else {
                            while (rs.next()) {
                                jobDisplay.append(
                                        "Job ID = " + rs.getString("jobID") + " / " +
                                                "Urgency = " + rs.getString("UrgencyLvl") + " / " +
                                                "Price = £" + rs.getString("Price") + " / " +
                                                "Status = " + rs.getString("Status") + " / " +
                                                "Deadline = " + rs.getString("Deadline") + "\n" +
                                                "Job Special Details = " + rs.getString("jobDesc") + " / " +
                                                "Task Total =  " + rs.getInt("taskamount") + " / " +
                                                "Tasks Completed: = " + rs.getInt("amountcompleted") + "\n" +
                                                "Completion Date: = " + rs.getTimestamp("completionDate") + " / " +
                                                "Customer ID: = " + rs.getInt("customerID") + "\n" + "\n");


                            }
                        }

                    } catch (SQLException es) {
                        es.printStackTrace();
                    }
            }
        });
        filterCompletedJobsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobDisplay.setText("");
                getConnector connector = new getConnector();
                try (Connection connection = connector.getConnection()) {
                    PreparedStatement jobgap = connection.prepareStatement("SELECT * FROM job WHERE taskamount = amountcompleted");
                    ResultSet jobsfound = jobgap.executeQuery();
                    while(jobsfound.next()) {
                        jobDisplay.append(
                                "Job ID = " + jobsfound.getString("jobID")+" / "+
                                        "Urgency = " + jobsfound.getString("UrgencyLvl")+" / " +
                                        "Price = £"+jobsfound.getString("Price")+" / " +
                                        "Status = " + jobsfound.getString("Status") +" / "+
                                        "Deadline = " + jobsfound.getString("Deadline")+"\n"+
                                        "Job Special Details = " + jobsfound.getString("jobDesc")+" / "+
                                        "Task Total =  "+jobsfound.getInt("taskamount")+" / "+
                                        "Tasks Completed: = "+jobsfound.getInt("amountcompleted")+"\n"+
                                        "Completion Date: = "+jobsfound.getTimestamp("completionDate")+" / "+
                                        "Customer ID: = "+jobsfound.getInt("customerID")+"\n"+"\n");
                    }

                } catch (SQLException es) {
                    es.printStackTrace();
                }

            }
        });
        filterIncompleteJobsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobDisplay.setText("");
                getConnector connector = new getConnector();
                try (Connection connection = connector.getConnection()) {
                    PreparedStatement jobgap = connection.prepareStatement("SELECT * FROM job WHERE taskamount > amountcompleted");
                    ResultSet jobsfound = jobgap.executeQuery();
                    while(jobsfound.next()) {
                        jobDisplay.append(
                                "Job ID = " + jobsfound.getString("jobID")+" / "+
                                        "Urgency = " + jobsfound.getString("UrgencyLvl")+" / " +
                                        "Price = £"+jobsfound.getString("Price")+" / " +
                                        "Status = " + jobsfound.getString("Status") +" / "+
                                        "Deadline = " + jobsfound.getString("Deadline")+"\n"+
                                        "Job Special Details = " + jobsfound.getString("jobDesc")+" / "+
                                        "Task Total =  "+jobsfound.getInt("taskamount")+" / "+
                                        "Tasks Completed: = "+jobsfound.getInt("amountcompleted")+"\n"+
                                        "Completion Date: = "+jobsfound.getTimestamp("completionDate")+" / "+
                                        "Customer ID: = "+jobsfound.getInt("customerID")+"\n"+"\n");
                    }

                } catch (SQLException es) {
                    es.printStackTrace();
                }
            }
        });
        removeFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jobDisplay.setText("");
                getConnector connector = new getConnector();
                try (Connection connection = connector.getConnection()) {
                    Statement stmt = connection.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * FROM job ");
                    while (rs.next() ) {
                        jobDisplay.append(
                                "Job ID = " + rs.getString("jobID")+" / "+
                                        "Urgency = " + rs.getString("UrgencyLvl")+" / " +
                                        "Price = £"+rs.getString("Price")+" / " +
                                        "Status = " + rs.getString("Status") +" / "+
                                        "Deadline = " + rs.getString("Deadline")+"\n"+
                                        "Job Special Details = " + rs.getString("jobDesc")+" / "+
                                        "Task Total =  "+rs.getInt("taskamount")+" / "+
                                        "Tasks Completed: = "+rs.getInt("amountcompleted")+"\n"+
                                        "Completion Date: = "+rs.getTimestamp("completionDate")+" / "+
                                        "Customer ID: = "+rs.getInt("customerID")+"\n"+"\n");

                    }

                } catch (SQLException es) {
                    es.printStackTrace();
                }
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Technician technician = new Technician(Logged_in);
                technician.main();
                frame.dispose();
            }
        });
    }

    public void populateSelection(){
        try(Connection connection = new getConnector().getConnection()){
            String jobQuery = "SELECT jobID FROM job ORDER BY jobID ASC";
            try(Statement stmt = connection.createStatement()){
                ResultSet rs = stmt.executeQuery(jobQuery);
                while (rs.next()) {
                    jobIdComboBox.addItem(rs.getString("jobID"));
                }
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void main(){
        int width = 950;
        int height = 500;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new viewJob(Logged_in).viewJobPanel);
        frame.setVisible(true);
    }
}
