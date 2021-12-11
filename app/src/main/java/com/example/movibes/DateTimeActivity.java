package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class DateTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_time);

        //this.showDatePickerDialog();
        //this.showTimePickerDialog();
    }
    // Create and show a DatePickerDialog when click button.
    private void showDatePickerDialog()
    {
        // Get open DatePickerDialog button.
        Button datePickerDialogButton = (Button)findViewById(R.id.datePickerDialogButton);
        datePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnDateSetListener instance. This listener will be invoked when user click ok button in DatePickerDialog.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append("You select date is ");
                        strBuf.append(year);
                        strBuf.append("-");
                        strBuf.append(month+1);
                        strBuf.append("-");
                        strBuf.append(dayOfMonth);
                        //TextView datePickerValueTextView = (TextView)findViewById(R.id.datePickerValue);
                        //datePickerValueTextView.setText(strBuf.toString());
                    }
                };
                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);
                // Create the new DatePickerDialog instance.
                //DatePickerDialog datePickerDialog = new DatePickerDialog(DateTimeActivity.this, onDateSetListener, year, month, day);
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateTimeActivity.this, android.R.style.Theme_Holo_Dialog, onDateSetListener, year, month, day);
                // Set dialog icon and title.
                //datePickerDialog.setIcon(R.drawable.movibes_logo);
                datePickerDialog.setTitle("Please select date.");
                // Popup the dialog.
                datePickerDialog.show();
            }
        });
    }
    // Create and show a TimePickerDialog when click button.
    private void showTimePickerDialog()
    {
        // Get open TimePickerDialog button.
        Button timePickerDialogButton = (Button)findViewById(R.id.timePickerDialogButton);
        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
                        strBuf.append("You select time is ");
                        strBuf.append(hour);
                        strBuf.append(":");
                        strBuf.append(minute);
                        //TextView timePickerValueTextView = (TextView)findViewById(R.id.timePickerValue);
                        //timePickerValueTextView.setText(strBuf.toString());
                    }
                };
                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);
                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;
                //TimePickerDialog timePickerDialog = new TimePickerDialog(DateTimeActivity.this, onTimeSetListener, hour, minute, is24Hour);
                TimePickerDialog timePickerDialog = new TimePickerDialog(DateTimeActivity.this, android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);
                //timePickerDialog.setIcon(R.drawable.if_snowman);
                timePickerDialog.setTitle("Please select time.");
                timePickerDialog.show();
            }
        });
    }
}