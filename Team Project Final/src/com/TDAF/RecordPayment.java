package com.TDAF;

//import sun.lwawt.macosx.CPrinterDevice;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RecordPayment {
    private static JFrame frame = new JFrame("RECORD PAYMENT");
    private JComboBox<String> jobIDComboBox;
    private JTextArea jobDetails;
    private JButton cardPaymentButton;
    private JButton recordCardPaymentButton;
    private JButton backButton;
    private JButton searchButton;
    private JPanel RecordPaymentLabel;
    private JButton recordCashPaymentButton;
    private user Logged_in;
    private int jobID;
    private int customerID;
    private float thisPrice;

    public user getLoggedin() {
        return this.Logged_in;
    }

    public RecordPayment(user logged_in) {
        this.Logged_in = logged_in;
        populateJobIDComboBox();

        recordCardPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardPayment paycard = new CardPayment(Logged_in, jobID, thisPrice, customerID);
                paycard.main();
                frame.dispose();
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receptionist receptionist = new Receptionist(Logged_in);
                receptionist.main();
                frame.dispose();
            }
        });

        recordCashPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recordCashPayment();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchForJob();
            }
        });
    }

    private void populateJobIDComboBox() {
        // Connected to the database
        try (Connection connection = new getConnector().getConnection()) {
            //Selects jobID collum from the "job" table, important for populating the JComboBox
            String jobQuery = "SELECT jobID FROM job ORDER BY jobID ASC";
            //the statment is used to send SQL queries to the database
            try (Statement stmt = connection.createStatement();
                 //This actually executes the jobQuery, currently it was only initialized but not executed.
                 ResultSet rs = stmt.executeQuery(jobQuery)) {
                // loops the jobID Collumn and adds all the IDs to the jobIDComboBox
                while (rs.next()) {
                    jobIDComboBox.addItem(rs.getString("jobID"));
                }
            }
            //SQL error exception occurs, a must have
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error retrieving job IDs: " + e.getMessage());
        }
    }


    public void recordCashPayment() {

        String jobIDText = (String) jobIDComboBox.getSelectedItem();
        if (jobIDText == null || jobIDText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a job ID.");
            return;
        }

        try (Connection connection = new getConnector().getConnection()) {
            String updateJobStatusQuery = "UPDATE `job` SET `Status`= ? WHERE `jobID` = ?";
            String insertPaymentQuery = "INSERT INTO `payment`(`paymentID`, `amount`, `date`, `type`, `customerID`, `jobID`) VALUES (?,?,?,?,?,?)";

            try (PreparedStatement updateStmt = connection.prepareStatement(updateJobStatusQuery);
                 PreparedStatement insertStmt = connection.prepareStatement(insertPaymentQuery)) {

                updateStmt.setString(1, "Paid");
                updateStmt.setInt(2, Integer.parseInt(jobIDText));

                int newPaymentID = getNextPaymentID(connection);
                float newAmount = this.thisPrice;
                Timestamp newDate = new Timestamp(System.currentTimeMillis());
                String newType = "Cash";

                insertStmt.setInt(1, newPaymentID);
                insertStmt.setFloat(2, newAmount);
                insertStmt.setTimestamp(3, newDate);
                insertStmt.setString(4, newType);
                insertStmt.setInt(5, this.customerID);
                insertStmt.setInt(6, this.jobID);

                updateStmt.executeUpdate();
                insertStmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Cash payment recorded successfully.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error recording cash payment.");
        }
    }

    private int getNextPaymentID(Connection connection) throws SQLException {
        String query = "SELECT MAX(paymentID) FROM payment";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        }
        return 1; // Start from 1 if no payments exist
    }

    public void searchForJob() {
        // Starts by Clearing jobDetails text
        jobDetails.setText("");
        //Gets the Selected jobID from the ComboBox and stores it in jobIDtext
        String jobIDText = (String) jobIDComboBox.getSelectedItem();

        // checks if is a selected jobID, then returns with the text (this can occur if we displayed an empty string)
        if (jobIDText == null || jobIDText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a job ID.");
            return;
        }

        //connects to the database
        try (Connection connection = new getConnector().getConnection()) {
            //selects all the jobID from the jobs database and then given the selected value which is "?", it is then stored in jobQuery
            String jobQuery = "SELECT * FROM job WHERE jobID = ?";
            //Safely prepares and stores the statment which is the jobQuery
            try (PreparedStatement jobStmt = connection.prepareStatement(jobQuery)) {
                //specifies the jobID in the jobIDCombobox when clicking the search button.
                //so if jobID 1 was chosen in the jobIDComboBox, the 1 is stored as the ?.  e.g. (SELECT * FROM job WHERE job ID = 1).
                jobStmt.setInt(1, Integer.parseInt(jobIDText));
                //Once it is stored in jobStmt, we execute the SQL Query, storing the final result inside ResultSet
                try (ResultSet rs = jobStmt.executeQuery()) {
                    //checks if there is a job for that jobID, if there is, it oppens the display function with the results given
                    //if not here it will give the "else" statment
                    if (rs.next()) {
                        displayJobDetails(rs, connection);
                    } else {
                        jobDetails.setText("No job found with the given ID.");
                    }
                }
            }
            //1st Catch, a general SQL error showing that it is an job error.
            //2nd Catch, if the JobID is not a number it will give an SQL error
        } catch (SQLException e) {
            e.printStackTrace();  // Print the stack trace for debugging
            JOptionPane.showMessageDialog(frame, "Error retrieving job details: " + e.getMessage());
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid Job ID format: " + e.getMessage());
        }
    }

    // displayjobdetails only works if the "rs" and "connection" variable is given
    private void displayJobDetails(ResultSet rs, Connection connection) throws SQLException {

        // using the rs variable, you are obtaining the result and storing it in the jobID/customerID/thisPrice class variable
        // all 3 variables are needed for displaying and recording the details
        this.jobID = rs.getInt("jobID");
        this.customerID = rs.getInt("customerID");
        this.thisPrice = rs.getFloat("Price");

        //this is what is being displayed on the jobDetails textbox
        //using the rs value to get each collum of the "job" database
        jobDetails.append("Job ID: " + rs.getString("jobID") + "\n");
        jobDetails.append("Urgency Level: " + rs.getString("UrgencyLvl") + "\n");
        jobDetails.append("Price: £" + rs.getString("Price") + "\n");
        jobDetails.append("Status: " + rs.getString("Status") + "\n");
        jobDetails.append("Deadline: " + rs.getString("Deadline") + "\n");
        jobDetails.append("Job Special Details: " + rs.getString("jobDesc") + "\n");
        jobDetails.append("Completion Date: " + rs.getString("completionDate") + "\n");

        //selects all the customerID from the Customer database and then given the selected value which is "?", it is then stored in customerQuery
        String customerQuery = "SELECT * FROM Customer WHERE customerID = ?";
        // prepares the customerquery
        try (PreparedStatement customerStmt = connection.prepareStatement(customerQuery)) {
            //gives the customer ID number to the selected customer
            customerStmt.setInt(1, this.customerID);
            //shows results and executes the query
            try (ResultSet rs2 = customerStmt.executeQuery()) {
                //checks if rs2.next returns the query results, if so then display the results which is in breackets of the if statement
                if (rs2.next()) {
                    jobDetails.append("Customer ID: " + rs2.getString("customerID") + "\n");
                    jobDetails.append("Customer Name: " + rs2.getString("customerName") + "\n");
                    jobDetails.append("Contact Name: " + rs2.getString("contactName") + "\n");
                }
            }
        }
    }

    public void main() {
        int width = 550;
        int height = 400;
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new RecordPayment(Logged_in).RecordPaymentLabel);
        frame.setVisible(true);
    }
}
