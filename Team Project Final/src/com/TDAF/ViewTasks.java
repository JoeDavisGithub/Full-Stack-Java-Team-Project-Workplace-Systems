package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ViewTasks {
    private static JFrame frame = new JFrame("VIEW TASKS");
    private JPanel ViewTasksPanel;
    private JButton searchButton;
    private JTextArea taskDisplay;
    private JButton completeTaskButton;
    private JButton backButton;
    private JButton beginTaskButton;
    private JButton filterCompleteJobsButton;
    private JButton removeFilterButton;
    private JButton filterIncompleteButton;
    private JComboBox jobIDComboBox;
    private JComboBox taskIDComboBox;
    public user Logged_in;
    boolean switch1;

    public user getLoggedin() {
        return this.Logged_in;
    }

//window for displaying tasks in the system, able to filter complete, incomplete and by JobID. Written by Joe
    public ViewTasks(user loggedin) {
        this.Logged_in = loggedin;
        populateSelectionJobId();
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM task ");
            while (rs.next() ) {
                taskDisplay.append(
                        "Job ID = " + rs.getString("jobID")+" / "+
                                "Task ID = " + rs.getString("taskID")+" / " +
                                "Task Type ID = "+rs.getString("task_detailID")+" / " +
                                "Status = " + rs.getString("status") +" / "+
                                "Department = " + rs.getString("department")+"\n"+
                                "Staff ID = " + rs.getString("StaffID")+" / "+
                                "Start Time =  "+rs.getTimestamp("startTime")+" / "+
                                "Completion Time = "+rs.getTimestamp("completionTime")+"\n"+"\n");
            }

        } catch (SQLException es) {
            es.printStackTrace();
        }

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForTasks();
            }

        });
        completeTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (taskIDComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a task or Search ID");
                }
                else {
                    completeTask();
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
        beginTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (taskIDComboBox.getSelectedItem() == null) {
                    JOptionPane.showMessageDialog(frame, "Please select a task or Search ID");
                }
                else {
                    beginTask();
                }

            }
        });
        filterIncompleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterIncompleteJobs();
            }
        });
        filterCompleteJobsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterCompletedJobs();
            }
        });
        removeFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               removeFilter();
            }
        });
    }

    public void beginTask(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            // checks if task is complete first
            String checkStatusQuery = "SELECT status FROM task WHERE taskID = ?";
            try (PreparedStatement checkStatusStmt = connection.prepareStatement(checkStatusQuery)) {
                checkStatusStmt.setInt(1, Integer.parseInt(taskIDComboBox.getSelectedItem().toString()));
                try (ResultSet statusRs = checkStatusStmt.executeQuery()) {
                    if (statusRs.next() && "Completed".equalsIgnoreCase(statusRs.getString("status"))) {
                        JOptionPane.showMessageDialog(frame, "Cannot Start, Task already Complete");
                        return;
                    }
                }
            }
            PreparedStatement pst = connection.prepareStatement("UPDATE task SET status= ?,StaffID =?, startTime=? WHERE taskID = ? AND status!='Completed' AND status != 'In-Progress'");
            pst.setString(1, "In-Progress");
            pst.setInt(2, Logged_in.getID());
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setInt(4, Integer.parseInt(taskIDComboBox.getSelectedItem().toString()));
            pst.executeUpdate();
            PreparedStatement stmt = connection.prepareStatement("Select * FROM task WHERE taskID =?");
            stmt.setInt(1, Integer.parseInt(taskIDComboBox.getSelectedItem().toString()));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                taskDisplay.setText("");
                taskDisplay.append(
                        "Job ID = " + rs.getString("jobID") + " / " +
                                "Task ID = " + rs.getString("taskID") + " / " +
                                "Task Type ID = " + rs.getString("task_detailID") + " / " +
                                "Status = " + rs.getString("status") + " / " +
                                "Department = " + rs.getString("department") + "\n" +
                                "Staff ID = " + rs.getString("StaffID") + " / " +
                                "Start Time =  " + rs.getTimestamp("startTime") + " / " +
                                "Completion Time = " + rs.getTimestamp("completionTime") + "\n" + "\n");
            }
            JOptionPane.showMessageDialog(frame,"TASK IN-PROGRESS");
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void completeTask(){
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            // checks if task is complete first
            String checkStatusQuery = "SELECT status FROM task WHERE taskID = ?";
            try (PreparedStatement checkStatusStmt = connection.prepareStatement(checkStatusQuery)) {
                checkStatusStmt.setInt(1, Integer.parseInt(taskIDComboBox.getSelectedItem().toString()));
                try (ResultSet statusRs = checkStatusStmt.executeQuery()) {
                    if (statusRs.next() && "Completed".equalsIgnoreCase(statusRs.getString("status"))) {
                        JOptionPane.showMessageDialog(frame, "Task already completed.");
                        return;
                    }
                }
            }

            // if it is not compelte, then call this code
            Statement stmt = connection.createStatement();
            String done = "Completed";
            PreparedStatement jpst = connection.prepareStatement("UPDATE job SET amountcompleted = amountcompleted+1 WHERE jobID = ? AND amountcompleted!=taskamount");
            PreparedStatement updateJobComp = connection.prepareStatement("UPDATE job SET completionDate=? WHERE jobID =? AND (amountcompleted=taskamount)");
            PreparedStatement stopgap = connection.prepareStatement("SELECT jobID FROM task WHERE taskID=?");
            stopgap.setInt(1, Integer.parseInt(taskIDComboBox.getSelectedItem().toString()));
            ResultSet stopgapset = stopgap.executeQuery();
            while(stopgapset.next()){
                jpst.setInt(1, stopgapset.getInt(1));
                updateJobComp.setInt(2, stopgapset.getInt(1));
                updateJobComp.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            }
            jpst.executeUpdate();
            updateJobComp.executeUpdate();

            PreparedStatement pst = connection.prepareStatement("UPDATE task SET status= ?, StaffID = ?, completionTime = ? WHERE taskID = ?");
            pst.setString(1, "Completed");
            pst.setInt(2, Logged_in.getID());
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pst.setInt(4, Integer.parseInt(taskIDComboBox.getSelectedItem().toString()));
            pst.executeUpdate();

            ResultSet rs = stmt.executeQuery("Select * from `task` Where taskID =" + taskIDComboBox.getSelectedItem().toString());
            while (rs.next()) {
                taskDisplay.setText("");
                taskDisplay.append(
                        "Job ID = " + rs.getString("jobID") + " / " +
                                "Task ID = " + rs.getString("taskID") + " / " +
                                "Task Type ID = " + rs.getString("task_detailID") + " / " +
                                "Status = " + rs.getString("status") + " / " +
                                "Department = " + rs.getString("department") + "\n" +
                                "Staff ID = " + rs.getString("StaffID") + " / " +
                                "Start Time =  " + rs.getTimestamp("startTime") + " / " +
                                "Completion Time = " + rs.getTimestamp("completionTime") + "\n" + "\n");
            }
            JOptionPane.showMessageDialog(frame,"TASK COMPLETED");

        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void filterIncompleteJobs(){
        taskDisplay.setText("");
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement jobgap = connection.prepareStatement("SELECT jobID FROM job WHERE taskamount > amountcompleted");
            ResultSet jobsfound = jobgap.executeQuery();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM task WHERE jobID = ?");
            while (jobsfound.next() ) {
                stmt.setInt(1,jobsfound.getInt(1));
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    taskDisplay.append(
                            "Job ID = " + rs.getString("jobID") + " / " +
                                    "Task ID = " + rs.getString("taskID") + " / " +
                                    "Task Type ID = " + rs.getString("task_detailID") + " / " +
                                    "Status = " + rs.getString("status") + " / " +
                                    "Department = " + rs.getString("department") + "\n" +
                                    "Staff ID = " + rs.getString("StaffID") + " / " +
                                    "Start Time =  " + rs.getTimestamp("startTime") + " / " +
                                    "Completion Time = " + rs.getTimestamp("completionTime") + "\n" + "\n");

                }
            }

        } catch (SQLException es) {
            es.printStackTrace();
        }
    }
    public void filterCompletedJobs(){
        taskDisplay.setText("");
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement jobgap = connection.prepareStatement("SELECT jobID FROM job WHERE taskamount = amountcompleted");
            ResultSet jobsfound = jobgap.executeQuery();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM task WHERE jobID = ?");
            while (jobsfound.next() ) {
                stmt.setInt(1,jobsfound.getInt(1));
                ResultSet rs = stmt.executeQuery();
                while(rs.next()) {
                    taskDisplay.append(
                            "Job ID = " + rs.getString("jobID") + " / " +
                                    "Task ID = " + rs.getString("taskID") + " / " +
                                    "Task Type ID = " + rs.getString("task_detailID") + " / " +
                                    "Status = " + rs.getString("status") + " / " +
                                    "Department = " + rs.getString("department") + "\n" +
                                    "Staff ID = " + rs.getString("StaffID") + " / " +
                                    "Start Time =  " + rs.getTimestamp("startTime") + " / " +
                                    "Completion Time = " + rs.getTimestamp("completionTime") + "\n" + "\n");

                }
            }

        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void removeFilter(){
        taskDisplay.setText("");
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM task ");
            while (rs.next() ) {
                taskDisplay.append(
                        "Job ID = " + rs.getString("jobID")+" / "+
                                "Task ID = " + rs.getString("taskID")+" / " +
                                "Task Type ID = "+rs.getString("task_detailID")+" / " +
                                "Status = " + rs.getString("status") +" / "+
                                "Department = " + rs.getString("department")+"\n"+
                                "Staff ID = " + rs.getString("StaffID")+" / "+
                                "Start Time =  "+rs.getTimestamp("startTime")+" / "+
                                "Completion Time = "+rs.getTimestamp("completionTime")+"\n"+"\n");

            }

        } catch (SQLException es) {
            es.printStackTrace();
        }
    }

    public void searchForTasks(){
        taskDisplay.setText("");
        try (Connection connection = new getConnector().getConnection()) {
            String jobIDQuery = "SELECT * FROM task WHERE jobID = ?";
            String taskQuery = "SELECT taskID FROM task WHERE jobID = ?";
            String jobIDComboBoxSelectedItem = (String) jobIDComboBox.getSelectedItem();
            try (PreparedStatement ps = connection.prepareStatement(jobIDQuery)) {
                ps.setInt(1, Integer.parseInt(jobIDComboBoxSelectedItem));
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    taskDisplay.append(
                            "Job ID = " + rs.getString("jobID") + " / " +
                                    "Task ID = " + rs.getString("taskID") + " / " +
                                    "Task Type ID = " + rs.getString("task_detailID") + " / " +
                                    "Status = " + rs.getString("status") + " / " +
                                    "Department = " + rs.getString("department") + "\n" +
                                    "Staff ID = " + rs.getString("StaffID") + " / " +
                                    "Start Time =  " + rs.getTimestamp("startTime") + " / " +
                                    "Completion Time = " + rs.getTimestamp("completionTime") + "\n" + "\n");
                }

            }
            try (PreparedStatement taskps = connection.prepareStatement(taskQuery)) {
                taskps.setInt(1, Integer.parseInt(jobIDComboBoxSelectedItem));
                ResultSet taskRs = taskps.executeQuery();
                taskIDComboBox.removeAllItems(); // Clear previous items
                while (taskRs.next()) {
                    taskIDComboBox.addItem(taskRs.getString("taskID"));
                }
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }
    }


    public void populateSelectionJobId(){
        try(Connection connection = new getConnector().getConnection()){
            String jobQuery = "SELECT jobID FROM job ORDER BY jobID ASC";
            try(Statement stmt = connection.createStatement()){
                ResultSet rs = stmt.executeQuery(jobQuery);
                while (rs.next()) {
                    jobIDComboBox.addItem(rs.getString("jobID"));
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
        frame.setContentPane(new ViewTasks(getLoggedin()).ViewTasksPanel);
        frame.setVisible(true);
    }
}

