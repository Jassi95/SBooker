package com.example.sbooker.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.sbooker.R;
/**
 *  Pop up dialog when looking at reservation
 */

public class ReservationInfoFragment extends AppCompatDialogFragment {
    private TextView info;
    String ResInfo;
    ReservationInfoFragment(String info){
        String iArr[] = info.split(";");
        this.ResInfo = ("Hall: "+iArr[7]+"\nPlace: "+iArr[2]+"\nDate: "+iArr[4]+"."+iArr[5]+"."+iArr[6]+"\nFrom: "+iArr[3]+" to "+(Double.parseDouble(iArr[3])+1)+"\nReserved by: "+iArr[8]+
                "\nInfo: "+iArr[9]+"\nsport:"+iArr[10]);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_reservation,null);

        builder.setView(view).setTitle("Reservation").setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        info = view.findViewById(R.id.reservationInfo);
        info.setText(ResInfo);


        return builder.create();
    }
}
