package com.example.sbooker.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sbooker.BackGround;
import com.example.sbooker.R;

import java.util.regex.Pattern;
/**
 *  Takes care of the registeration
 */
public class RegisterActivity extends AppCompatActivity {
    EditText usernameEditText;
    EditText passwordEditText1;
    EditText passwordEditText2;
    EditText emailEditText;
    RegisterViewModel RVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.emailEditText = findViewById(R.id.email);
        this.usernameEditText = findViewById(R.id.username);
        this.passwordEditText1 = findViewById(R.id.password1);
        this.passwordEditText2 = findViewById(R.id.password2);
        this.RVM = new ViewModelProvider(this).get(RegisterViewModel.class);

        final Button registerButton = findViewById(R.id.registerButton);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);


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
               if(validateUser(emailEditText.getText().toString(),usernameEditText.getText().toString().trim(),passwordEditText1.getText().toString(),passwordEditText2.getText().toString())){
                   registerButton.setEnabled(true);
               }else{
                   registerButton.setEnabled(false);
               }
            }
        };
        emailEditText.addTextChangedListener(afterTextChangedListener);
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText1.addTextChangedListener(afterTextChangedListener);
        passwordEditText2.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackGround.makeNewUser(usernameEditText.getText().toString(),passwordEditText1.getText().toString(),emailEditText.getText().toString(),RVM);
            }
        });

        RVM.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(s.equals("User name taken")){
                    Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                }else if(s.equals("Registration successful")){
                    Toast.makeText(getApplicationContext(),s, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }

    private boolean validateUser(String email, String userName, String psw1,String psw2) {
        Boolean valid = false;
        //validate email
        String emailVal= "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(emailVal);
        String userVal = "^[a-z0-9_-]{4,16}$";
        Pattern uPattern = Pattern.compile(userVal);

        if(!pattern.matcher(email).matches()){
                this.emailEditText.setError(getString(R.string.email_ERROR));
        }

        else if(!uPattern.matcher(userName).matches()){
            this.usernameEditText.setError(getString(R.string.too_short_username));

        }
        else  if(userName.isEmpty() || psw1.isEmpty() ){}// filling not completed

        else if(!userName.isEmpty() || !psw1.isEmpty() ){
            if(psw1.length() < 6){
                this.passwordEditText1.setError(getString(R.string.invalid_password));

            }if(!psw1.equals(psw2)){
                this.passwordEditText2.setError(getString(R.string.password_no_match));

            }else{
                valid = true;
            }

        }

        return valid;
    }



}

