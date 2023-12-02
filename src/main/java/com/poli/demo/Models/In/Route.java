package com.poli.demo.Models.In;
public class Route {

    private int duration;

    private Station[] stations;

    public Route(int duration, Station[] stations)
    {
        this.duration = duration;
        this.stations = stations;
    }

    public int getDuration() {
        return duration;
    }

    public void setDurations(int Duration) {
        this.duration = duration;
    }

    public Station[] getStations() {
        return stations;
    }

    public void setPassword(Station[] stations) {
        this.stations = stations;
    }
}
