package com.example.tulips;

import java.util.ArrayList;
import java.util.List;

public class Bulb {
    private String name;
    private int imageResourceID;

    //constructor
    private Bulb(String newname, int newID){
        this.name = newname;
        this.imageResourceID = newID;
    }
    public static List<Bulb> tulips = new ArrayList<Bulb>(){{
        add(new Bulb("Daydream", R.drawable.daydream));
        add(new Bulb("Apeldoorn Elite", R.drawable.apeldoorn));
        add(new Bulb("Banja Luka", R.drawable.banjaluka));
        add(new Bulb("Burning Heart", R.drawable.burningheart));
        add(new Bulb("Cape Cod", R.drawable.capecod));
    }};
    public String getName(){
        return name;
    }
    public int getImageResourceID(){
        return imageResourceID;
    }
    //the string representation of a tulip is its name
    public String toString(){
        return this.name;
    }

}
