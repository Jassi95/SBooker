package com.example.sbooker.ui.data.model;
/**
 * Used to create weekly reservations
 */
public class staticReservation {
    private String makerName;
    private String hall;
    private String placeName;
    private int day_of_the_week;
    private int howManyMonths;
    private double startTime;
    private double endTime;
    private String sport;
    private String info;

    public staticReservation(String n, String h, String p, int d, int m, double t1, double t2, String sport, String info){
        this.makerName= n;
        this.hall = h;
        this.placeName=p;
        this.day_of_the_week = d;
        this.howManyMonths = m;
        this.startTime=t1;
        this.endTime=t2;
        this.sport = sport;
        this.info = info;
    }
    public staticReservation(){} //needed for firebase


    public String getMakerName() {
        return makerName;
    }

    public String getHall() {
        return hall;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getStartTime() {
        return startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public String getInfo(){return info;}

    public String getSport(){return sport;}

    public void setMakerName(String makerName) {
        this.makerName = makerName;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    public void setSport(String sport){this.sport=sport;}

    public void setInfo(String info){this.info=info;}
}
