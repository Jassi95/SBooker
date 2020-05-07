package com.example.sbooker.ui.data.model;

import java.util.ArrayList;
/**
 * Stores place and reservations
 */
public class place {
    private String placeName;
    private ArrayList<reservation> reservations = new ArrayList<>();

    public place(){}
    public place(String name){
        this.placeName=name;
    }
    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public ArrayList<reservation> getReservations() {
        return reservations;
    }

    public void setReservations(ArrayList<reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservations(reservation r){
        reservations.add(r);
    }

}
