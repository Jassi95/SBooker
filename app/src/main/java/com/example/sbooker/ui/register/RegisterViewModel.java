package com.example.sbooker.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 *  Hold async data of successfully making new account
 */
public class RegisterViewModel extends ViewModel {
    private MutableLiveData<String> text = new MutableLiveData<>();


    public void setText(String registaration_succes) {
        text.setValue(registaration_succes);
    }

    public MutableLiveData<String> getText(){
        return text;
    }
}
