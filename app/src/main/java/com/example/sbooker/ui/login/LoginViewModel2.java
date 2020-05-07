package com.example.sbooker.ui.login;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sbooker.ui.data.model.user;
/**
 *  Keeps users information during login. Should be hashed
 */
public class LoginViewModel2 extends ViewModel {
    private MutableLiveData<String> text = new MutableLiveData<>();
    private MutableLiveData<user> USER = new MutableLiveData<>();

    public void setText(String login_success) {
        text.setValue(login_success);
    }

    public MutableLiveData<String> getText(){
        return text;
    }
    public void setUSER(user u){
        this.USER.setValue(u);
    }
    public user getUSER(){
        return this.USER.getValue();
    }


}
