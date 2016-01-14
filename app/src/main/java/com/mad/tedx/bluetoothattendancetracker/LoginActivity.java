package com.mad.tedx.bluetoothattendancetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * File Name: LoginActivity.java
 * Team Members:  Manju Raghavendra Bellamkonda
 */
public class LoginActivity extends Activity {

	EditText userName, password;
	Button login;
	TextView signUp, forgotPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		loadAllViews();
        //addBeacons();
		ParseUser user = ParseUser.getCurrentUser();
		if (user != null && user.isAuthenticated()) {


			Intent intent = new Intent(getBaseContext(), HomeActivity.class);
			startActivity(intent);
			finish();

		}

		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkAvailable()) {
					if (checkForNull()) {
						ParseUser.logInInBackground(userName.getText()
								.toString(), password.getText().toString(),
								new LogInCallback() {

									@Override
									public void done(ParseUser arg0,
											ParseException arg1) {
										if (arg1 == null) {
                                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                            installation.put("user", ParseUser.getCurrentUser().getUsername());
                                            installation.saveInBackground();
											Intent intent = new Intent(
													getBaseContext(),
													HomeActivity.class);
											startActivity(intent);
											finish();

										} else {
											Toast.makeText(getBaseContext(),
													arg1.getLocalizedMessage(),
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

		signUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						SignUpActivity.class);
				startActivity(intent);
				finish();

			}
		});
	}

	private void addBeacons() {
		ParseObject obj=new ParseObject("Beacons");
		obj.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
		obj.put("major","15212");
		obj.put("minor","31506");
		obj.put("purpose","Homework");
		obj.put("region","");
		obj.put("location","Woodward 333F ");
        obj.saveEventually();

		obj=new ParseObject("Beacons");
		obj.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
		obj.put("major","48320");
		obj.put("minor","58596");
		obj.put("purpose","Homework");
		obj.put("region","");
		obj.put("location","Woodward 323B ");
        obj.saveEventually();

		obj=new ParseObject("Beacons");
		obj.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
		obj.put("major","41072");
		obj.put("minor","44931");
		obj.put("purpose","Homework");
		obj.put("region","");
		obj.put("location","Woodward 311 ");
        obj.saveEventually();

		//My Beacons
		obj=new ParseObject("Beacons");
		obj.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
		obj.put("major","16709");
		obj.put("minor","27702");
		obj.put("purpose","Homework");
		obj.put("region","");
		obj.put("location","Test1");
		obj.saveEventually();

		obj=new ParseObject("Beacons");
		obj.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
		obj.put("major","17390");
		obj.put("minor","14174");
		obj.put("purpose","Homework");
		obj.put("region","");
		obj.put("location","Test2");
		obj.saveEventually();

		obj=new ParseObject("Beacons");
		obj.put("uuid","B9407F30-F5F8-466E-AFF9-25556B57FE6D");
		obj.put("major","1250");
		obj.put("minor","32204");
		obj.put("purpose","Homework");
		obj.put("region","");
		obj.put("location","Test3");
		obj.saveEventually();
	}

	private void loadAllViews() {
		userName = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		login = (Button) findViewById(R.id.button1);
        forgotPassword = (TextView) findViewById(R.id.login);
		signUp = (TextView) findViewById(R.id.signup);

	}

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	private boolean checkForNull() {
		boolean status = true;
		if (userName.getText().toString().trim().equals("")) {
			userName.setError("Please Enter Username");
			status = false;
		}
		if (password.getText().toString().trim().equals("")) {
			password.setError("Please Enter Password");
			status = false;
		}
		return status;

	}
}
