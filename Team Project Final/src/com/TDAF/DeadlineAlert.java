package com.TDAF;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class DeadlineAlert {
    //Pop-up alert which warns the user of which jobs have yet to be paid for after their deadlines. Created by Tayo
user check;
    public void main(JFrame frame,user checked ){
        this.check = checked;
        if (check.get_alertCheck()==false){
            getConnector connector = new getConnector();
            try (Connection connection = connector.getConnection()) {
                Statement stmt = connection.createStatement();
                //PreparedStatement pst = connection.prepareStatement(" ");
                ResultSet rs = stmt.executeQuery("SELECT * FROM job");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                int new_taskID = 0;
                while(rs.next()){

                    if (now.isAfter(rs.getTimestamp("Deadline").toLocalDateTime()) && rs.getString("Status").equals("Unpaid")){
                        System.out.println("LATE");
                        JOptionPane.showMessageDialog(frame,"Job ID "+rs.getInt("jobID")+" HAS RUN LATE FOR CUSTOMER ID "+rs.getInt("customerID"));
                    }

                }
            }catch (SQLException e1){
                System.out.println(e1);
            }

        }
        check.set_alertCheck();
    }

}
