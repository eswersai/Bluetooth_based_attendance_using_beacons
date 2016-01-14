package com.mad.tedx.bluetoothattendancetracker;

import com.parse.ParseObject;

/**
 * Created by ManjuRaghavendra on 10/19/2015.
 */
public class AttendanceConstants {

    //Table Names
    public final static String TABLE_NAME_CONFERENCE_SPEAKERS="Conference_Speakers";
    public final static String TABLE_NAME_BEACONS="Beacons";
    public final static String TABLE_NAME_CLASS ="Classes";
    public final static String TABLE_NAME_CLASS_STUDENTS ="Class_Students";
    public final static String TABLE_NAME_STUDENT_ATTENDANCE ="Student_Attendance";
    public final static String TABLE_NAME_USER_CONFERENCES="User_Conferences";
    public final static String TABLE_NAME_USER_SPEAKER="User_Speakers";



    public static final String KEY_TAB_NAME_CLASS = "classes";
    public static final String KEY_TAB_NAME_PROFILE = "profile";
    public static final String KEY_TAB_NAME_STUDENT_CLASS = "student_classes";
    public static final String KEY_TAB_NAME_CONNECTIONS = "connections";
    public static final String KEY_TAB_NAME_CHAT = "chat";
    public static final String KEY_TAB_NAME_CONFERENCE = "conference";
    public static final String KEY_TAB_NAME_CLASS_ATTENDANCE = "Attendance";
    public static final String KEY_TAB_NAME_SETTINGS = "settings";
    public static final String KEY_TAB_NAME_QUESTION = "questions";
    public static final String KEY_TAB_NAME = "tab";
    public static final String ROLE_USER="ROLE_USER";
    public static final String ROLE_PROFESSOR="ROLE_PROFESSOR";
    public static final Integer IMAGE_SIZE=100;
    public static ParseObject OBJECT=null;
}
