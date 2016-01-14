package com.mad.tedx.bluetoothattendancetracker;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ManjuRaghavendra on 10/28/2015.
 */
public class StudentAttendanceA implements Serializable {
    private String name, className, status;

    public StudentAttendanceA(ParseObject obj) {


        ParseUser user = obj.getParseUser("student");
        this.name = user.getString("firstName") + " " + user.getString("lastName");

        this.className = obj.getParseObject("class").getString("name");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
