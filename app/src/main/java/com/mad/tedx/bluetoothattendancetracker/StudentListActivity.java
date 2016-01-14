package com.mad.tedx.bluetoothattendancetracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class StudentListActivity extends Activity {


    HomeActivity activity;

    ArrayList<Student> studentList = new ArrayList<Student>();
    StudentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class);
        ListView lv = (ListView) findViewById(R.id.listView1);
        String classId = getIntent().getExtras().get("classId").toString();
        getStudentList(classId);
        if (studentList == null) {
            studentList = new ArrayList<Student>();
        }
        adapter = new StudentAdapter(this, studentList);
        lv.setAdapter(adapter);


    }

    private void getStudentList(String classID) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_CLASS_STUDENTS);
        query.whereEqualTo("classId", classID);
        try {
            List<ParseObject> list = query.find();
            if (list != null) {
                for (ParseObject obj : list) {
                    Student s = new Student(obj, getStudentsListInMap() );
                    studentList.add(s);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private Map<String, ParseUser> getStudentsListInMap() {

        final Map<String, ParseUser> selectedUsers = new HashMap<String, ParseUser>();
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.addAscendingOrder("lastName");
        userQuery.whereEqualTo("role", AttendanceConstants.ROLE_USER);
        try {
            List<ParseUser> usersList = userQuery.find();

            for (ParseUser currentUser : usersList) {
                selectedUsers.put(currentUser.getObjectId(), currentUser);
            }
        } catch (Exception e) {
            e.printStackTrace();


        }
        return selectedUsers;
    }

}
