package com.example.sbooker.ui.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;


import com.example.sbooker.BackGround;

import com.example.sbooker.MainViewModel;
import com.example.sbooker.R;
import java.util.ArrayList;
import java.util.Objects;
/**
 *  Takes care of admin tab, shows different options depending if the user is ADMIN or not. Try login with admin1 psw: 12356
 */
public class AdminFragment extends Fragment {


    MainViewModel MVM;
    Spinner hallSpinner;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MVM = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        View root = inflater.inflate(R.layout.fragment_admin, container, false);
        final TextView userNameInfo = root.findViewById(R.id.userNameInfo);
        final TextView userEmailInfo = root.findViewById(R.id.userEmailInfo);
        final Button delUser = root.findViewById(R.id.deleteUserButton);
        final Button addHallB = root.findViewById(R.id.addHallButton);
        final Button addPlaceB = root.findViewById(R.id.addPlaceButton);
        final TextView curHallRights = root.findViewById(R.id.curHallRights);
        final Button addRights = root.findViewById(R.id.addHallRightB);
        this.hallSpinner = root.findViewById(R.id.addHallRightSp);




        if(!MVM.getUser().getUsername().equals("admin1")){ //makes hall adding and place adding to not show
            addHallB.setVisibility(View.INVISIBLE);
            addPlaceB.setVisibility(View.INVISIBLE);
        }

        userNameInfo.setText(MVM.getUser().getUsername());
        userEmailInfo.setText(MVM.getUser().getEmail());
        curHallRights.setText("You have rights to following halls: "+MVM.getUser().getResRights().toString());
        updateHallSpinner(MVM.getHallKeyArray(), root);

        addRights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hallName = hallSpinner.getSelectedItem().toString();
                if(!MVM.getUser().getResRights().contains(hallName)){
                    BackGround.addHallRight(MVM,hallName);
                    Toast toast = Toast.makeText(getContext(), "Retry login to apply changes", Toast.LENGTH_SHORT);
                }else{
                    Toast toast = Toast.makeText(getContext(), "You already have rights to this hall", Toast.LENGTH_SHORT);
                }
            }
        });


        delUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackGround.deleteUser(MVM);
                Toast toast = Toast.makeText(getContext(), "User deleted", Toast.LENGTH_SHORT);
                MVM.logOut();
            }
        });

        addHallB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((MainActivity) getActivity()).openAddHallDialog();
                HallAddDialog resDialog = new HallAddDialog(MVM.getHallKeyArray());
                resDialog.show(getChildFragmentManager(), "add new hall");
            }
        });

        addPlaceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlaceAddDialog resDialog = new PlaceAddDialog(MVM.getHallKeyArray());
                resDialog.show(getChildFragmentManager(),"add new place");
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
    }



}
