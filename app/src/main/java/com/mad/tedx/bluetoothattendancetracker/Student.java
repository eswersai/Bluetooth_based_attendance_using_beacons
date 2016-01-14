package com.mad.tedx.bluetoothattendancetracker;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Map;

/**
 * Created by ManjuRaghavendra on 12/8/2015.
 */
public class Student {

    private String name;

    public Student(ParseObject obj, Map<String,ParseUser> users){

        ParseUser user;
        if(users!=null) {
            user = users.get(obj.get("studentId"));
            this.name = user.getString("firstName") + " " + user.getString("lastName");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
