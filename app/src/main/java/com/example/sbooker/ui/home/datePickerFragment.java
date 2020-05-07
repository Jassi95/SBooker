package com.example.sbooker.ui.home;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import com.example.sbooker.ui.data.model.dateObject;

import java.util.Calendar;
/**
 * Makes dialog where you can select the date you wanna view
 */
public class datePickerFragment extends DialogFragment



     implements DatePickerDialog.OnDateSetListener{
        TextView textView;
        HomeViewModel hvm;

    public datePickerFragment(TextView text, HomeViewModel hmv){
            this.textView = text;
            this.hvm = hmv;
        }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) { // creates dialog with current date set as default. No access to past dates.
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        Dialog DPD = new DatePickerDialog(getActivity(),this,year,month,day);
        //c.add(Calendar.MONTH,1);
        ((DatePickerDialog) DPD).getDatePicker().setMinDate(System.currentTimeMillis());//TODO should be made external time
       // ((DatePickerDialog) DPD).getDatePicker().setMaxDate(c.getTimeInMillis());
        return DPD;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        /*Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);*/
        textView.setText("Chosen day: "+dayOfMonth+"."+month+1+"."+year);
        dateObject date = new dateObject(dayOfMonth,month+1,year);
        this.hvm.setDate(date);
    }
}
