package com.example.sbooker.ui.data.model;

/**
 *reservation
 */
public class reservation {// todo, sport and information
    private String makerName;
    private String hall;
    private String placeName;
    private int year;
    private int month;
    private int day;
    private double startTime;
    private double endTime;
    private String sport;
    private String info;

    public reservation(String n,String h, String p,int y,int m,int d,double t1,double t2,String sport,String info){
        this.makerName= n;
        this.hall = h;
        this.placeName=p;
        this.year=y;
        this.month=m;
        this.day=d;
        this.startTime=t1;
        this.endTime=t2;
        this.sport = sport;
        this.info = info;
    }
    public reservation(){} //needed for firebase


    public String getMakerName() {
        return makerName;
    }

    public String getHall() {
        return hall;
    }

    public String getPlaceName() {
        return placeName;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
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

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
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
