package com.TDAF;

public class ComboObject {
    private int ID;
    private String Description;


    public ComboObject(String desc, int id){
        this.ID = id;
        this.Description = desc;
    }
    public String getDescription(){return this.Description;}
    public int getID(){return this.ID;}
    public void addint(int add){this.ID = this.ID + add;}
    public void setzero(){this.ID=0;}

    @Override
    public String toString() {
        return this.Description;
    }
}
