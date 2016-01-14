package com.mad.tedx.bluetoothattendancetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;
import java.util.Map;


/**
 * File Name: SignUpActivity.java
 * Team Members:  Manju Raghavendra Bellamkonda
 */
public class SignUpActivity extends Activity {

    EditText firstName, lastName, userName, password, confirmPassword;
    Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        loadAllViews();
        onClickListeners();
    }

    private void onClickListeners() {
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    if (checkForNull()) {
                        final ParseUser user = new ParseUser();
                        user.setEmail(userName.getText().toString());
                        user.setUsername(userName.getText().toString());
                        user.setPassword(password.getText().toString());
                        user.put("firstName", firstName.getText().toString());
                        user.put("lastName", lastName.getText().toString());
                        user.put("role", AttendanceConstants.ROLE_USER);

                        user.signUpInBackground(new SignUpCallback() {

                            @Override
                            public void done(ParseException arg0) {

                                if (arg0 == null) {
                                    Toast.makeText(
                                            getBaseContext(),
                                            "User Created and Logged in successfully",
                                            Toast.LENGTH_LONG).show();
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("userid", ParseUser.getCurrentUser().getUsername());
                                    map.put("message", "New user " + user.get("firstName") + " " + user.get("lastName") + " has been signed up");
                                    map.put("place", "SIGN_UP");
                                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                    installation.put("user", user.getUsername());
                                    installation.saveInBackground();
                                    ParseCloud.callFunctionInBackground("pushMessage", map, new FunctionCallback<Object>() {
                                        @Override
                                        public void done(Object response, ParseException exc) {
                                            Toast.makeText(SignUpActivity.this, "Test", Toast.LENGTH_LONG).show();
                                            Log.e("cloud code example", "response: " + response);
                                        }
                                    });
                                    Intent intent = new Intent(
                                            getBaseContext(),
                                            HomeActivity.class);
                                    startActivity(intent);

                                    finish();
                                } else {
                                    Toast.makeText(getBaseContext(),
                                            arg0.getLocalizedMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                } else {
                    Toast.makeText(getBaseContext(), "No Internet Connection",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void loadAllViews() {
        firstName = (EditText) findViewById(R.id.editText1);
        lastName = (EditText) findViewById(R.id.editText2);
        userName = (EditText) findViewById(R.id.editText3);
        password = (EditText) findViewById(R.id.editText4);
        confirmPassword = (EditText) findViewById(R.id.editText5);
        signUp = (Button) findViewById(R.id.button1);

    }

    private boolean checkForNull() {
        boolean status = true;
        String regExp = "\\b[A-Za-z0-9._%+-]+@[UNCC|uncc]+\\.[EDU|edu]{3}\\b";
        String passwordStr = password.getText().toString();
        String confirmPasswordStr = confirmPassword.getText().toString();
        String emailStr = userName.getText().toString();
        if (firstName.getText().toString().trim().equals("")) {
            firstName.setError("Please Enter First Name");
            status = false;
        }
        if (lastName.getText().toString().trim().equals("")) {
            lastName.setError("Please Enter Last Name");
            status = false;
        }
        if (emailStr.equals("")) {
            userName.setError("Please Enter Email");
            status = false;
        }
        if (passwordStr.equals("")) {
            password.setError("Please Enter Password");
            status = false;
        }
        if (confirmPasswordStr.equals("")) {
            confirmPassword.setError("Please Enter Confirm Password");
            status = false;
        }
        if (!passwordStr.equals(confirmPasswordStr)) {
            confirmPassword.setError("Password Does not match");
            status = false;
        }
        if (!emailStr.matches(regExp)) {
            userName.setError("Please enter UNCC Mail id");
            status = false;
        }

        return status;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
