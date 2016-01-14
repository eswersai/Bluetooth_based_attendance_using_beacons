package com.mad.tedx.bluetoothattendancetracker;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by ManjuRaghavendra on 10/28/2015.
 */
public class StudentAttendance implements Serializable{
    private String name, className, status;

    public StudentAttendance(ParseObject obj)
    {

        this.name=obj.getString("name");
        this.status=obj.getString("code");
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
}
