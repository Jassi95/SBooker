package com.example.sbooker.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sbooker.BackGround;
import com.example.sbooker.R;
import com.example.sbooker.ui.register.RegisterActivity;


import java.util.regex.Pattern;
/**
 *  Takes care of login in returns the users information to mainActivity if succesful.
 */
public class LoginActivity2 extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText;
    LoginViewModel2 LVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.usernameEditText = findViewById(R.id.username);
        this.passwordEditText = findViewById(R.id.password);

        this.LVM = new ViewModelProvider(this).get(LoginViewModel2.class);

        //final Button toLogin = findViewById(R.id.goToLogin);
        final Button loginButton = findViewById(R.id.registerButton);
        final Button registerButton = findViewById(R.id.toRegisterButton);


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(validateUser(usernameEditText.getText().toString().trim(),passwordEditText.getText().toString())){
                    loginButton.setEnabled(true);
                }else{
                    loginButton.setEnabled(false);
                }
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackGround.login(usernameEditText.getText().toString(),passwordEditText.getText().toString(), LVM);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent s = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(s);

            }
        });

        LVM.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                System.out.println(s);
                if(s.equals("Username Not found")){
                    Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                }else if(s.equals("Invalid password")){
                    Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();

                }else if(s.equals("Login Success")){ //Todo return User
                    Intent returnIntent = getIntent();
                    returnIntent.putExtra("result", LVM.getUSER());
                    //Complete and destroy login activity once successful
                    setResult(Activity.RESULT_OK, returnIntent);

                    finish();


                }
            }
        });


    }

    private boolean validateUser( String userName, String psw1) {
        Boolean valid = false;
        //validate email
        String userVal = "^[a-z0-9_-]{4,16}$";
        Pattern uPattern = Pattern.compile(userVal);

       if(!uPattern.matcher(userName).matches()){
            this.usernameEditText.setError(getString(R.string.too_short_username));

        }
        else  if(userName.isEmpty() || psw1.isEmpty() ){}// filling not completed

        else if(!userName.isEmpty() || !psw1.isEmpty() ){
            if(psw1.length() < 6){
                this.passwordEditText.setError(getString(R.string.invalid_password));

            }else{
                valid = true;
            }

        }

        return valid;
    }
}
