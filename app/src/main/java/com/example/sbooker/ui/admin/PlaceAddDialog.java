package com.example.sbooker.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sbooker.BackGround;
import com.example.sbooker.R;

import java.util.ArrayList;
import java.util.Objects;
/**
 *  Pop up dialog to add new place to existing hall.
 */
public class PlaceAddDialog extends AppCompatDialogFragment {
    EditText PlaceNameEditText;
    ArrayList<String> halls;
    Spinner hallsSpinner;
    public PlaceAddDialog(ArrayList<String> Halls){
        this.halls = Halls;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_place,null);

        builder.setView(view).setTitle("Add new place").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String placeName = PlaceNameEditText.getText().toString();
                String hallName = hallsSpinner.getSelectedItem().toString();
                if(!placeName.trim().isEmpty() ){
                    System.out.println("Making new place named "+hallName+ "to hall "+hallName);
                    BackGround.addPlace(hallName,placeName);
                }
            }
        });
        PlaceNameEditText = view.findViewById(R.id.addPlace);
        hallsSpinner = view.findViewById(R.id.chooseHallSpinner);
        updateHallSpinner(halls,view);
        //cancelButton = view.findViewById(R.id.reservationCancelButton);

        return builder.create();
    }

    private void updateHallSpinner(ArrayList<String> hallKeyArray, View v) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, hallKeyArray);
        //System.out.println(getContext().toString()+ " This si the context");
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hallsSpinner.setAdapter(adapter);
        hallsSpinner.setSelection(1);
    }
}
