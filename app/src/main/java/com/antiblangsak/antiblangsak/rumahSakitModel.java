package com.antiblangsak.antiblangsak;

/**
 * Created by martinovela on 1/28/18.
 */

public class rumahSakitModel {


    String name;
    String address;
    String distance;

    public rumahSakitModel(String name, String address, String distance) {
        this.name=name;
        this.address=address;
        this.distance=distance;

    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistance() {
        return distance;
    }



}
