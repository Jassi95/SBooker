package com.example.sbooker;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sbooker.ui.data.model.user;
import com.example.sbooker.ui.home.ReservationDeleteFragment;
import com.example.sbooker.ui.home.ReservationMakeFragment;
import com.example.sbooker.ui.login.LoginActivity2;

import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements ReservationMakeFragment.MakeResListener {
    private AppBarConfiguration mAppBarConfiguration;
    private MainViewModel mainViewModel;
    private Toolbar toolbar;
    BackGround bk;
    private NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bk = new BackGround();
        //bk.populateDB(); //populates database
        this.mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        setContentView(R.layout.activity_main);


        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        this.navigationView = findViewById(R.id.nav_view);




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        bk.getHallKeysFromDB(mainViewModel);

        mainViewModel.LoggedListener().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    getSupportActionBar().setTitle("User: "+mainViewModel.getUser().getUsername());
                }
                else{
                    getSupportActionBar().setTitle("Home");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_LogIn:

                // opens Login activity.
                System.out.println("Going to log in");
                Intent myIntent = new Intent(MainActivity.this, LoginActivity2.class);

                MainActivity.this.startActivityForResult(myIntent, 100);
                return true;
            case R.id.action_LogOut:
                mainViewModel.logOut();

                System.out.println("Logging out");

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean onPrepareOptionsMenu(Menu menu)
    {

        MenuItem loggedIn = menu.findItem(R.id.action_LogIn);
        MenuItem loggedOut= menu.findItem(R.id.action_LogOut);
        if(mainViewModel.getLoggedIn())
        {

            loggedIn.setVisible(false);
            loggedOut.setVisible(true);
        }
        else
        {
            loggedIn.setVisible(true);
            loggedOut.setVisible(false);
        }
        return true;
    }


    @Override
    public boolean onSupportNavigateUp() {
        Menu nav_Menu = navigationView.getMenu();
        if(mainViewModel.getLoggedIn()){
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(true);
            nav_Menu.findItem(R.id.nav_gallery).setVisible(true);
        }else{
            nav_Menu.findItem(R.id.nav_slideshow).setVisible(false);
            nav_Menu.findItem(R.id.nav_gallery).setVisible(false);
        }

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                //assert data != null;
                user u = (user)data.getExtras().getParcelable("result");

                System.out.println("This returned: " + u.getUsername()+ u.getEmail() + u.getResRights());
                mainViewModel.setUser(u);


            } else {
                // handle cancellation
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setUserName(user u){
        String email =u.getEmail();
        String psw = u.getPsw();
        String username = u.getUsername();
        user newUser = new user();
        mainViewModel.setUser(u);
    }

    public void openReservationDialog(String info){
        ReservationMakeFragment resDialog = new ReservationMakeFragment(info);
        resDialog.show(getSupportFragmentManager(), "Make Reservation");
    };

    public void makeReservation(String buttonTag) {
        System.out.println("This is button "+ buttonTag);
        String[] BT = buttonTag.split(";"); // parse the tag
        String placeIndx = BT[0];
        String place = BT[1];
        String timeTag = BT[2];
        String day = BT[3];
        String month = BT[4];
        String year = BT[5];
        String hall = BT[6];
        String info = BT[8];
        String sport = BT[9];
        //System.out.println(buttonTag);
        if(this.mainViewModel.getLoggedIn()){

            bk.makeReservation(this.mainViewModel.getUser(),hall,placeIndx, place,timeTag,day,month,year,sport,info);
            System.out.println("Making reservation");
        }
    }

    public void deleteReservation(String buttonTag){
        buttonTag = buttonTag.substring(buttonTag.indexOf(";")+1);
        System.out.println("This is button "+ buttonTag);
        String BT[] = buttonTag.split(";"); // parse the tag
        String placeIndx = BT[0];
        String place = BT[1];
        String timeTag = BT[2];
        String day = BT[3];
        String month = BT[4];
        String year = BT[5];
        String hall = BT[6];

        System.out.println(place + " "+ timeTag+ " "+Double.parseDouble(timeTag));
        if(this.mainViewModel.getLoggedIn()){

            bk.deleteReservation(this.mainViewModel.getUser(),hall,placeIndx, place,timeTag,day,month,year);
            System.out.println("Reservation deleted");
        }
    }

    @Override
    public void applyReservation(String tag,String ResInfo, String SportInfo) {
        if(ResInfo.trim().isEmpty()){ResInfo = "No information given";}
        if(SportInfo.trim().isEmpty()){SportInfo = "No sport information given";}

        tag = tag.substring(tag.indexOf(";") + 1);
        makeReservation(tag+";"+ResInfo+";"+SportInfo);
        //System.out.println("we would make reservation with"+tag+";"+ResInfo+";"+SportInfo);
    }

    public void openDeleteReservationDialog(String info) {
        ReservationDeleteFragment resDialog = new ReservationDeleteFragment(info);
        resDialog.show(getSupportFragmentManager(), "Delete Reservation");
    }



}
