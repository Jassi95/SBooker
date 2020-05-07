package com.example.sbooker.ui.home;


import android.graphics.Color;

import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sbooker.MainActivity;
import com.example.sbooker.MainViewModel;
import com.example.sbooker.R;
import com.example.sbooker.ui.data.model.dateObject;
import com.example.sbooker.ui.data.model.hall;
import com.example.sbooker.ui.data.model.place;
import com.example.sbooker.ui.data.model.reservation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;


/**
 *  Responsible for showing everything at home screen
 */
public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private MainViewModel mvm;
    private TableLayout tl;
    private Spinner hallSpinner;
    private TextView dayPicker;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.mvm = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.tl = (TableLayout) root.findViewById(R.id.ReservationTable);
        this.hallSpinner = root.findViewById(R.id.hallSpinner);
        this.dayPicker = root.findViewById(R.id.dayPicker);
        //String date = getDate();
        //dayPicker.setText(date);
        homeViewModel.setDate(getDate());

        dayPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //opens date picker
                DialogFragment datePicker = new datePickerFragment(dayPicker, homeViewModel);
                datePicker.show(getChildFragmentManager(), "date picker");
            }
        });

        homeViewModel.getHalls().observe(getViewLifecycleOwner(), new Observer<hall>() {
            @Override
            public void onChanged(hall h) { //updates table when we switch hall
                System.out.println("update table");
                tl.removeAllViews();
                createTable(h);
            }
        });

        //getHallFromDB("Halli yksi");

        //createTable(root,  homeViewModel.getHall());

        homeViewModel.getDate().observe(getViewLifecycleOwner(), new Observer<dateObject>() {
            @Override
            public void onChanged(dateObject dateObject) { //Updates table when we select date
                dayPicker.setText("Reservations in: " + dateObject.getDate());
                System.out.println("Changed day to " + homeViewModel.getDateObj().getDate());
                if (homeViewModel.getHall() != null) {
                    tl.removeAllViews();
                    createTable(homeViewModel.getHall());
                    System.out.println("this should update table");
                }
            } //updates the table if dayObject is changed

        });

        mvm.getHallKeyChange().observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {// updates Spinner when when we have loaded halls
                System.out.println("Updating hall spinner!" + mvm.getHallKeyArray());
                updateHallSpinner(mvm.getHallKeyArray(), root);
            }
        });

        mvm.LoggedListener().observe(getViewLifecycleOwner(), new Observer<Boolean>() { //updates table when logged in
            @Override
            public void onChanged(Boolean aBoolean) {
                if (homeViewModel.getHall() != null) {
                    tl.removeAllViews();
                    createTable(homeViewModel.getHall());
                }
            }
        });


        return root;
    }

    private void updateHallSpinner(ArrayList<String> hallKeyArray, View v) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, hallKeyArray);
        //System.out.println(getContext().toString()+ " This si the context");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hallSpinner.setAdapter(adapter);
        hallSpinner.setSelection(1);

        this.hallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + " Selected " + hallSpinner.getItemAtPosition(position).toString());
                getHallFromDB(hallSpinner.getItemAtPosition(position).toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing Selected");
            }
        });

    }


    private void createTable(hall h) {
        /* Makes the reservation table */

        String[] timeArray = {"8.00", "9.00", "10.00", "11.00", "12.00", "13.00", "14.00", "15.00", "16.00", "17.00", "18.00", "19.00", "20.00", "21.00", "22.00", "23.00"};
        String[] placesArray = {"kenttä 1", "kenttä 2", "kenttä 3", "kenttä 4"}; //name of the fields

        /* Create a new row to be added. */
        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1f;
        dateObject selectedDate = homeViewModel.getDateObj();
        for (int i = 0; i < timeArray.length; i++) {
            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(rowParams);
            /* Create a Button to be the row-content. */

            int placeIndx = 0;
            for (place ps : h.getSportPlaces()) {
                final Button b = new Button(getContext());
                final TextView c = new TextView(getContext());

                b.setTextSize(12);
                b.setTag("0" + ";" + placeIndx + ";" + ps.getPlaceName() + ";" + timeArray[i] + ";" + selectedDate.getDay() + ";" + selectedDate.getMonth() + ";" + selectedDate.getYear() + ";" + h.getHallName());
                //c.setTag(b.getTag().toString());
                b.setLayoutParams(rowParams);
                b.setText(timeArray[i] + " " + ps.getPlaceName());
                for (reservation res : ps.getReservations()) {

                    if (res.getStartTime() == Double.parseDouble(timeArray[i]) &&
                            res.getDay() == selectedDate.getDay() &&
                            res.getMonth() == selectedDate.getMonth() &&
                            res.getYear() == selectedDate.getYear()) { //if day matches with reservations.

                        //b.setEnabled(false);
                        b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.other_res_button, null));
                        //for future tag should be set as an object for easier up keep.
                        b.setTag("1" + ";" + placeIndx + ";" + ps.getPlaceName() + ";" + timeArray[i] + ";" + selectedDate.getDay() + ";" + selectedDate.getMonth() + ";" + selectedDate.getYear() + ";" + h.getHallName() + ";" + res.getMakerName()+";"+
                                res.getInfo()+";"+res.getSport());
                        if (mvm.getLoggedIn()) {// own reservations show in green
                            if (mvm.getUser().getUsername().equals(res.getMakerName())) {
                                b.setTextColor(Color.parseColor("#FFFFFF"));
                                b.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.my_res_button, null));
                                b.setEnabled(true);
                                b.setTag("2" + ";" + placeIndx + ";" + ps.getPlaceName() + ";" + timeArray[i] + ";" + selectedDate.getDay() + ";" + selectedDate.getMonth() + ";" + selectedDate.getYear() + ";" + h.getHallName() + ";" + res.getMakerName()+ ";"+
                                        res.getInfo()+";"+res.getSport());

                            }
                        }
                    }
                }


                b.setOnClickListener(new View.OnClickListener() { // desides what kind of action button holds based on its status(0 free 1 some res 2 my res)
                    @Override
                    public void onClick(View v) { // todo tag [0] used for reservation. frag showing
                        String s = v.getTag().toString();
                        if (s.split(";")[0].equals("1")) { //someones reservation
                            openReservationDialog(v.getTag().toString());
                        }
                        else if (mvm.getLoggedIn()) {
                            if (s.split(";")[0].equals("0")) {// not reserved
                                System.out.println(v.getTag().toString().split(";")[7]);
                                System.out.println(mvm.getUser().getResRights().contains("Halli yksi"));
                                //System.out.println(mvm.getUser().getResRights().get(0));
                                if(mvm.getUser().getResRights().contains(v.getTag().toString().split(";")[7])){
                                    openMakeReservationDialog(v.getTag().toString() + ";" + mvm.getUser().getUsername());
                                }else{Toast toast = Toast.makeText(getContext(), "No right to book from this hall", Toast.LENGTH_SHORT);
                                toast.show();}
                                //((MainActivity) getActivity()).makeReservation(s.substring(s.indexOf(";")+1));
                                //v.setEnabled(false);
                                //getHallFromDB(hallSpinner.getItemAtPosition(hallSpinner.getSelectedItemPosition()).toString());

                            }

                            else if (s.split(";")[0].equals("2")) { //ur reservation
                                System.out.println("REMOVING RESERVATION");
                                //v.setEnabled(true);
                                openDeleteReservationDialog(v.getTag().toString());
                               // ((MainActivity) getActivity()).deleteReservation(s.substring(s.indexOf(";") + 1));
                               // getHallFromDB(hallSpinner.getItemAtPosition(hallSpinner.getSelectedItemPosition()).toString());
                            }
                        } else {
                            // openReservationDialog();
                            Toast toast = Toast.makeText(getContext(), "Login to book", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });

                /* Add Button to row. */
                tr.addView(b);
                placeIndx++;
            }
            /* Add row to TableLayout. */
            //tr.setBackgroundResource(R.drawable.sf_gradient_03);
            tl.addView(tr, rowParams);
        }
    }


    public void getHallFromDB(final String hallKey) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Halls");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(hallKey);
                //for(DataSnapshot ds: dataSnapshot.getChildren()) {
                //System.out.println(dataSnapshot.toString());
                hall h = ds.getValue(hall.class);
                //System.out.println(h.getHallName());

                homeViewModel.setHall(h);
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


    private dateObject getDate() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        dateObject date = new dateObject(day, month+1, year);
        return (date);
    }

    public void openReservationDialog(String info) {
        ReservationInfoFragment resDialog = new ReservationInfoFragment(info);
        resDialog.show(getChildFragmentManager(), "Reservation dialog");
    }

    public void openMakeReservationDialog(String info) {
        ((MainActivity) getActivity()).openReservationDialog(info);
    }
    public void openDeleteReservationDialog(String info) {
        ((MainActivity) getActivity()).openDeleteReservationDialog(info);
    }

}