package com.antiblangsak.antiblangsak;

/**
 * Created by Syukri on 1/28/18.
 */

public class NasabahModel {
    private int id;
    private String name;
    private String relation;
    private int status;

    public NasabahModel(int id, String name, String relation, int status) {
        this.id = id;
        this.name = name;
        this.relation = relation;
        this.status = status;
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
}
