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
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class StudentAttendanceActivity extends Fragment {


    Context con;
    ListView lv;
    HomeActivity activity;

    ArrayList<StudentClass> conferences = new ArrayList<StudentClass>();
    StudentClassAdapter adapter;
    String classId="LNbu29ACaO";
    public StudentAttendanceActivity(Context con) {
        this.con = con;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        //addStudentAttendance();
        View view = inflater.inflate(R.layout.activity_class, container, false);
        lv = (ListView) view.findViewById(R.id.listView1);

        getAttendanceList(classId);


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
            /*if (user != null && user.isAuthenticated()) {
                activity.redirectToToDoFragment();
            }*/
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

    private void getAttendanceList(String classId) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_CLASS_STUDENTS);
        query.whereEqualTo("student",ParseUser.getCurrentUser());
        try {
            List<ParseObject> list = query.find();
            if (list != null) {
                for (ParseObject obj : list) {
                    StudentClass c = new StudentClass(obj,null);
                    conferences.add(c);

                }
                adapter = new StudentClassAdapter(con, conferences);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
