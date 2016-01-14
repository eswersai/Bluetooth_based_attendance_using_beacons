package com.mad.tedx.bluetoothattendancetracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class StudentClassActivity extends Fragment {


    Context con;

    HomeActivity activity;

    ArrayList<StudentClass> conferences = new ArrayList<StudentClass>();
    StudentClassAdapter adapter;
    Map<String, ParseObject> classObjectMap = new HashMap<String, ParseObject>();
    Map<String, ParseObject> studentsObjectMap = new HashMap<String, ParseObject>();

    public StudentClassActivity(Context con) {
        this.con = con;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.activity_class, container, false);
        ListView lv = (ListView) view.findViewById(R.id.listView1);
        getStudentClassList();
        adapter = new StudentClassAdapter(con, conferences);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });


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

    private void getStudentClassList() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_CLASS_STUDENTS);
        getClassesListInMap();
        getStudentsListInMap();
        query.whereEqualTo("student", ParseUser.getCurrentUser());
        try {
            List<ParseObject> list = query.find();
            if (list != null) {
                for (ParseObject obj : list) {
                    String classId=obj.getString("classId");
                    ParseObject clasObj=classObjectMap.get(classId);
                    StudentClass c = new StudentClass(clasObj,classObjectMap.get(classId));
                    conferences.add(c);

                }
            }

        } catch (Exception e) {

        }


    }

    private void getClassesListInMap() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_CLASS);
        try {
            List<ParseObject> objList = query.find();
            if (objList != null) {
                for (ParseObject obj : objList) {
                    classObjectMap.put(obj.getObjectId(), obj);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getStudentsListInMap() {

        final Map<String, ParseUser> selectedUsers = new HashMap<String, ParseUser>();
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.addAscendingOrder("lastName");
        userQuery.whereEqualTo("role", AttendanceConstants.ROLE_USER);
        try {
            List<ParseUser> usersList = userQuery.find();

            for (ParseUser currentUser : usersList) {
                String currentUserlastName = currentUser
                        .getString("firstName")
                        + " "
                        + currentUser.getString("lastName");
                studentsObjectMap.put(currentUserlastName, currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();


        }

    }

}
