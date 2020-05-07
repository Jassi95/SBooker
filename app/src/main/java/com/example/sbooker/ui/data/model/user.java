package com.example.sbooker.ui.data.model;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

/**
 * Data class that captures user information when logging in/ creating new user
 */
public class user implements Parcelable {


    private String email;
    private String username;
    private String psw;

    private ArrayList<String> resRights = new ArrayList<>();

    public user() {

    }

    public user(String email, String psw, String username) {
        this.email = email;
        this.username = username;
        this.psw = psw;
    }

    protected user(Parcel in) {
        email = in.readString();
        username = in.readString();
        psw = in.readString();
        resRights=in.readArrayList(null);
    }

    public static final Creator<user> CREATOR = new Creator<user>() {
        @Override
        public user createFromParcel(Parcel in) {
            return new user(in);
        }

        @Override
        public user[] newArray(int size) {
            return new user[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPsw() {
        return psw;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public void setusername(String username) {
        this.username = username;
    }

    public void setpsw(String psw) {
        this.psw = psw;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(psw);
        dest.writeList(resRights);
    }



    public void setResRights(ArrayList<String> rights) {
        resRights = rights;
        System.out.println(rights);
    }
    public ArrayList<String> getResRights(){
        return resRights;
    }
}