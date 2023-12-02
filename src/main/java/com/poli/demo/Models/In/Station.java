package com.poli.demo.Models.In;

public class Station {

    private String name;

    private boolean stand;

    public Station(String name, boolean stand)
    {
        this.name = name;
        this.stand = stand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getStand() {
        return stand;
    }

    public void setStand(boolean Stand) {
        this.stand = stand;
    }
}
