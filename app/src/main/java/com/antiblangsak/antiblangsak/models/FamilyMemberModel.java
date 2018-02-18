package com.antiblangsak.antiblangsak.models;

import org.json.JSONObject;

/**
 * Created by Syukri on 2/16/18.
 */

public class FamilyMemberModel {

    private int id;
    private String name;
    private String relation;
    private JSONObject data;

    public FamilyMemberModel(int id, String name, String relatioo, JSONObject data) {
        this.id = id;
        this.name = name;
        this.relation = relatioo;
        this.data = data;
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

    public JSONObject getData() {
        return data;
    }
}
