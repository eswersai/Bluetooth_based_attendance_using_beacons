package com.mad.tedx.bluetoothattendancetracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassAdapter extends ArrayAdapter<ClassParseObject> {

    Context context;
    ArrayList<ClassParseObject> items;

    public ClassAdapter(Context context, ArrayList<ClassParseObject> result) {
        super(context, R.layout.class_lv, result);
        this.context = context;
        this.items = result;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.class_lv, parent, false);
        }
        final ClassParseObject item = items.get(position);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        TextView code = (TextView) convertView.findViewById(R.id.textView2);
        final ImageButton addOrRemoveStudent = (ImageButton) convertView.findViewById(R.id.imageButton);
        ImageButton viewStudents = (ImageButton) convertView.findViewById(R.id.imageButton2);
        final ImageButton takeAttendance = (ImageButton) convertView.findViewById(R.id.imageButton3);
        addOrRemoveStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String classId = items.get(position).getId();
                Toast.makeText(context, "String is :" + classId, Toast.LENGTH_LONG).show();
                Map<String, ParseUser> selectedUsers = new HashMap<String, ParseUser>();
                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                userQuery.addAscendingOrder("lastName");
                userQuery.whereEqualTo("role", AttendanceConstants.ROLE_USER);
                final Map<String, ParseUser> finalSelectedUsers = selectedUsers;
                userQuery.findInBackground(new FindCallback<ParseUser>() {

                    @Override
                    public void done(List<ParseUser> arg0, ParseException arg1) {
                        final Map<String, ParseObject> users = new HashMap<String, ParseObject>();

                        boolean[] checkStatus = new boolean[arg0.size()];
                        final AlertDialog.Builder builder = new AlertDialog.Builder(
                                context);
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        String currentUserlastName = currentUser
                                .getString("firstName")
                                + " "
                                + currentUser.getString("lastName");
                        List<String> nameList = new ArrayList<String>();
                        int i = 0;
                        for (ParseUser user : arg0) {
                            boolean status = true;
                            String lastName = user.getString("firstName") + " "
                                    + user.getString("lastName");


                            nameList.add(lastName);
                            users.put(lastName, user);

                            if (finalSelectedUsers.containsKey(lastName)) {
                                checkStatus[i] = true;
                            } else {
                                checkStatus[i] = false;
                            }
                        }
                        final CharSequence[] cs = nameList
                                .toArray(new CharSequence[users.size()]);


                        builder.setMultiChoiceItems(cs, checkStatus, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                final ParseUser userObj = (ParseUser) users
                                        .get(cs[which]);
                                String userlastName = userObj
                                        .getString("firstName")
                                        + " "
                                        + userObj.getString("lastName");
                                if (isChecked)
                                    finalSelectedUsers.put(userlastName, userObj);
                                else
                                    finalSelectedUsers.remove(userlastName);

                            }
                        });

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (finalSelectedUsers != null) {
                                    for (ParseUser user : finalSelectedUsers.values()) {
                                        if(!checkClassStudentStatus(user,classId)) {
                                            ParseObject classStudentObj = new ParseObject(AttendanceConstants.TABLE_NAME_CLASS_STUDENTS);
                                            classStudentObj.put("classId", classId);
                                            classStudentObj.put("student", user);
                                            classStudentObj.put("studentId", user.getObjectId());
                                            classStudentObj.saveEventually();
                                        }
                                    }
                                }
                            }
                        });
                        builder.setTitle("Users:");
                        builder.show();

                    }
                });
            }
        });
        resizeImage(addOrRemoveStudent, R.drawable.add);
        viewStudents.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StudentListActivity.class);
                intent.putExtra("classId", items.get(position).getId());
                context.startActivity(intent);
            }
        });
        resizeImage(viewStudents, R.drawable.view);
        takeAttendance.setOnClickListener(new View.OnClickListener() {

                                              @Override
                                              public void onClick(View v) {
                                                  if (item.getStatus()) {
                                                      item.setStatus(false);
                                                  } else {
                                                      item.setStatus(true);
                                                  }
                                                  ParseObject obj = item.getObj();
                                                  obj.put("status", item.getStatus());
                                                  obj.saveEventually();
                                                  System.out.println("Status:" + item.getStatus());
                                                  if (item.getStatus()) {
                                                      takeAttendance.setImageResource(R.drawable.stop);
                                                  } else {
                                                      takeAttendance.setImageResource(R.drawable.start);
                                                  }
                                              }
                                          }

        );

        if (item.getStatus()) {
            resizeImage(takeAttendance, R.drawable.stop);
        } else {
            resizeImage(takeAttendance, R.drawable.start);
        }


        name.setText(item.getName());
        code.setText(item.getCode());
        return convertView;

    }

    private boolean checkClassStudentStatus(ParseUser user, String classId) {
        boolean status=false;
        ParseQuery<ParseObject> query=new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_CLASS_STUDENTS);
        query.whereEqualTo("classId",classId);
        query.whereEqualTo("studentId",user.getObjectId());
        try {
            List<ParseObject> objects=query.find();
            if(objects!=null && objects.size()>0)
            {
                status=true;
            }
        }catch(Exception e)
        {
            status=false;
            e.printStackTrace();
        }

        return status;
    }

    private void resizeImage(ImageButton takeAttendance, int start) {
        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), start);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, AttendanceConstants.IMAGE_SIZE, AttendanceConstants.IMAGE_SIZE, true);
        takeAttendance.setImageBitmap(bMapScaled);

    }

    private Map<String, ParseUser> getStudentsListInMap(String classId) {

        Map<String, ParseUser> selectedUsers = new HashMap<String, ParseUser>();
        Map<String, ParseUser> selectedUsers1 = new HashMap<String, ParseUser>();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AttendanceConstants.TABLE_NAME_CLASS_STUDENTS);
        query.whereEqualTo("classId", classId);
        try {
            List<ParseObject> objects = query.find();

            ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
            userQuery.addAscendingOrder("lastName");
            userQuery.whereEqualTo("role", AttendanceConstants.ROLE_USER);
            List<ParseUser> usersList = userQuery.find();

            for (ParseUser currentUser : usersList) {

                selectedUsers.put(currentUser.getObjectId(), currentUser);
            }
            System.out.println(selectedUsers.toString());
            for (ParseObject obj : objects) {
                ParseUser user = selectedUsers.get(obj.get("studentId"));
                String currentUserlastName = user
                        .getString("firstName")
                        + " "
                        + user.getString("lastName");
                selectedUsers1.put(user.getObjectId(), user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return selectedUsers1;
    }


}
