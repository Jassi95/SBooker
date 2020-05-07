package com.example.sbooker.ui.home;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sbooker.R;
/**
 *  Pop up dialog for making a reservation
 */

public class ReservationMakeFragment extends AppCompatDialogFragment {
    private TextView info;
    private EditText resInfo;
    private EditText sportInfo;
    private MakeResListener listener;
    String ResInfo,tag;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener =(MakeResListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "Must implement Listener");
        }
    }


    public ReservationMakeFragment(String info){
        this.tag =info;
        String iArr[] = info.split(";");
        this.ResInfo = ("Hall: "+iArr[7]+"\nPlace: "+iArr[2]+"\nDate: "+iArr[4]+"."+iArr[5]+"."+iArr[6]+"\nFrom: "+iArr[3]+" to "+(Double.parseDouble(iArr[3])+1)+"\nReserved by: "+iArr[8]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_make_reservation,null);

        builder.setView(view).setTitle("Reservation").setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Make Reservation", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ResInfo = resInfo.getText().toString();
                String SportInfo = sportInfo.getText().toString();

                listener.applyReservation(tag, ResInfo,SportInfo);
            }
        });
        info = view.findViewById(R.id.reservationInfo);
        info.setText(ResInfo);
        resInfo = view.findViewById(R.id.addResInfo);
        sportInfo= view.findViewById(R.id.addSportInfo);

        return builder.create();
    }



    public interface MakeResListener{
        void applyReservation(String tag, String ResInfo, String SportInfo);
    }
}
