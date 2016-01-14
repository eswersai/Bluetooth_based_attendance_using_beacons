package com.mad.tedx.bluetoothattendancetracker;

import com.parse.Parse;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ManjuRaghavendra on 10/28/2015.
 */
public class ClassParseObject implements Serializable {
    private String id, name, location, time, description, code;
    Boolean status;
    ParseObject obj;

    public ClassParseObject(String id, String name, String location, String time) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.time = time;
    }

    public ClassParseObject(ParseObject obj) {

        this.name = obj.getString("name");
        this.code = obj.getString("code");
        this.id = obj.getObjectId();
        this.status=obj.getBoolean("status");
        this.obj=obj;
        System.out.println("id" + id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public ParseObject getObj() {
        return obj;
    }

    public void setObj(ParseObject obj) {
        this.obj = obj;
    }
}
