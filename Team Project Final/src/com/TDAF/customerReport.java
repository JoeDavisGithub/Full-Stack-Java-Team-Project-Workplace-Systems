package com.TDAF;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;

public class customerReport {
    private static JFrame frame = new JFrame("CUSTOMER REPORT");
    private JPanel customerreportpanel;
    private JTextArea reportDisplay;
    private JButton backButton;
    private JButton printReportButton;
    private user Logged_in;
    private int custID;
    private String start;
    private String end;
    //Displays  customer report, written by Joe, printing/PDF creation written by Tayo
    public customerReport(user loggedin,int custid,String startdate,String enddate) {
        this.Logged_in = loggedin;
        this.custID = custid;
        this.start = startdate;
        this.end = enddate;
        display();
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShiftManager shiftManager = new ShiftManager(Logged_in);
                shiftManager.main();
                frame.dispose();
            }
        });
        printReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream("./src/com/TDAF/resources/customerPDF.pdf"));
                    document.open();
                    document.add(new Paragraph(reportDisplay.getText()));
                    document.close();
                }catch (Exception e1){
                    System.out.println(e1);
                }
                System.out.println("itext PDF program executed");
            }
        });
    }
    public void main(){
        int width = 1200;
        int height = 600;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new customerReport(Logged_in,custID,start,end).customerreportpanel);
        frame.setVisible(true);
    }

    public void display(){
        Timestamp starttime = Timestamp.valueOf(this.start +" 00:00:00.000");
        Timestamp endtime = Timestamp.valueOf(this.end +" 00:00:00.000");

        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement getCustomer = connection.prepareStatement("SELECT * FROM customer WHERE customerID=?");
            PreparedStatement getJobs = connection.prepareStatement("SELECT * FROM job WHERE customerID=?");
            PreparedStatement getTaskdetailID = connection.prepareStatement("SELECT * FROM task WHERE jobID=?");
            PreparedStatement getTaskDescription = connection.prepareStatement("SELECT * FROM taskdetails WHERE task_detailID=?");
            getCustomer.setInt(1,custID);
            ResultSet customer = getCustomer.executeQuery();
            getJobs.setInt(1,custID);
            while(customer.next()){
                reportDisplay.append("Period Report For Customer: ID: "+customer.getString("customerID")+
                        " Customer: "+customer.getString("customerName")+
                        " Contact Name: "+customer.getString("contactName")+"\n"+"\n");
            }
            ResultSet Jobs = getJobs.executeQuery();
            while(Jobs.next()) {
                Boolean JobPass = false;
                if(Jobs.getTimestamp("completiondate")!=null){
                    if(((Jobs.getTimestamp("completiondate").getTime()>starttime.getTime())&&(Jobs.getTimestamp("completiondate").getTime()<endtime.getTime()))){
                        JobPass =true;}
                    }
                if(Jobs.getTimestamp("startDate")!=null){
                    if(((Jobs.getTimestamp("startDate").getTime()>starttime.getTime())&&(Jobs.getTimestamp("startDate").getTime()<endtime.getTime()))){
                        JobPass =true;}
                }
                if (JobPass==true) {
                    reportDisplay.append("Job ID: " + Jobs.getString("jobID") + " Description of the Work: " + "\n");
                    getTaskdetailID.setInt(1, Jobs.getInt("jobID"));
                    ResultSet task = getTaskdetailID.executeQuery();
                    while (task.next()) {
                        reportDisplay.append("Task Detail ID: "+task.getString("task_detailID")+" / Completed By Staff ID: "+task.getString("taskID")+" / Start Date: "+
                                task.getString("startTime")+" / Completion Date: "+task.getString("completionTime")+" / ");
                        getTaskDescription.setInt(1,task.getInt("task_detailID"));
                        ResultSet taskdetail = getTaskDescription.executeQuery();
                        while(taskdetail.next()){
                            reportDisplay.append(" Task: "+taskdetail.getString("description")+" / Department: "+taskdetail.getString("location")+"\n");
                        }
                    }
                }
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }

    }
}
