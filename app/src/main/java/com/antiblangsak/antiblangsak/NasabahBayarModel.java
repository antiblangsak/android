package com.antiblangsak.antiblangsak;

/**
 * Created by citra on 2/11/2018.
 */

public class NasabahBayarModel {
    boolean checked;
    private String name;

    public NasabahBayarModel(String name){
        this.name = name;
        this.checked = false;
    }

    public String getName(){
        return this.name;
    }
    public boolean isChecked(){
        return this.checked;
    }

    public void checked() {
        this.checked = !this.checked;
    }
}
