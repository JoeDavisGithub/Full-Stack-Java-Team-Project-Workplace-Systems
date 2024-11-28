package com.TDAF;

//import jdk.javadoc.internal.tool.Start;
//import sun.security.util.Debug;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobCreation {
    private static JFrame frame = new JFrame("Task Creation");
    private JTextField SearchBar;
    private JComboBox TaskDropdown;
    private JButton addTaskButton;
    private JTextArea priceTextArea;
    private JPanel TaskCreationPanel;
    private JTextArea taskAmountTextArea;
    private JButton createJobButton;
    private JButton backButton;
    private JTextArea durationTextArea;
    private JComboBox CustomerComboBox;
    private JComboBox UrgencyLevelDropDown;
    private JTextField jobDescField;
    private JTextArea discountDisplay;
    private float price = 0;
    private float duration = 0;
    private float discount = 0;
    private float urgencyrate = 1;
    private float fixedrate;
    private float flexdiscount;
    public user Logged_in;
    private int jobID;
    private ArrayList<String> tasks;
    private Map discounts;
    private ArrayList<FlexDiscountObject> flexdiscs;
    private String discType;


    public user getLoggedin() {
        return this.Logged_in;
    }

    // window that allows jobs to be created, Reworked By Joe from tayo's JDBC version, to allow for dynamic task allocation and integrated task creation into this window's operations.
    //also reformatted GUI to make it more presentable.
    public JobCreation(user loggedin) {
        this.Logged_in = loggedin;
        tasks = new ArrayList<String>();
        discounts = new HashMap();
        flexdiscs = new ArrayList<FlexDiscountObject>();
        getConnector connector = new getConnector();
        Start();

        CustomerComboBox.addItem(""); // Adding a blank customer at the start



        try (Connection connection = connector.getConnection()) {
            Statement stmt = connection.createStatement();
            Statement stmt1 = connection.createStatement();
            //Statement stmt2 = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT description FROM taskdetails");
            ResultSet rs1 = stmt1.executeQuery("SELECT customerID,customerName FROM customer");

            while (rs.next()) {
                TaskDropdown.addItem(rs.getString("description"));
            }
            while (rs1.next()) {
                CustomerComboBox.addItem("ID: " + rs1.getString("customerID") + " Name: " + rs1.getString("customerName"));
            }
        } catch (SQLException es) {
            es.printStackTrace();
        }

        addTaskButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getConnector connector = new getConnector();
                try (Connection connection = connector.getConnection()) {
                    PreparedStatement stmt = connection.prepareStatement("SELECT price,duration,description,task_detailID FROM taskdetails WHERE description = ?");
                    String dropdown = TaskDropdown.getSelectedItem().toString();
                    stmt.setString(1, dropdown);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        tasks.add(rs.getString("description"));
                        price += rs.getFloat("price") * (1 - (calcdisc(rs.getInt(4)) / 100));
                        discount += rs.getFloat(1) - (rs.getFloat(1) * (1 - (calcdisc(rs.getInt(4)) / 100)));
                        duration += rs.getInt("duration");
                        taskAmountTextArea.append("Task: " + rs.getString(3) + " / Price: £" + Float.toString(rs.getFloat(1) * (1 - (calcdisc(rs.getInt(4)) / 100))) +
                                " Duration: " + Integer.toString(rs.getInt(2)) + " minutes Discount: £" +
                                Float.toString(rs.getFloat(1) - (rs.getFloat(1) * (1 - (calcdisc(rs.getInt(4)) / 100)))) + "\n");
                        priceTextArea.setText("Job Price: £" + Float.toString(price) + "\n" + "Urgency Surcharge: £" + Float.toString((price * urgencyrate) - price) + "\n" + "Total Cost: £" + Float.toString(price * urgencyrate) + "\n");
                        if (discType == "Flexible") {
                            priceTextArea.append("From Flexible Discount Bands: £" + Float.toString(applyFix()) + " will be deducted from this job's costs." + "\n");
                            priceTextArea.append("Total with Applied Discount: £" + Float.toString((price * urgencyrate) - applyFix()) + "\n");
                            priceTextArea.append("Total (Inc VAT): £" + Float.toString(((price * urgencyrate) - applyFix()) + ((price * urgencyrate) - applyFix()) * (0.2f)));

                        } else if (discType == "Fixed") {
                            priceTextArea.append("From Fixed Discount: £" + Float.toString((price * urgencyrate) - ((price * urgencyrate) * (1 - (fixedrate / 100)))) + " will be deducted from this job's costs." + "\n");
                            priceTextArea.append("Total with Applied Discount: £" + Float.toString((price * urgencyrate) - ((price * urgencyrate) - ((price * urgencyrate) * (1 - (fixedrate / 100))))) + "\n");
                            priceTextArea.append("Total (Inc VAT): £" + Float.toString(((price * urgencyrate) - ((price * urgencyrate) - ((price * urgencyrate) * (1 - (fixedrate / 100))))) + (((price * urgencyrate) - ((price * urgencyrate) - ((price * urgencyrate) * (1 - (fixedrate / 100))))) * (0.2f))));
                        } else {
                            priceTextArea.append("Total Discount: £" + Float.toString(discount) + "\n");
                            priceTextArea.append("Total (Inc VAT): £" + Float.toString(((price * urgencyrate) - discount) + ((price * urgencyrate) - discount) * (0.2f)));
                        }


                    }

                } catch (SQLException es) {
                    es.printStackTrace();
                }

            }
        });
        createJobButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (price == 0 || duration == 0) {
                    JOptionPane.showMessageDialog(frame, "NO TASKS SELECTED");
                } else if (CustomerComboBox.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(frame, "NO CUSTOMER SELECTED");
                } else {
                    saveJob();
                    JOptionPane.showMessageDialog(frame, "JOB CREATED");
                    taskAmountTextArea.setText("");
                    priceTextArea.setText("");
                }
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

        UrgencyLevelDropDown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                seturgencyrate();
            }
        });

        CustomerComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                tasks.clear();
                discounts.clear();
                flexdiscs.clear();
                price = 0;
                duration = 0;
                discount = 0;
                taskAmountTextArea.setText("");
                priceTextArea.setText("Job Price: £0.0\n" + "Total Discount: £0.0\n" + "Urgency Surcharge: £0.0\n" + "Total Cost: £0.0");
                discountDisplay.setText("");
                getConnector connector = new getConnector();
                try (Connection connection = connector.getConnection()) {
                    PreparedStatement checkfixed = connection.prepareStatement("SELECT * FROM fixed_discounts WHERE customerID=?");
                    PreparedStatement checkflex = connection.prepareStatement("SELECT * FROM flexible_discounts WHERE customerID=?");
                    PreparedStatement checkvar = connection.prepareStatement("SELECT * FROM variable_discounts WHERE customerID=?");
                    String[] customerIDsplit = CustomerComboBox.getSelectedItem().toString().split(" ");
                    int customerID = Integer.parseInt(customerIDsplit[1]);
                    checkfixed.setInt(1, customerID);
                    checkflex.setInt(1, customerID);
                    checkvar.setInt(1, customerID);
                    ResultSet fixed = checkfixed.executeQuery();
                    while (fixed.next()) {
                        discountDisplay.append("Fixed Discount : " + fixed.getString("fixed_rate") + "%" + "\n");
                        discType = "Fixed";
                        fixedrate = fixed.getFloat("fixed_rate");
                    }
                    ResultSet var = checkvar.executeQuery();
                    while (var.next()) {
                        discountDisplay.append("Variable Discount for Task ID: " + var.getString("task_detailID") + " Discount: " + var.getString("task_discount") + "%" + "\n");
                        discounts.put(var.getInt("task_detailID"), var.getFloat("task_discount"));
                        discType = "Variable";
                    }
                    ResultSet flex = checkflex.executeQuery();
                    while (flex.next()) {
                        discountDisplay.append("Flexible Discount{ Lower Bound: £" + flex.getString("lower_bound") + " Upper Bound:  £" + flex.getString("upper_bound") + " Discount: " + flex.getString("discountrate") + "% }" + "\n");
                        flexdiscs.add(new FlexDiscountObject(flex.getFloat("lower_bound"), flex.getFloat("upper_bound"), flex.getFloat("discountrate")));
                        System.out.println("Lower: " + flex.getString("lower_bound") + " Upper: " + flex.getString("upper_bound") + " Rate: " + flex.getString("discountrate"));
                        discType = "Flexible";
                    }

                } catch (SQLException es) {
                    es.printStackTrace();
                }
            }
        });
    }

    public float applyFix() {
        float fixedDiscTotal = 0;
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            PreparedStatement JobChecker = connection.prepareStatement("Select * FROM job WHERE customerID =?");
            //int calendarMonth = (((44640 * 60)) * 1000);
            Timestamp reverse = new Timestamp(System.currentTimeMillis() - ((int) 2.678E+9));
            //reverse.setTime(reverse.getTime() - calendarMonth);
            //JobChecker.setTimestamp(1,reverse);
            String[] customerIDsplit = CustomerComboBox.getSelectedItem().toString().split(" ");
            int customerID = Integer.parseInt(customerIDsplit[1]);
            JobChecker.setInt(1, customerID);
            ResultSet Jobs = JobChecker.executeQuery();
            while (Jobs.next()) {
                System.out.println(Jobs.getTimestamp("startDate").getTime() > reverse.getTime());
                System.out.println(Jobs.getTimestamp("startDate").getTime());
                System.out.println(reverse.getTime());
                if (Jobs.getTimestamp("startDate").getTime() > reverse.getTime()) {
                    for (FlexDiscountObject f : flexdiscs) {
                        System.out.println("found");
                        if ((Jobs.getFloat("Price") > f.getLower()) && (Jobs.getFloat("Price") < f.getUpper())) {
                            fixedDiscTotal += Jobs.getFloat("Price") * (f.getRate() / 100);
                            System.out.println(Jobs.getFloat("Price") * (f.getRate() / 100));
                            break;
                        }
                    }
                }
            }

        } catch (SQLException es) {
            es.printStackTrace();
        }
        return fixedDiscTotal;
    }

    public float calcdisc(int ID) {
        if (discType == "Variable") {
            if (discounts.containsKey(ID)) {
                return (float) discounts.get(ID);
            }
        }
        return 0;
    }

    public void seturgencyrate() {
        String ur = UrgencyLevelDropDown.getSelectedItem().toString();
        priceTextArea.setText("Job Price: £" + Float.toString(price) + "\n" + "Total Discount: £" + Float.toString(discount) + "\n" + "Urgency Surcharge: £" + Float.toString((price * urgencyrate) - price) + "\n" + "Total Cost: £" + Float.toString(price * urgencyrate));

        if (ur == "High Urgency") {
            this.urgencyrate = 2;
        } else if (ur == "Urgent") {
            this.urgencyrate = 1.50F;
        } else {
            this.urgencyrate = 1;
        }
    }

    public void main() {
        int width = 700;
        int height = 600;

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JobCreation(getLoggedin()).TaskCreationPanel);
        frame.setVisible(true);
    }

    public Timestamp getDeadline() {
        int deadline = ((1440 * 60)) * 1000;
        ;
        String ur = UrgencyLevelDropDown.getSelectedItem().toString();
        if (ur == "High Urgency") {
            deadline = ((180 * 60)) * 1000;
        } else if (ur == "Urgent") {
            deadline = ((360 * 60)) * 1000;
        }
        Timestamp time = new Timestamp(System.currentTimeMillis());
        time.setTime(time.getTime() + deadline);
        return time;
    }

    public void saveJob() {
        getConnector connector = new getConnector();
        try (Connection connection = connector.getConnection()) {
            Statement stmt = connection.createStatement();
            PreparedStatement pst = connection.prepareStatement("INSERT INTO job(jobID,UrgencyLvl,Price,Status,Deadline,jobDesc,taskamount,amountcompleted,customerID,startDate) VALUES (?,?,?,?,?,?,?,?,?,?)");
            ResultSet rsma = stmt.executeQuery("SELECT MAX(jobID) FROM job");
            PreparedStatement pstT = connection.prepareStatement("INSERT INTO task(taskID,department,status,task_detailID,jobID) VALUES(?,?,?,?,?)");
            PreparedStatement pstTID = connection.prepareStatement("SElECT MAX(taskID) FROM task");
            Statement pstTDID = connection.createStatement();
            int new_ID = 0;
            while (rsma.next()) {
                new_ID = rsma.getInt(1) + 1;
            }
            this.jobID = new_ID;
            String[] customerIDsplit = CustomerComboBox.getSelectedItem().toString().split(" ");
            int customerID = Integer.parseInt(customerIDsplit[1]);
            pst.setInt(1, new_ID);
            pst.setString(2, (String) UrgencyLevelDropDown.getSelectedItem().toString());
            if (discType == "Flexible") {
                pst.setFloat(3, (price * urgencyrate) - applyFix());
            } else if (discType == "Fixed") {
                pst.setFloat(3, ((price * urgencyrate) * (1 - (fixedrate / 100))));
            } else {
                pst.setFloat(3, price * urgencyrate);
            }
            pst.setString(4, "Unpaid");
            pst.setTimestamp(5, getDeadline());
            pst.setString(6, jobDescField.getText());
            pst.setInt(7, tasks.size());
            pst.setInt(8, 0);
            pst.setInt(9, customerID);
            pst.setTimestamp(10, new Timestamp(System.currentTimeMillis()));
            pst.executeUpdate();
            for (String s : tasks) {
                int taskID = 0;
                ResultSet rsID = pstTID.executeQuery();
                while (rsID.next()) {
                    taskID = rsID.getInt(1) + 1;
                }
                //pstTDID.setString(1,s);
                ResultSet rsTDD = pstTDID.executeQuery("SELECT task_detailID,location,description FROM taskdetails WHERE description = '" + s + "'");
                while (rsTDD.next()) {
                    pstT.setString(2, rsTDD.getString(2));
                    pstT.setInt(4, rsTDD.getInt(1));
                }
                pstT.setString(3, "Not Started");
                pstT.setInt(1, taskID);
                pstT.setInt(5, jobID);
                pstT.executeUpdate();
            }

            JOptionPane.showMessageDialog(frame, "New Job ID: " + Integer.toString(new_ID));

            new Receptionist(Logged_in).main();
            frame.dispose();


        } catch (SQLException es) {
            System.out.println(es);
        }
    }

    public void Start(){
        //Make sure display texts are not editable (These all have their own classes to be edited in)
        priceTextArea.setEditable(false);
        taskAmountTextArea.setEditable(false);
        discountDisplay.setEditable(false);
        //Start of with a text
        priceTextArea.setText("Job Price: £0.0\n" +"Total Discount: £0.0\n" +"Urgency Surcharge: £0.0\n" +"Total Cost: £0.0");
    }

}
