package com.example.sbooker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.sbooker.ui.data.model.hall;
import com.example.sbooker.ui.data.model.user;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> mText = new MutableLiveData<>();
    private MutableLiveData<user> user = new MutableLiveData(null);
    private MutableLiveData<Integer> i = new MutableLiveData<>(0);
    private MutableLiveData<Boolean> loggedIn = new MutableLiveData<>(false);

    private MutableLiveData<ArrayList<String>> hallKeyArray = new MutableLiveData<>();

    public void setUser (user u){
        user.setValue(u);
        System.out.println("CHanged the MVM user!");
        mText = new MutableLiveData<>();
        mText.setValue(u.getUsername());
        loggedIn.setValue(true);
    }
    public void logOut(){
        user = new MutableLiveData<>(null);
        loggedIn.setValue(false);
    }

    public user getUser() {
       return user.getValue();
    }

    public void setUserName(String n){
        mText.setValue(n);
        System.out.println("TYDYY");
    }

    public LiveData<String> getText() {
        i.setValue(i.getValue()+1);
        System.out.println("This is loaded "+i+" times");
        return mText;
    }
    public boolean getLoggedIn(){
        return loggedIn.getValue();
    }

    public MutableLiveData<Boolean> LoggedListener(){return loggedIn;}


    public void setHallKeyArray(ArrayList<String> keys){
        System.out.println("updating hallKeys to "+ keys);
        hallKeyArray.setValue(keys);
    }

    public ArrayList<String> getHallKeyArray (){
        return hallKeyArray.getValue();
    }
    public MutableLiveData<ArrayList<String>> getHallKeyChange(){
        return hallKeyArray;
    }
}

