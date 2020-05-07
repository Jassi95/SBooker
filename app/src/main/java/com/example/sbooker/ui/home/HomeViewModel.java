package com.example.sbooker.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sbooker.BackGround;
import com.example.sbooker.ui.data.model.dateObject;
import com.example.sbooker.ui.data.model.hall;


/**
 *  Responsible for storing data shown in home screen
 */
public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private MutableLiveData<hall> halls = new MutableLiveData<>();
    private MutableLiveData<dateObject> date =new MutableLiveData<>();


    public HomeViewModel() {
        BackGround bk = new BackGround();
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
        //hall h = bk.getHall();
       //bk.getHallFromDB();
    }



    public LiveData<String> getText() {
        return mText;
    }




    public void setHall(hall h){
        //System.out.println(h.getHallName());
       // System.out.println(h.toString());
        //System.out.println(h.getSportPlaces().get(0).getPlaceName());
        if(h!=null ) {
            halls.setValue(h);
        }
       // System.out.println("we got new hall from db");
    }

    public MutableLiveData<hall> getHalls(){
        return halls;
    }

    public hall getHall(){
        return halls.getValue();
    }

    public MutableLiveData<dateObject> getDate() {
        return date;
    }

    public dateObject getDateObj(){
        return date.getValue();
    }

    public void setDate(dateObject d) {
       date.setValue(d);
    }

}