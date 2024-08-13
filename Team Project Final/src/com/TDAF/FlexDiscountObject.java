package com.TDAF;

public class FlexDiscountObject {
    private float lower;
    private float upper;
    private float rate;

    public FlexDiscountObject(float low,float high,float rte){
        this.lower = low;
        this.upper = high;
        this.rate = rte;
    }
    public float getLower(){return lower;}
    public float getUpper(){return upper;}
    public float getRate(){return rate;}
}
