package com.TDAF;
//This class was created by Joe, used as a means of tracking the currently logged in user through the windows that they open.
//This means that if the user is a technician, or an office manager. the correct details can be pulled from the account to manipulate the page or
//the sql statement that they will be running.
public class user {
    private String name;
    private String password;
    private String role;
    private int id;
    private boolean alert_check;

    public user(String nme,String pass,String rle,int ide ){
        this.name = nme;
        this.password = pass;
        this.role = rle;
        this.id=ide;
        this.alert_check=false;
    }
    public int getID(){return this.id;}
    public String getname(){return this.name;}
    public String getpassword(){return this.password;}
    public String getrole(){return this.role;}
    public void set_alertCheck(){this.alert_check = true;}
    public boolean get_alertCheck(){return this.alert_check;}

}
