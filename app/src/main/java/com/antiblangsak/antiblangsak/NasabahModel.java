package com.antiblangsak.antiblangsak;

/**
 * Created by Syukri on 1/28/18.
 */

public class NasabahModel {
    private int id;
    private String name;
    private String relation;
    private int status;
    private boolean selected;

    public NasabahModel(int id, String name, String relation, int status) {
        this.id = id;
        this.name = name;
        this.relation = relation;
        this.status = status;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRelation() {
        return relation;
    }

    public String getStatus() {
        if (status == 0) {
            return "Requested";
        } else if (status == 1) {
            return "Active";
        } else {
            return "Rejected";
        }
    }

    public void pressed() {
        this.selected = !this.selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
