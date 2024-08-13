package com.TDAF;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;


public class summaryReport {
    private static JFrame frame = new JFrame("SUMMARY REPORT");
    private JTextArea reportdisplay;
    private JPanel summaryreportpanel;
    private JButton backButton;
    private JButton printReportButton;
    private user Logged_in;
    private String end;
    private String start;
    private Map Depo_Totals;
    private Map day1totals;
    private Map temptotal;
    private String[] Timesplit;
    private String current;
    // GUI created by Joe, code created by Joe, PDF printing created by Tayo
    public summaryReport(user loggedin, String starts,String ends) {
        this.Logged_in=loggedin;
        this.end=ends;
        this.start=starts;
        Depo_Totals = new HashMap<String,Integer>();
        day1totals = new HashMap<String,Integer>();
        temptotal = new HashMap<String,Integer>();
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
                    PdfWriter.getInstance(document, new FileOutputStream("./src/com/TDAF/resources/summaryPDF.pdf"));
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
        Timestamp starttime = Timestamp.valueOf(this.start +" 00:00:00.000");
        Timestamp endtime = Timestamp.valueOf(this.end +" 00:00:00.000");

        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement getlocales = connection.prepareStatement("SELECT DISTINCT location FROM taskdetails;");
            PreparedStatement getTasks = connection.prepareStatement("SELECT * FROM task WHERE startTime BETWEEN ? AND ? ORDER BY startTime ASC");
            PreparedStatement getTaskDetails = connection.prepareStatement("SELECT * FROM taskdetails WHERE task_detailID=?");
            ResultSet locale = getlocales.executeQuery();
            getTasks.setTimestamp(1,starttime);
            getTasks.setTimestamp(2,endtime);

            while(locale.next()){
                Depo_Totals.put(locale.getString("location"),0);
                day1totals.put(locale.getString("location"),0);
                temptotal.put(locale.getString("location"),0);
            }
            reportdisplay.append("Summary Performance Report for Period :"+start+" - "+end+"\n\n");
            reportdisplay.append("Day Shift 1 (5 am -- 2:30pm)"+"\n");
            ResultSet days1 = getTasks.executeQuery();
            reportdisplay.append("Date                   / ");
            for (Object entry : Depo_Totals.keySet()) {
                reportdisplay.append((String)entry+"                 / ");
            }
            reportdisplay.append("\n");


            current = null;
            while(days1.next()){
                if(current==null){
                    String temp=days1.getString("startTime");
                    Timesplit = temp.split(" ");
                    current = Timesplit[0];
                    Timestamp shiftstart = Timestamp.valueOf(Timesplit[0] +" 05:00:00.000");
                    Timestamp shiftend = Timestamp.valueOf(Timesplit[0] +" 14:30:00.000");
                    Timestamp Thistime = days1.getTimestamp("startTime");
                    getTaskDetails.setInt(1,days1.getInt("task_detailID"));

                    /*if(()&&()) {
                        ResultSet getdur = getTaskDetails.executeQuery();
                        while (getdur.next()) {
                            reportdisplay.append(current + "      / ");
                            int hold = (int) temptotal.get(days1.getString("department")) + getdur.getInt("duration");
                            temptotal.put(days1.getString("department"), hold);
                            int day1hold = (int) day1totals.get(days1.getString("department")) + getdur.getInt("duration");
                            day1totals.put(days1.getString("department"), day1hold);
                            int totalhold = (int) Depo_Totals.get(days1.getString("department")) + getdur.getInt("duration");
                            Depo_Totals.put(days1.getString("department"), totalhold);
                        }
                    }*/
                }
                else{
                    String temp=days1.getString("startTime");
                    Timesplit = temp.split(" ");


                    if(!(current.equals(Timesplit[0]))){
                        current = Timesplit[0];
                        for (Object entry : Depo_Totals.keySet()) {
                            if((int)temptotal.get((String)entry)==0){
                                reportdisplay.append("0 Hours 0 Minutes"+"/ ");
                            }
                            else{
                                int hours = (int)temptotal.get((String)entry)/60;
                                int mins =  (int)temptotal.get((String)entry)%60;
                                reportdisplay.append(Integer.toString(hours)+" Hours "+Integer.toString(mins)+" Minutes"+"/ ");
                            }


                        }
                        for(Object entry : temptotal.keySet()){
                            temptotal.put((String)entry,0);
                        }
                        reportdisplay.append("\n");

                        getTaskDetails.setInt(1,days1.getInt("task_detailID"));
                        ResultSet getdur = getTaskDetails.executeQuery();
                        while(getdur.next()){
                            reportdisplay.append(current+"      / ");
                            int hold = (int)temptotal.get(days1.getString("department"))+getdur.getInt("duration");
                            temptotal.put(days1.getString("department"),hold);
                            int day1hold = (int)day1totals.get(days1.getString("department"))+getdur.getInt("duration");
                            day1totals.put(days1.getString("department"),day1hold);
                            int totalhold = (int)Depo_Totals.get(days1.getString("department"))+getdur.getInt("duration");
                            Depo_Totals.put(days1.getString("department"),totalhold);
                        }

                    } else {
                        getTaskDetails.setInt(1,days1.getInt("task_detailID"));
                        ResultSet getdur = getTaskDetails.executeQuery();
                        while(getdur.next()){
                            int hold = (int)temptotal.get(days1.getString("department"))+getdur.getInt("duration");
                            temptotal.put(days1.getString("department"),hold);
                            int day1hold = (int)day1totals.get(days1.getString("department"))+getdur.getInt("duration");
                            day1totals.put(days1.getString("department"),day1hold);
                            int totalhold = (int)Depo_Totals.get(days1.getString("department"))+getdur.getInt("duration");
                            Depo_Totals.put(days1.getString("department"),totalhold);
                        }


                    }

                }

            }
            for (Object entry : Depo_Totals.keySet()) {
                if((int)temptotal.get((String)entry)==0){
                    reportdisplay.append("0 Hours 0 Minutes"+"/ ");
                }
                else{
                    int hours = (int)temptotal.get((String)entry)/60;
                    int mins =  (int)temptotal.get((String)entry)%60;
                    reportdisplay.append(Integer.toString(hours)+" Hours "+Integer.toString(mins)+" Minutes"+"/ ");
                }
                temptotal.put((String)entry,0);
            }
            reportdisplay.append("\n");
            reportdisplay.append("Totals:           ");
            for (Object entry : day1totals.keySet()) {
                int hours = (int)day1totals.get((String)entry)/60;
                int mins =  (int)day1totals.get((String)entry)%60;
                reportdisplay.append(Integer.toString(hours)+" Hours "+Integer.toString(mins)+" Minutes"+"/ ");
            }
            reportdisplay.append("\n");
            reportdisplay.append("\n");
            current=null;

            reportdisplay.append("\n");
            reportdisplay.append("Day Shift 2 (2.30pm -- 10pm)");

            reportdisplay.append("\n");
            reportdisplay.append("Night Shift 1 (2.30pm -- 10pm)");

            reportdisplay.append("\n");
            reportdisplay.append("Total for Period: ("+start+" - "+end+"): ");



        } catch (SQLException es) {
            es.printStackTrace();
        }

    }


    public void main(){
        int width = 1000;
        int height = 600;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new summaryReport(Logged_in,start,end).summaryreportpanel);
        frame.setVisible(true);
    }
}
