package com.mad.tedx.bluetoothattendancetracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity {

    FloatingActionButton fab;
    TabLayout tabLayout;
    private BeaconManager beaconManager;
    private Region region;
    List<ParseObject> becaonsList;
    ParseQuery<ParseUser> userQuery;
    Map<String, ParseUser> usersMap;
    String userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupTablayout();
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

    private void setupTablayout() {

        ParseUser user = ParseUser.getCurrentUser();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setTabGravity(TabLayout.MODE_SCROLLABLE);
        userRole = user.getString("role");
        if (userRole.equals(AttendanceConstants.ROLE_PROFESSOR)) {
            tabLayout.addTab(tabLayout.newTab().setTag(AttendanceConstants.KEY_TAB_NAME_CLASS).setIcon(R.drawable.classimg).setText("Class"));
            tabLayout.addTab(tabLayout.newTab().setTag(AttendanceConstants.KEY_TAB_NAME_CLASS_ATTENDANCE).setIcon(R.drawable.classimg).setText("Attendance Report"));
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new ClassActivity(HomeActivity.this),
                            AttendanceConstants.KEY_TAB_NAME_CONNECTIONS).commit();
        } else {
            tabLayout.addTab(tabLayout.newTab().setTag(AttendanceConstants.KEY_TAB_NAME_STUDENT_CLASS).setIcon(R.drawable.classimg).setText("Class"));
            tabLayout.addTab(tabLayout.newTab().setTag(AttendanceConstants.KEY_TAB_NAME_PROFILE).setIcon(R.drawable.profile).setText("Profile"));
            //getBeaconsList();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new StudentClassActivity(HomeActivity.this),
                            AttendanceConstants.KEY_TAB_NAME_CONNECTIONS).commit();
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

           /*     Bundle bundle = new Bundle();

                bundle.put("con", HomeActivity.this);*/
                if (tab.getTag().equals(AttendanceConstants.KEY_TAB_NAME_CLASS)) {

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new ClassActivity(HomeActivity.this),
                                    AttendanceConstants.KEY_TAB_NAME_CLASS).commit();
                }
                else if (tab.getTag().equals(AttendanceConstants.KEY_TAB_NAME_STUDENT_CLASS)) {

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new StudentClassActivity(HomeActivity.this),
                                    AttendanceConstants.KEY_TAB_NAME_CLASS).commit();
                } else if (tab.getTag().equals(AttendanceConstants.KEY_TAB_NAME_CLASS_ATTENDANCE)) {

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new AttendanceActivity(HomeActivity.this),
                                    AttendanceConstants.KEY_TAB_NAME_CLASS_ATTENDANCE).commit();
                }else if (tab.getTag().equals(AttendanceConstants.KEY_TAB_NAME_PROFILE)) {

                    getFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new ProfileActivity(HomeActivity.this),
                                    AttendanceConstants.KEY_TAB_NAME_CLASS_ATTENDANCE).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (userRole.equals(AttendanceConstants.ROLE_PROFESSOR))
            getMenuInflater().inflate(R.menu.menu_home, menu);
        else
            getMenuInflater().inflate(R.menu.menu_student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            ParseUser.logOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.action_add_class) {
            Intent intent = new Intent(this, AddClassActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
