package com.mad.tedx.bluetoothattendancetracker;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class StudentClassAdapter extends ArrayAdapter<StudentClass> {

    Context context;
    ArrayList<StudentClass> items;
    private BeaconManager beaconManager;
    private Region region;
    List<ParseObject> becaonsList;

    public StudentClassAdapter(Context context, ArrayList<StudentClass> result) {
        super(context, R.layout.class_lv, result);
        this.context = context;
        this.items = result;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.student_class_lv, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        Button giveAttendance = (Button) convertView.findViewById(R.id.button);


        final StudentClass item = items.get(position);
        Boolean status = item.getClassObj().getBoolean("status");
        if (status) {
            giveAttendance.setVisibility(View.VISIBLE);
        } else {
            giveAttendance.setVisibility(View.GONE);
        }
        name.setText(item.getClassName());
        giveAttendance.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AttendanceConstants.OBJECT = item.getClassObj();
                List<ParseObject> objects=getAttendanceObjects();
                for(ParseObject obj:objects)
                {
                    System.out.println("id:"+obj.getObjectId());
                }
                if (beaconManager != null) {
                    beaconManager.stopRanging(region);
                }
                getBeaconsList(objects);
            }
        });

        return convertView;

    }

    private void getBeaconsList(final List<ParseObject> objects) {
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Beacons");
        query.findInBackground(new FindCallback<ParseObject>() {
                                   @Override
                                   public void done(List<ParseObject> list, final ParseException e) {
                                       becaonsList = list;
                                       beaconManager = new BeaconManager(context);
                                       beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
                                           @Override
                                           public void onServiceReady() {

                                               region = new Region("ranged region", UUID.fromString((String) becaonsList.get(0).get("uuid")), null, null);

                                               beaconManager.startRanging(region);


                                           }
                                       });
                                       final Boolean status = false;
                                       beaconManager.setRangingListener(new BeaconManager.RangingListener() {
                                                                            @Override
                                                                            public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                                                                                if (list != null && !list.isEmpty()) {

                                                                                    System.out.println("Beacons Exists");
                                                                                    final List<Beacon> lis = list;
                                                                                    if (objects == null || objects.size() == 0) {
                                                                                        Boolean status=false;
                                                                                        for (Beacon b : lis) {
                                                                                            for (ParseObject obj : becaonsList) {
                                                                                                if (b.getMajor() == Integer.parseInt(obj.getString("major")) && b.getMinor() == Integer.parseInt(obj.getString("minor"))) {
                                                                                                    status=true;
                                                                                                    break;
                                                                                                }

                                                                                            }
                                                                                            if(status)
                                                                                            {
                                                                                                ParseObject obj1 = new ParseObject(AttendanceConstants.TABLE_NAME_STUDENT_ATTENDANCE);
                                                                                                obj1.put("class", AttendanceConstants.OBJECT);
                                                                                                obj1.put("student", ParseUser.getCurrentUser());
                                                                                                obj1.put("attend_status", true);
                                                                                                obj1.put("createdDate",new Date());

                                                                                                obj1.saveEventually(new SaveCallback() {
                                                                                                    @Override
                                                                                                    public void done(ParseException e) {
                                                                                                        if (e == null) {
                                                                                                            Toast.makeText(context, "Your Attendance has been taken", Toast.LENGTH_LONG).show();
                                                                                                            AttendanceConstants.OBJECT = null;
                                                                                                        } else
                                                                                                            Toast.makeText(context, "Error while taking attendance. Please contact your professor", Toast.LENGTH_LONG).show();
                                                                                                    }
                                                                                                });
                                                                                                beaconManager.stopRanging(region);
                                                                                            }
                                                                                        }
                                                                                    } else {

                                                                                        Toast.makeText(context, "Attendance has been already taken", Toast.LENGTH_LONG).show();
                                                                                    }
                                                                                    beaconManager.stopRanging(region);

                                                                                } else {
                                                                                    beaconManager.stopRanging(region);
                                                                                    Toast.makeText(context, "No Beacons Exists. Please check your bluetooth connection and try again.", Toast.LENGTH_LONG).show();
                                                                                }
                                                                            }

                                                                        }

                                       );
                                   }
                               }
        );


    }

    private List<ParseObject> getAttendanceObjects() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(AttendanceConstants.TABLE_NAME_STUDENT_ATTENDANCE);
        query.whereEqualTo("class",AttendanceConstants.OBJECT);
        query.whereEqualTo("student", ParseUser.getCurrentUser());
        Date midnight = new Date();
        midnight.setHours(0);
        midnight.setMinutes(0);
        midnight.setSeconds(0);
        System.out.println("date:" + midnight.toString());
        query.whereGreaterThan("createdDate", midnight);
        List<ParseObject> objects = null;
        try {
            objects = query.find();
        } catch (Exception e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return objects;
    }

}