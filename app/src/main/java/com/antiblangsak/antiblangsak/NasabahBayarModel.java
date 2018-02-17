package com.antiblangsak.antiblangsak;

/**
 * Created by citra on 2/11/2018.
 */

public class NasabahBayarModel {
    boolean checked;
    private String name;
    private int id;

    public NasabahBayarModel(int id, String name){
        this.id = id;
        this.name = name;
        this.checked = false;
    }

    public String getName(){
        return this.name;
    }
    public boolean isChecked(){
        return this.checked;
    }
    public int getId(){return this.id;}

    public void checked() {
        this.checked = !this.checked;
    }
}
