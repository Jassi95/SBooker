package com.example.sbooker.ui.reservations;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sbooker.BackGround;
import com.example.sbooker.MainViewModel;
import com.example.sbooker.R;
/**
 *  Takes care of the reservations tab
 */
public class ReservationFragment extends Fragment {

    private ReservationViewModel RVM;
    private MainViewModel MVM;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RVM = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);
        this.MVM = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);
        Button statResButton = root.findViewById(R.id.makeStaticReservation);
        final BackGround bk = new BackGround();
        textView.setMovementMethod(new ScrollingMovementMethod());

        RVM.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(MVM.getLoggedIn() == false){
                    textView.setText("Log in to see your reservations");
                } else{
                    textView.setText(s);
                }
            }
        });

        RVM.getStaticResSucces().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!s.isEmpty()) {
                    Toast toast = Toast.makeText(getContext(), s, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        MVM.LoggedListener().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(MVM.getLoggedIn() == false){
                    textView.setText("Log in to see your reservations");
                } else{
                    bk.getUserReservations(MVM.getUser(),RVM);
                    //textView.setText(RVM.getText().getValue());
                }
            }
        });



        statResButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeStaticReservationDialog resDialog = new makeStaticReservationDialog(RVM,MVM);
                resDialog.show(getChildFragmentManager(), "add new statc reservation");
                //makeStaticReservation(1,MVM.getUser().getUsername(),"halli yksi","kenttä1",3,6,12.0,13.0,"Keilausta","Vakio vuoro");
            }
        });
        return root;
    }



}
