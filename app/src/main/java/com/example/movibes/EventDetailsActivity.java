package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class EventDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        TextView tvVenue = findViewById(R.id.tvEventVenue);
        TextView tvDate = findViewById(R.id.tvEventDate);
        TextView tvEntranceFee = findViewById(R.id.tvEventEntranceFee);
        TextView tvDescription = findViewById(R.id.tvEventDescription);
        TextView tvLineUp = findViewById(R.id.tvEventLineUp);
        TextView tvNotes = findViewById(R.id.tvEventNotes);
        ImageView ivEventFlyer = findViewById(R.id.ivEventFlyer);
        Bundle eventBundle = getIntent().getExtras();

        String sVenue = eventBundle.getString("Venue");
        String sDate = eventBundle.getString("Date");
        String sEntranceFee = eventBundle.getString("EntranceFee");
        String sDescription = eventBundle.getString("Description");
        String sLineUp = eventBundle.getString("LineUp");
        String sNotes = eventBundle.getString("Notes");
        String sImageUrl = eventBundle.getString("ImageUrl");
        String _sStartDate = eventBundle.getString("StartDate");
        String _sEndDate = eventBundle.getString("EndDate");

        String _sRawStartTime = _sStartDate.substring(_sStartDate.indexOf("T")+1);
        _sRawStartTime.trim();

        String _sRawEndTime = _sEndDate.substring(_sEndDate.indexOf("T")+1);
        _sRawEndTime.trim();

        String sStartTime = GateTimeFromDate(_sRawStartTime);
        String sEndTime = GateTimeFromDate(_sRawEndTime);

        tvVenue.setText(sVenue+" >");
        tvDate.setText("Date: " + sDate + " @ " + sStartTime + " - " + sEndTime);
        tvEntranceFee.setText("Entrance Fee: " + sEntranceFee);
        tvDescription.setText(sDescription);
        tvLineUp.setText("Line-up: " + sLineUp);
        tvNotes.setText("Please note: " + sNotes);

        Glide.with(getApplicationContext())
                .load(sImageUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivEventFlyer);

        tvVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sAddress = "109 President Reitz Ave, Westdene, Bloemfontein, 9301";
                MoVibesTools.NavigateToEvent(getApplicationContext(),sAddress);
            }
        });
    }
    public String GateTimeFromDate(String sTime) {
        String sFinalTime="";
        if (sTime != null ) {
            sFinalTime = sTime.substring(0, sTime.length() - 3);
        }
        else
        {
            sFinalTime = "No Date";
        }
        return sFinalTime;
    }
}