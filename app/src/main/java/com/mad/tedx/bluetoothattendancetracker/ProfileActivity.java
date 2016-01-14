package com.mad.tedx.bluetoothattendancetracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class ProfileActivity extends Fragment {


    Context con;

    HomeActivity activity;

    ArrayList<ClassParseObject> conferences = new ArrayList<ClassParseObject>();
    ClassAdapter adapter;

    public ProfileActivity(Context con) {
        this.con = con;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.activity_profile, container, false);
        TextView firstName, lastName;
        firstName= (TextView) view.findViewById(R.id.textView2);
        lastName= (TextView) view.findViewById(R.id.textView4);
        ParseUser user=ParseUser.getCurrentUser();
        firstName.setText(user.getString("firstName"));
        lastName.setText(user.getString("lastName"));
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onViewCreated(view, savedInstanceState);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isNetworkAvailable()) {
            ParseUser user = ParseUser.getCurrentUser();

        } else {
            Toast.makeText(con, "No Network Connection Available",
                    Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) con
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
