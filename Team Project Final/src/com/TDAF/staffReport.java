package com.TDAF;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;

public class staffReport {

    private static JFrame frame = new JFrame("STAFF REPORT");
    private user Logged_in;
    private int staffID;
    private String starts;
    private String ends;
    private JPanel staffreportpanel;
    private JTextArea reportdisplay;
    private JButton backButton;
    private JButton printReportButton;


    public user getLogged_in() {
        return this.Logged_in;
    }
    // GUI created by Joe, code created by Joe, PDF printing created by Tayo

    public staffReport(user loggedin,String start,String end) {
        this.Logged_in = loggedin;
        this.starts = start;
        this.ends = end;
        display();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateReport cr = new CreateReport(Logged_in);
                cr.main();
                frame.dispose();
            }
        });
        printReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream("./src/com/TDAF/resources/staffPerformancePDF.pdf"));
                    document.open();
                    document.add(new Paragraph(reportdisplay.getText()));
                    document.close();
                }catch (Exception e1){
                    System.out.println(e1);
                }
                System.out.println("itext PDF program executed");
            }
        });
    }
    public void display(){
        Timestamp starttime = Timestamp.valueOf(this.starts +" 00:00:00.000");
        Timestamp endtime = Timestamp.valueOf(this.ends +" 00:00:00.000");

        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement getStaff = connection.prepareStatement("SELECT * FROM staff");
            PreparedStatement getTasks = connection.prepareStatement("SELECT * FROM task WHERE StaffID=?");
            PreparedStatement getTaskDetails = connection.prepareStatement("SELECT * FROM taskdetails WHERE task_detailID=?");
            ResultSet staff = getStaff.executeQuery();
            int totaltime = 0;
            while(staff.next()) {
                    int stafftime = 0;
                    reportdisplay.append("Staff ID: " + staff.getString("StaffID") + " / Username: " + staff.getString("Name") + " Role: " + staff.getString("role") + "\n");
                    getTasks.setInt(1, staff.getInt("StaffID"));
                    ResultSet tasks = getTasks.executeQuery();
                    while (tasks.next()) {
                        if ((tasks.getTimestamp("startTime") != null) && ((tasks.getTimestamp("startTime").getTime() > starttime.getTime()) && (tasks.getTimestamp("startTime").getTime() < endtime.getTime()))) {
                            reportdisplay.append("Task ID: " + tasks.getString("taskID") + " / Department: " + tasks.getString("department") + " / Task Start Date: " + tasks.getString("startTime") + " / ");
                            getTaskDetails.setInt(1, tasks.getInt("task_detailID"));
                            ResultSet deet = getTaskDetails.executeQuery();
                            while (deet.next()) {
                                reportdisplay.append("Task Description: " + deet.getString("description") + " / Duration: " + deet.getString("duration") + " mins" + "\n");
                                stafftime += deet.getInt("duration");
                            }
                        }
                    }
                    totaltime += stafftime;
                    reportdisplay.append("Total Effort: " + Integer.toString(stafftime) + " minutes");
                    reportdisplay.append("\n" + "\n");
            }
            reportdisplay.append("Total Staff Effort: "+Integer.toString(totaltime)+" minutes");

        } catch (SQLException es) {
            es.printStackTrace();
        }

    }

    public void main(){
        int width = 1000;
        int height = 600;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new staffReport(Logged_in,starts,ends).staffreportpanel);
        frame.setVisible(true);
    }
}
