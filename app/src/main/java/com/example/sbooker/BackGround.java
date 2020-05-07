package com.example.sbooker;

import androidx.annotation.NonNull;

import com.example.sbooker.ui.data.model.hall;
import com.example.sbooker.ui.data.model.place;
import com.example.sbooker.ui.data.model.reservation;
import com.example.sbooker.ui.data.model.staticReservation;
import com.example.sbooker.ui.data.model.user;
import com.example.sbooker.ui.login.LoginViewModel2;
import com.example.sbooker.ui.register.RegisterViewModel;
import com.example.sbooker.ui.reservations.ReservationViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *  Offers database methods.
 */

public class BackGround { //offers database methods
    ArrayList<reservation> usersReservations= new ArrayList<>();
    private hall Hall = null;
   // MainViewModel mvm;
    public BackGround(){
        //System.out.println("THIS WORKS?");
        //DBConnection();
    }



    private void DBConnection(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
    }

    private void getDataFromDB(){

    }

    public void writeDataToDB(){

    }

    public void refreshData(){

    }
    public static void login(final String userName, final String password,  final LoginViewModel2 LVM) { // takes care of login
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                if(dataSnapshot.child(userName).exists()){
                    System.out.println("username found");
                    if(!userName.isEmpty()){
                        user login = dataSnapshot.child(userName).getValue(user.class);
                        if(login.getPsw().equals(password)){
                            System.out.println("Logging in ");
                            LVM.setUSER(login);
                            LVM.setText("Login Success");
                        }else{
                            LVM.setText("Invalid password");
                        }
                    }
                }else{
                    LVM.setText("Username Not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void makeNewUser(final String userName, final String psw, final String email, final RegisterViewModel RVM) { //makes new user returns error if username taken
        String result = "";
        final boolean[] succes= {false};
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.child(userName).toString());
                if(dataSnapshot.child(userName).exists()){
                    RVM.setText("User name taken");
                }else {
                    ArrayList<String> resRights = new ArrayList<>();
                    resRights.add("Halli yksi");
                    user newUser = new user(email,psw,userName);
                    newUser.setResRights(resRights);
                    Map<String,Object> nUserMap= new HashMap<>();
                    nUserMap.put(newUser.getUsername(),newUser);
                    ref.updateChildren(nUserMap);
                    RVM.setText("Registration successful");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


    }
    public static void addHallRight(MainViewModel mvm, final String hallName){ // add rights to hall for user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users").child(mvm.getUser().getUsername());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                    user tempU = dataSnapshot.getValue(user.class);
                    ArrayList<String>tempL = tempU.getResRights();
                    tempL.add(hallName);
                    tempU.setResRights(tempL);
                    ref.setValue(tempU);
                    /*
                    Map<String,Object> nUserMap = new HashMap<>();
                    nUserMap.put(tempU.getUsername(),tempU);

                    ref.setValue(nUserMap);*/
                    //RVM.setText("Registration successful");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    public static void deleteUser(MainViewModel mvm){ //deletes logged in user
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Users").child(mvm.getUser().getUsername());
        ref.removeValue();
    }
    public void getUserReservations(final user u, final ReservationViewModel rvm) { // Gets users reservations form db and makes them to string format to be shown and uploads it to ReservationViewmodel
        System.out.println("Getting users reservations "+u.getUsername());
        final ArrayList<reservation> userRes = new ArrayList<>();
        final ArrayList<hall> allHalls = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Halls");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    allHalls.add(ds.getValue(hall.class));
                }
                for(hall h :allHalls){
                   ArrayList<place> placeList = h.getSportPlaces();
                    //System.out.println(h.getHallName());
                   for (place p :placeList){
                       //System.out.println(p.getPlaceName());
                      ArrayList<reservation> resList =  p.getReservations();
                      for (reservation r : resList){
                          //System.out.println(r.getMakerName());
                          if(r.getMakerName().equals(u.getUsername())){

                              userRes.add(r);
                          }
                      }
                   }
                }
                rvm.setReservations(userRes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //makes single reservation to database
    public void makeReservation(user u, String hall, String placeIndx, final String place, String timeTag,String day,String month,String year,String sport,String info){
        String userName = u.getUsername();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //find the right child node
        final DatabaseReference ref = database.getReference("Halls").child(hall).child("sportPlaces").child(placeIndx);

        double t1 = Double.parseDouble(timeTag);
        int y = Integer.parseInt(year);
        int m = Integer.parseInt(month);
        int d =Integer.parseInt(day);
        //create reservation object
        final reservation res = new reservation(userName,hall,place,y,m,d,t1,t1+1,sport,info);

        // pushes the reservation to right child note.
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                place p = dataSnapshot.getValue(place.class);
                p.addReservations(res);
                ref.setValue(p);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    public void deleteReservation(user user, String hall, String placeIndx, String place, final String timeTag, String day, String month, String year) { //deletes reservation
        String userName = user.getUsername();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //find the right child node
        final DatabaseReference ref = database.getReference("Halls").child(hall).child("sportPlaces").child(placeIndx);

        final double t1 = Double.parseDouble(timeTag);
        final int y = Integer.parseInt(year);
        final int m = Integer.parseInt(month);
        final int d =Integer.parseInt(day);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //finds the reservation and then replaces the reservation list with list not containing this reservation
                    DataSnapshot ds = dataSnapshot;
                    System.out.println(ds.toString());
                    place p = ds.getValue(place.class);
                    System.out.println(p.getPlaceName());
                    int i=0;
                    int j = 0;
                    for(reservation res : p.getReservations()){
                        //System.out.println();
                        if(t1==res.getStartTime() && y == res.getYear() && d == res.getDay() && m == res.getMonth()){
                            //p.getReservations().remove(i);
                            j= i;
                            System.out.println("this path is to be deleted: " + ref+"/"+j);
                            break;
                        }
                    i++;
                    }
                p.getReservations().remove(j);
                ref.setValue(p);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public hall getHallFromDB(final String hallKey){//this method does not work?

        System.out.println("Haetaan databasesta hall....");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Halls");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(hallKey);
                //for(DataSnapshot ds: dataSnapshot.getChildren()) {
                System.out.println(ds.toString());
                hall h = ds.getValue(hall.class);
                System.out.println(h.getHallName());

                setHall(h);
                //}
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());

            }
        });
        return this.Hall;
    }

    public hall getHall(String hallKey){
        if(this.Hall==null){
            //int ok = getHallFromDB(hallKey);
            //return(this.Hall);
        } else if (!this.Hall.getHallName().equals(hallKey)){
           // int ok  = getHallFromDB(hallKey);
            //return(this.Hall);
        }
        return (getHallFromDB(hallKey));
    }

    public void setHall(hall h){
        System.out.println("Setting hall in bk");
        this.Hall= h;
        System.out.println(Hall.getHallName());
    }

    public static void addHall(String s){ //adds hall to database
        final hall Hall = new hall();
        Hall.setHallName(s);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Halls");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map<String,Object> nHallMap= new HashMap<>();
                nHallMap.put(Hall.getHallName(),Hall);
                ref.updateChildren(nHallMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public static void addPlace(String hallName, final String placeName) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference ref = database.getReference("Halls").child(hallName);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // System.out.println(dataSnapshot.toString());
                hall tempHall = dataSnapshot.getValue(hall.class);
                tempHall.addSportPlace(new place(placeName));
                ref.setValue(tempHall);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void makeStaticReservation(final staticReservation sR, final ArrayList<reservation> wantedRes, final ReservationViewModel RVM) { //try's to make static reservation to firebase otherwise returns error message

            System.out.println("Getting reservations ");
            final ArrayList<place> allPlaces = new ArrayList<>();
            //makes wanted reservations.


            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference ref = database.getReference("Halls").child(sR.getHall());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Boolean hall_found = false;

                        //System.out.println(ds);
                        hall h = dataSnapshot.getValue(hall.class);
                        for(place p: h.getSportPlaces()){
                            int i = 0; //used to update right place
                            ArrayList<reservation> Reservations = p.getReservations();
                            ArrayList<reservation> endReservations =new ArrayList<>();
                            if(p.getPlaceName().equals(sR.getPlaceName())){
                                hall_found = true;

                                for(reservation wRes : wantedRes){//Loops both wanted reservations and excisting ones
                                    boolean resTaken = false;
                                    for(reservation r: Reservations){
                                        if(wRes.getYear()==r.getYear() //if wanted time is taken we add existing reservation to end reservations.
                                                && wRes.getMonth()==r.getMonth()
                                                && wRes.getDay()==r.getDay()
                                                && wRes.getStartTime()==r.getStartTime()){

                                            System.out.println(wRes.getDay()+"."+wRes.getMonth());
                                            //endReservations.add(r);
                                            resTaken=true;
                                        }
                                    }
                                    if(!resTaken){ //if taken reservation not found we add wanted reservation to end reservations
                                        endReservations.add(wRes);
                                    }
                                }
                                endReservations.addAll(Reservations);
                            }else{
                                endReservations.addAll(Reservations);
                            }
                            p.setReservations(endReservations);

                        i++;
                        }

                        ref.setValue(h);
                        if(!hall_found ){
                           RVM.setStaticResSuccess("Place was not found");
                        }
                        else{
                            RVM.setStaticResSuccess("Reservations made if not taken, please check reservations");
                        }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }


            });
    }



    public void populateDB(){// to make the database.
        hall Hall = new hall();
        Hall.setHallName("Halli kaksi");

        place p = new place("Kenttä1");
        reservation r = new reservation("setup","halli yksi",p.getPlaceName(),2020,1,1,1,1," "," ");
        p.addReservations(r);
        System.out.println(p.getPlaceName()+p.getReservations().get(0).getMakerName());
        Hall.addSportPlace(p);

        p = new place("Kenttä2");
        r = new reservation("setup","halli yksi",p.getPlaceName(),2020,1,1,1,1," "," ");
        p.addReservations(r);
        Hall.addSportPlace(p);

        p = new place("Kenttä3");
        r = new reservation("setup","halli yksi",p.getPlaceName(),2020,1,1,1,1," "," ");
        p.addReservations(r);
        Hall.addSportPlace(p);

        p = new place("Kenttä4");
        r = new reservation("setup","halli yksi",p.getPlaceName(),2020,1,1,1,1," ", " ");
        p.addReservations(r);
        Hall.addSportPlace(p);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Halls");
        Map<String,Object> halls = new HashMap<>();
        halls.put(Hall.getHallName(),Hall);

        myRef.updateChildren(halls);

    }


    public void getHallKeysFromDB(final MainViewModel mvm) { //Gets all halls and returns them in string array
        final ArrayList<String> keys = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Halls");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    keys.add(ds.getKey());

                    //System.out.println(ds.toString());
                    //System.out.println(ds.getKey());
                }
                mvm.setHallKeyArray(keys);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }


}
