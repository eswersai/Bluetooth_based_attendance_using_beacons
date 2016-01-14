package com.mad.tedx.bluetoothattendancetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class StudentListAdapter extends ArrayAdapter<StudentAttendanceA> {

	Context context;
	ArrayList<StudentAttendanceA> items;

	public StudentListAdapter(Context context, ArrayList<StudentAttendanceA> result) {
		super(context, R.layout.attendance_lv, result);
		this.context = context;
		this.items = result;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.attendance_lv, parent, false);
		}
		TextView name = (TextView) convertView.findViewById(R.id.textView1);
		TextView status = (TextView) convertView.findViewById(R.id.textView2);

		StudentAttendanceA item = items.get(position);
		name.setText(item.getName());
		status.setText(item.getClassName());
		return convertView;

	}
}
