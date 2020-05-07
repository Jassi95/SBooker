package com.example.sbooker.ui.reservations;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.sbooker.ui.data.model.reservation;

import java.util.ArrayList;
import java.util.Calendar;

import java.util.Collections;
import java.util.Comparator;
/**
 *  Stores data needed in reservation tab
 */
public class ReservationViewModel extends ViewModel {
    private MutableLiveData<reservation> StaticReservations = new MutableLiveData<>();
    private MutableLiveData<String> StaticResSuccess = new MutableLiveData<>();
    private MutableLiveData<String> mText=new MutableLiveData<>();


    public ReservationViewModel() {
        mText.setValue("Your reservations: ");
    }

    public MutableLiveData<String> getText() {
        return mText;
    }

    public void setReservations(ArrayList<reservation> resList){
        String s = "Your reservations from database: ";
        Collections.sort(resList,new ReservationComparator()); //sorts the reservations by date
        for(reservation res:resList){
           s =  s.concat("\n\n"+getReservationString(res));
        }
        mText.setValue(s);
    }

    public MutableLiveData<String> getStaticResSucces(){
        return StaticResSuccess;
    }
    public void setStaticResSuccess(String s){StaticResSuccess.setValue(s);}

    private String getReservationString(reservation r){
        String s = ("Reservation: "+r.getHall()+" "+r.getPlaceName()+" "+r.getDay()+"."+r.getMonth()+"."+r.getYear()+" from: "+r.getStartTime()+" to "+r.getEndTime());
        return s;
    }



    class ReservationComparator implements Comparator<reservation>{ //COMPARES times of reservations

        @Override
        public int compare(reservation o1, reservation o2) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            c1.set(Calendar.YEAR,o1.getYear());c2.set(Calendar.YEAR,o2.getYear());
            c1.set(Calendar.MONTH,o1.getMonth());c2.set(Calendar.MONTH,o2.getMonth());
            c1.set(Calendar.DAY_OF_MONTH,o1.getDay());c2.set(Calendar.DAY_OF_MONTH,o2.getDay());

            return ((c1.compareTo(c2))*-1); //turns the comparison from newest to oldest

        }
    }

}