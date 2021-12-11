package com.example.movibes;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MoVibesTools {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate){

        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
        }

        return outputDate;

    }

    public static void NavigateToEvent(Context context,String sAddress)
    {
        String uri = String.format(Locale.ENGLISH, "geo:0,0?q="+sAddress);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        context.startActivity(intent);
    }

    public static boolean isEditTextEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public static void DatePicker(Context context, EditText editText, String dialogTitle)
    {
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                StringBuffer strBuf = new StringBuffer();
                String sFinalMonth = "",sFinalDay = "";
                if(dayOfMonth < 10)
                {
                    sFinalDay = "0" + dayOfMonth;
                }
                else
                {
                    sFinalDay = String.valueOf(dayOfMonth);
                }

                if((month + 1) < 10)
                {
                    sFinalMonth = "0" + String.valueOf(month + 1);
                }
                else
                {
                    sFinalMonth = String.valueOf(month + 1);
                }
                String sDate = String.valueOf(sFinalDay)+"/"+String.valueOf(sFinalMonth)+"/"+String.valueOf(year);
                //TextView datePickerValueTextView = (TextView)findViewById(R.id.datePickerValue);
                //datePickerValueTextView.setText(strBuf.toString());
                editText.setText(sDate);
            }
        };
        // Get current year, month and day.
        Calendar now = Calendar.getInstance();
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);
        // Create the new DatePickerDialog instance.
        //DatePickerDialog datePickerDialog = new DatePickerDialog(DateTimeActivity.this, onDateSetListener, year, month, day);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, onDateSetListener, year, month, day);
        // Set dialog icon and title.
        //datePickerDialog.setIcon(R.drawable.movibes_logo);
        datePickerDialog.setTitle(dialogTitle);
        // Popup the dialog.
        datePickerDialog.show();
    }

    public static void TimePicker(Context context, EditText editText, String dialogTitle)
    {
        // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                StringBuffer strBuf = new StringBuffer();
                //strBuf.append("You select time is ");
                strBuf.append(hour);
                strBuf.append(":");
                strBuf.append(minute);

                String sHour = "",sMinute = "", sTime = "";
                if(hour < 10)
                {
                    sHour = "0" + hour;
                }
                else
                {
                    sHour = String.valueOf(hour);
                }

                if(minute < 10)
                {
                    sMinute = "0" + minute;
                }
                else
                {
                    sMinute = String.valueOf(minute);
                }
                sTime = sHour + ":" + sMinute;
                editText.setText(sTime);
            }
        };
        Calendar now = Calendar.getInstance();
        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = now.get(java.util.Calendar.MINUTE);
        // Whether show time in 24 hour format or not.
        boolean is24Hour = true;
        //TimePickerDialog timePickerDialog = new TimePickerDialog(DateTimeActivity.this, onTimeSetListener, hour, minute, is24Hour);
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, android.R.style.Theme_Holo_Light_Dialog, onTimeSetListener, hour, minute, is24Hour);
        //timePickerDialog.setIcon(R.drawable.if_snowman);
        timePickerDialog.setTitle(dialogTitle);
        timePickerDialog.show();
    }

    public static String ValidateCellphone(String sCellphone)
    {
        String sResponse = "";
        //Accepts numbers only.
        String regNumbers = "^[0-9]*$";
        if (sCellphone.matches(regNumbers))
        {
            //Accepts 10 digits only.
            String regNumber10 = "^[0-9]{10}$";
            //Check if 1st character is 0.
            String sFistLetter = String.valueOf(sCellphone.charAt(0));
            if(sCellphone.startsWith("0") && sCellphone.matches(regNumber10))
            {
                sResponse = "Valid";
            }
            else
            {
                sResponse = "Enter 10 digits cellphone number in a format 0601234567.";
            }
        }
        else
        {
            sResponse = "Enter a valid cellphone number or a valid email address.";
        }
        return sResponse;
    }

    public static boolean ValidateEmail(String emailStr)
    {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static void CheckEmptyEditText(EditText editText,String sError)
    {
        String sText = editText.getText().toString().trim();

        if(sText.equals(""))
        {
            editText.setError(sError);
        }
    }

    public static void Logout(Context context, SessionManager sessionManager)
    {
        sessionManager = new SessionManager(context);

        sessionManager.setLogin(false);
        sessionManager.setProfileID("");
        sessionManager.setUserRole("");
        sessionManager.setUserProfilePic("");
        sessionManager.setUserEstablishment("");
        sessionManager.setUserName("");
        sessionManager.setUserAddress("");
        sessionManager.setUserContact("");
        sessionManager.setUserId("");
        sessionManager.setUserGender("");
        sessionManager.setUserInterests("");
    }

}
