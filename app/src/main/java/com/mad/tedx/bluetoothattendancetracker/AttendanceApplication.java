package com.mad.tedx.bluetoothattendancetracker;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * File Name: TedxApplication.java
 * Team Members:  Manju Raghavendra Bellamkonda
 */
public class AttendanceApplication extends Application {

    private BeaconManager beaconManager;
    List<ParseObject> becaonsList;
    ParseQuery<ParseUser> userQuery;
    Map<String, ParseUser> usersMap;

    private Region region;

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "iZEV1joQ1quywEui5q8u9i4IOspZMbMYlpzVVTwq", "ZxkcdfDJvk3MuJeuLz8UJVNLls3atQuQsg1FakSW");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //addBeacons();
        //monitorBeacons();

        userQuery = ParseUser.getQuery();
        usersMap = new HashMap<String, ParseUser>();
        try {
            List<ParseUser> userList = userQuery.find();
            for (ParseUser userObj : userList) {
                usersMap.put(userObj.getObjectId(), userObj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //getBeaconsList();
    }


    private void getBeaconsList() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Beacons");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                becaonsList = list;
                beaconManager = new BeaconManager(getApplicationContext());
                beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                    @Override
                    public void onServiceReady() {

                        region = new Region("ranged region", UUID.fromString((String) becaonsList.get(0).get("uuid")), null, null);

                        beaconManager.startMonitoring(region);


                    }
                });
                beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                    @Override
                    public void onEnteredRegion(Region region, List<Beacon> list) {
                        //showNotification("Alert", "The Speaker you have followed is near you. Please look around. ");

                        for (ParseObject obj : becaonsList) {
                            System.out.println("Object mjor:" + obj.getString("major") + "Beacon Major:" + list.get(0).getMajor());
                            if (obj.getString("major").equals(list.get(0).getMajor())) {
                                ParseUser user = ParseUser.getCurrentUser();
                                user.put("location", obj);
                                user.saveEventually();
                                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_USER_SPEAKER);
                                query.whereEqualTo("User", ParseUser.getCurrentUser());
                                try {
                                    List<ParseObject> speakerList = query.find();
                                    for (ParseObject lis : speakerList) {
                                        ParseUser speaker = (ParseUser) lis.get("speaker");
                                        speaker = usersMap.get(speaker.getObjectId());
                                        if (speaker.get("location") != null && speaker.get("location").equals(user.get("location"))) {
                                            showNotification("Alert", "The Speaker you have followed is near you. Please look around. ");
                                        }
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }


                    }

                    @Override
                    public void onExitedRegion(Region region) {
                        showNotification(
                                "Thank you",
                                "Thank you for attending the conference");
                    }
                });
                beaconManager.setRangingListener(new BeaconManager.RangingListener() {


                    @Override
                    public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                        //showNotification("Beacon Listener", "Becon communication is working");

                        if (list != null && !list.isEmpty()) {

                            System.out.println("Beacons Exists");
                            final List<Beacon> lis = list;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {

                                @Override
                                public void run() {
                                    //getMessagesForBeacon(lis.get(0).getMajor());

                                }
                            }, 10000);

                        }
                    }

                });
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, HomeActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }

    public void monitorBeacons() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Beacons");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> list, ParseException e) {
                becaonsList = list;
                beaconManager = new BeaconManager(getApplicationContext());
                beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                    @Override
                    public void onServiceReady() {
                        if (list != null) {
                            region = new Region("ranged region", UUID.fromString((String) becaonsList.get(0).get("uuid")), null, null);

                            beaconManager.startMonitoring(region);
                        }

                    }
                });
                beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
                    @Override
                    public void onEnteredRegion(Region region, List<Beacon> list) {
                        showNotification(
                                "Welcome Message",
                                "Welcome to the Conference");
                    }

                    @Override
                    public void onExitedRegion(Region region) {
                        showNotification(
                                "Thank you",
                                "Thank you for attending the conference");
                    }
                });
            }
        });
    }


    private void addBeacons() {
        ParseObject obj = new ParseObject("Beacons");

        //My Beacons
        obj = new ParseObject("Beacons");
        obj.put("uuid", "B9407F30-F5F8-466E-AFF9-25556B57FE6D");
        obj.put("major", "16709");
        obj.put("minor", "27702");
        obj.put("purpose", "Homework");
        obj.put("region", "");
        obj.put("location", "Test1");
        obj.saveEventually();

        obj = new ParseObject("Beacons");
        obj.put("uuid", "B9407F30-F5F8-466E-AFF9-25556B57FE6D");
        obj.put("major", "17390");
        obj.put("minor", "14174");
        obj.put("purpose", "Homework");
        obj.put("region", "");
        obj.put("location", "Test2");
        obj.saveEventually();

        obj = new ParseObject("Beacons");
        obj.put("uuid", "B9407F30-F5F8-466E-AFF9-25556B57FE6D");
        obj.put("major", "1250");
        obj.put("minor", "32204");
        obj.put("purpose", "Homework");
        obj.put("region", "");
        obj.put("location", "Test3");
        obj.saveEventually();
    }
}
