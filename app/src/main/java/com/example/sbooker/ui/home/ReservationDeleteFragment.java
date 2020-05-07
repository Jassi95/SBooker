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

import com.example.sbooker.MainActivity;
import com.example.sbooker.R;
/**
 *  Pop up dialog when deleting reservation
 */
public class ReservationDeleteFragment extends AppCompatDialogFragment {
    private TextView info;
    //private Button cancelButton;
    String ResInfo;
    String tag;

    public ReservationDeleteFragment(String info){
        this.tag=info;
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

        builder.setView(view).setTitle("Delete reservation").setNegativeButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ((MainActivity) getActivity()).deleteReservation(tag);
            }
        });
        info = view.findViewById(R.id.reservationInfo);
        info.setText(ResInfo +"\n Are you sure you wanna delete this reservation?");
        //cancelButton = view.findViewById(R.id.reservationCancelButton);

        return builder.create();
    }
}
