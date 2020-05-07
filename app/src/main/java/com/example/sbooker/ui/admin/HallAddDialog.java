package com.example.sbooker.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sbooker.BackGround;
import com.example.sbooker.R;

import java.util.ArrayList;
/**
 *  Pop up dialog to setup new hall
 */
public class HallAddDialog extends AppCompatDialogFragment {
    EditText hallNameEditText;
    ArrayList<String> halls;
    public HallAddDialog(ArrayList<String> Halls){
        this.halls = Halls;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_hall,null);

        builder.setView(view).setTitle("Add new hall").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String hallName = hallNameEditText.getText().toString();

                if(!hallName.trim().isEmpty() && !halls.contains(hallName)){
                    System.out.println("Making new hall named "+hallName);
                    BackGround.addHall(hallName);
                }
            }
        });
        hallNameEditText = view.findViewById(R.id.addHallName);

        //cancelButton = view.findViewById(R.id.reservationCancelButton);

        return builder.create();
    }
}
