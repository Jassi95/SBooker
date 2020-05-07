package com.example.sbooker.ui.data.model;

import java.util.ArrayList;

/**
 * Stores hall and it's places
 */
public class hall {
    private String hallName;
    private ArrayList<place> SportPlaces = new ArrayList<>();

    public hall(){

    }
    public hall( ArrayList<place> sportPlaces,String hallName) {
        this.hallName = hallName;
        SportPlaces = sportPlaces;
    }

    public hall(String hallName) {
        this.hallName = hallName;
    }



    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public ArrayList<place> getSportPlaces() {
        return SportPlaces;
    }

    public void setSportPlaces(ArrayList<place> sportPlaces) {
        SportPlaces = sportPlaces;
    }

    public void addSportPlace(place p){
        SportPlaces.add(p);
    }


}
