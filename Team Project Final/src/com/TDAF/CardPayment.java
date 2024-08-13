package com.TDAF;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;


public class CardPayment {
    private static JFrame frame = new JFrame("CARD PAYMENT");
    private JTextField cardTypefield;
    private JTextField cardHolderField;
    private JTextField ExpiryDateField;
    private JTextField lastfourfield;
    private JButton submitPaymentButton;
    private JButton backButton;
    private JPanel CardPaymentPanel;
    public user Logged_in;
    private int jobID;
    private float thisPrice;
    private int customerID;
    public user getLoggedin() {
        return this.Logged_in;
    }
    private int payID;
//window that allows a user to attach card details to a payment. Reworked by Joe from Tayo's old local db version. Reworked by Tayo to fix bugs/errors
// CardPay() Rewritten by Tayo to fix bugs/errors
    public CardPayment(user logged_in,int jobid,float thisprice,int customerid ) {
        this.Logged_in = logged_in;
        this.jobID = jobid;
        this.thisPrice = thisprice;
        this.customerID = customerid;
        submitPaymentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CashPay();
                Receptionist receptionist = new Receptionist(Logged_in);
                receptionist.main();
                frame.dispose();
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Receptionist receptionist = new Receptionist(getLoggedin());
                receptionist.main();
                frame.dispose();
            }
        });
    }
    public void CashPay(){
        //creates a connection
        getConnector connector = new getConnector();
        try(Connection connection = connector.getConnection())
        {
            //creates a new record of the payment
            Statement stmt = connection.createStatement();
            PreparedStatement pst1 = connection.prepareStatement("UPDATE `job` SET `Status`= ? WHERE `jobID` = ?");
            PreparedStatement pst2 = connection.prepareStatement("INSERT INTO `payment`(`paymentID`, `amount`, `date`, `type`, `customerID`, `jobID`) VALUES (?,?,?,?,?,?)");
            PreparedStatement pst3 = connection.prepareStatement("INSERT INTO cardpayment (cardtype,lastFourDigits,cardExpiryDate,paymentID,CardHolder) VALUES (?,?,?,?,?)");
            ResultSet rs1 = stmt.executeQuery("SELECT * FROM job Where jobID ="+this.jobID );
            ResultSet rs = stmt.executeQuery("Select MAX(paymentID) from payment");
            pst1.setString(1,"Paid");
            pst1.setInt(2,this.jobID);
            int new_paymentID = 0;
            while(rs.next()){new_paymentID = rs.getInt(1)+1;}
            float new_amount = this.thisPrice;
            Timestamp new_date = new Timestamp(System.currentTimeMillis());
            String new_type = "Card";
            int new_customerID =this.customerID ;
            int new_jobID = this.jobID;
            pst2.setInt(1,new_paymentID);
            pst2.setFloat(2,new_amount);
            pst2.setTimestamp(3,new_date);
            pst2.setString(4,new_type);
            pst2.setInt(5,new_customerID);
            pst2.setInt(6,new_jobID);
            pst3.setString(1,cardTypefield.getText());
            pst3.setInt(2,Integer.parseInt(lastfourfield.getText()));
            String part = ExpiryDateField.getText();
            Timestamp expdate = Timestamp.valueOf(part+" 00:00:00.000");
            pst3.setTimestamp(3,expdate);
            pst3.setInt(4,new_paymentID);
            pst3.setString(5,cardHolderField.getText());
            pst1.executeUpdate();
            pst2.executeUpdate();
            pst3.executeUpdate();

        }catch(SQLException e) {
            System.out.println(e);
        }
    }

    public void main(){
        int width = 550;
        int height = 400;
        frame.setSize(width,height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new CardPayment(getLoggedin(),jobID,thisPrice,customerID).CardPaymentPanel);
        frame.setVisible(true);
    }
}
