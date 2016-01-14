package com.mad.tedx.bluetoothattendancetracker;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddClassActivity extends AppCompatActivity {

    EditText className, classCode, classTime;
    Button addClass;
    TextView adddaysTv, daysListTv;
    List<Integer> selectedItems;
    CharSequence cs[]={"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        loadAllViews();
        addClass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseObject obj = new ParseObject(AttendanceConstants.TABLE_NAME_CLASS);
                obj.put("name", className.getText().toString());
                obj.put("code", classCode.getText().toString());
                String time = classTime.getText().toString();
                String[] timeArr = time.split(":");
                obj.put("hour", timeArr[0]);
                obj.put("min", timeArr[1]);
                obj.put("email",ParseUser.getCurrentUser().getEmail());
                if (selectedItems.contains(0)) {
                    obj.put("Monday", true);
                } else {
                    obj.put("Monday", false);
                }
                if (selectedItems.contains(1)) {
                    obj.put("Tuesday", true);
                } else {
                    obj.put("Tuesday", false);
                }
                if (selectedItems.contains(2)) {
                    obj.put("Wednesday", true);
                } else {
                    obj.put("Wednesday", false);
                }
                if (selectedItems.contains(3)) {
                    obj.put("Thursday", true);
                } else {
                    obj.put("Thursday", false);
                }
                if (selectedItems.contains(4)) {
                    obj.put("Friday", true);
                } else {
                    obj.put("Friday", false);
                }
                obj.put("status",true);
                obj.put("professor",ParseUser.getCurrentUser().getEmail());
                obj.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(AddClassActivity.this, "ClassParseObject Details Saved Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        } else {
                            Toast.makeText(AddClassActivity.this, "Error while saving ClassParseObject Details", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
        selectedItems = new ArrayList<>();

        adddaysTv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddClassActivity.this);
                Boolean status[] = new Boolean[5];
                for (Integer ob : selectedItems) {
                    status[ob] = true;
                }
                builder.setMultiChoiceItems(cs, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked)
                            selectedItems.add(which);
                        else
                            selectedItems.remove(which);
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String days = "";
                        Integer i = 0;
                        for (Integer in : selectedItems) {
                            if (i == 0) {
                                i++;
                            } else {
                                days += ",";
                            }
                            days += cs[in];
                        }
                        daysListTv.setText(days);

                    }
                });
                builder.show();
            }
        });
        classTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        classTime.setText("" + hourOfDay + ":" + minute);
                    }
                };

                mTimePicker = new TimePickerDialog(AddClassActivity.this, listener, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
    }

    private void loadAllViews() {
        className = (EditText) findViewById(R.id.editText1);
        classCode = (EditText) findViewById(R.id.editText2);
        classTime = (EditText) findViewById(R.id.editText3);
        addClass = (Button) findViewById(R.id.addClass);
        daysListTv = (TextView) findViewById(R.id.textView1);
        adddaysTv = (TextView) findViewById(R.id.textView2);
    }


}
