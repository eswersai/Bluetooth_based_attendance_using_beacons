package com.mad.tedx.bluetoothattendancetracker;

import com.parse.ParseObject;

import java.io.Serializable;
import java.util.List;

public class StudentClass implements Serializable {
    private String id, classId, className;
    ParseObject classObj;

    public StudentClass(ParseObject obj, ParseObject classObj) {

        this.id = obj.getObjectId();
        this.classId = obj.getString("classId");
        this.className=classObj.getString("name");
        this.classObj=classObj;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public ParseObject getClassObj() {
        return classObj;
    }

    public void setClassObj(ParseObject classObj) {
        this.classObj = classObj;
    }
}
