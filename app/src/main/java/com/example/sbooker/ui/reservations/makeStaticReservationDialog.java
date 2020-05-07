package com.example.sbooker.ui.reservations;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sbooker.BackGround;
import com.example.sbooker.MainViewModel;
import com.example.sbooker.R;
import com.example.sbooker.ui.data.model.reservation;
import com.example.sbooker.ui.data.model.staticReservation;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

/**
    This class opens dialog and makes the static reservation to database, returns the succes value to ReservationViewModel
*/
public class makeStaticReservationDialog extends AppCompatDialogFragment {
    private ReservationViewModel RVM;
    private MainViewModel MVM;
    private Spinner hallSpinner;
    private EditText day,startTime,place,duration,sport,info;
    public makeStaticReservationDialog(ReservationViewModel RVM, MainViewModel MVM){
        this.RVM=RVM;
        this.MVM=MVM;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_static_reservation,null);

        builder.setView(view).setTitle("Static Reservation").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Make Reservation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hall = hallSpinner.getSelectedItem().toString();
                String p = place.getText().toString();
                String d = day.getText().toString();
                String dur = duration.getText().toString();
                String t1 = startTime.getText().toString();
                String s = sport.getText().toString();
                String i = info.getText().toString();
                if(hall.isEmpty() || p.isEmpty()|| day.getText().toString().isEmpty() || duration.getText().toString().isEmpty()||
                        startTime.getText().toString().isEmpty()|| s.isEmpty()||i.isEmpty() ){//if any thing is empty.
                    Toast toast = Toast.makeText(getContext(), "Fill all information", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    makeStaticReservation(MVM.getUser().getUsername(), hall, p,Integer.parseInt(d), Integer.parseInt(dur),Double.parseDouble( t1), Double.parseDouble(t1) + 1, s,i);
                }
            }
        });
        this.day = view.findViewById(R.id.dayOfWeek);

        this.startTime = view.findViewById(R.id.startTime);

        this.place = view.findViewById(R.id.choosePlace);

        this.duration = view.findViewById(R.id.durationMonths);

        this.sport = view.findViewById(R.id.SportInfo);
        this.info = view.findViewById(R.id.StatReservationInfo);
        this.hallSpinner = view.findViewById(R.id.chooseHallSpinner);
        updateHallSpinner(MVM.getUser().getResRights(),view);

        //some validation
        startTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    try {
                        int i = Integer.parseInt(s.toString());
                        if (i > 23 || i < 8) {
                            startTime.setError("Invalid start time");
                        }
                    }catch (NumberFormatException e){
                        startTime.setError("Invalid start time");
                    }
                }
            }
        });

        day.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()) {
                    try {
                        int i = Integer.parseInt(s.toString());
                        if (i > 7 || i < 1) {
                            day.setError("Invalid day of the week");
                        }
                    }catch (NumberFormatException e){
                        day.setError("Invalid day of the week");
                    }
                }
            }
        });



        return builder.create();
    }


    public void makeStaticReservation(String n, String h, String p, int d, int m, double t1, double t2, String sport, String info ){
        staticReservation sR = new staticReservation(n,h,p,d,m,t1,t2,sport,info);
        ArrayList<reservation> wantedReservations = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        System.out.println(c.get(Calendar.DAY_OF_MONTH));

        if(d== 7){d=1;}else{d=d+1;}
        c.set(Calendar.DAY_OF_WEEK,d);//sets the day of week for wanted date
        int day_in_year = c.get(Calendar.DAY_OF_YEAR); //this date we will loop
        //System.out.println(c.get(Calendar.DAY_OF_YEAR));

        for(int i=0 ; i<(m*4) ; i++){
            c.set(Calendar.DAY_OF_YEAR,(day_in_year+i*7));
            //System.out.println(c.get(Calendar.DAY_OF_YEAR));
            wantedReservations.add(new reservation(n,h,p,c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH),t1,t1+1,sport,info));
        }

        System.out.println("making this many reservations: "+wantedReservations.size());
        BackGround.makeStaticReservation(sR,wantedReservations,RVM);

    }

    private void updateHallSpinner(ArrayList<String> hallKeyArray, View v) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, hallKeyArray);
        //System.out.println(getContext().toString()+ " This si the context");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hallSpinner.setAdapter(adapter);
        hallSpinner.setSelection(0);
    }

}
