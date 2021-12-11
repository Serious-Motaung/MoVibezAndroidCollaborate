package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import java.util.Calendar;

public class EventTimeActivity extends AppCompatActivity {

    EditText et_StartDate,et_StartTime,et_EndDate,et_EndTime;
    ImageView iv_StartDate,iv_StartTime,iv_EndDate,iv_EndTime;
    Button btn_TimeAdd,btn_TimeCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_time);

        et_StartDate = findViewById(R.id.et_StartDate);
        et_StartTime = findViewById(R.id.et_StartTime);
        et_EndDate = findViewById(R.id.et_EndDate);
        et_EndTime = findViewById(R.id.et_EndTime);

        iv_StartDate = findViewById(R.id.iv_StartDate);
        iv_StartTime = findViewById(R.id.iv_StartTime);
        iv_EndDate = findViewById(R.id.iv_EndDate);
        iv_EndTime = findViewById(R.id.iv_EndTime);

        btn_TimeAdd = findViewById(R.id.btn_TimeAdd);
        btn_TimeCancel = findViewById(R.id.btn_TimeCancel);

        iv_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoVibesTools.DatePicker(EventTimeActivity.this,et_StartDate,"Event Start Date");
            }
        });

        iv_StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoVibesTools.TimePicker(EventTimeActivity.this,et_StartTime,"Event Start Time");
            }
        });

        iv_EndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoVibesTools.DatePicker(EventTimeActivity.this,et_EndDate,"Event End Date");
            }
        });

        iv_EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoVibesTools.TimePicker(EventTimeActivity.this,et_EndTime,"Event End Time");
            }
        });

        btn_TimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sStartDate = et_StartDate.getText().toString().trim();
                String sStartTime = et_StartTime.getText().toString().trim();
                String sEndDate = et_EndDate.getText().toString().trim();
                String sEndTime = et_EndTime.getText().toString().trim();

                Bundle date_timeBundle = new Bundle();
                date_timeBundle.putString("StartTime",sStartTime);
                date_timeBundle.putString("StartDate",sStartDate);
                date_timeBundle.putString("EndDate",sEndDate);
                date_timeBundle.putString("EndTime",sEndTime);

                Intent requestLink = new Intent(EventTimeActivity.this, AddEventActivity.class);
                requestLink.putExtras(date_timeBundle);
                startActivity(requestLink);
                finish();
            }
        });

        btn_TimeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventTimeActivity.this, AddEventActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}