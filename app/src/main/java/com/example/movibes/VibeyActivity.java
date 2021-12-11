package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class VibeyActivity extends AppCompatActivity implements RecyclerViewClickListener{

    private RecyclerView mList;
    private RecyclerView.LayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Events> eventsList;
    private RecyclerView.Adapter adapter;
    BottomNavigationView vibeyNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibey);

        mList = findViewById(R.id.vibey_main_list);
        eventsList = new ArrayList<>();
        adapter = new EventVibeyAdapter(getApplicationContext(),eventsList,this);
        vibeyNavigation = findViewById(R.id.vibeyNavigation);
        //adapter.notifyDataSetChanged();

        //linearLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL, false);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        linearLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });

        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //dividerItemDecoration = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        //mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        //mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        ReadEventsFromAPI();

        vibeyNavigation.setOnItemSelectedListener(item ->
        {
            // do stuff
            switch (item.getItemId()){

                case R.id.nav_home:
                    Intent homeIntent = new Intent(VibeyActivity.this,MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return true;

                case R.id.nav_profile:
                    Intent profileIntent = new Intent(VibeyActivity.this,EditProfileActivity.class);
                    startActivity(profileIntent);
                    finish();
                    return true;

                case R.id.nav_vibey:
                    //actionBar.setTitle("Vibey");
                    ReadEventsFromAPI();
                    return true;
            }
            return false;
        });
    }
    private void ReadEventsFromAPI()
    {
        //String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/GetVibeyEvents";
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/GetEventByPostedDateDescending";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Events event = new Events();
                        event.setDescription(jsonObject.getString("Description"));
                        event.setVenue(jsonObject.getString("Venue"));

                        //event.setDatePosted(jsonObject.getString("DatePosted"));
                        String _rawDate = jsonObject.getString("DatePosted");
                        String _sEventDate = _rawDate.substring(0,19);
                        String sEventDate = MoVibesTools.formateDateFromstring("yyyy-MM-dd'T'hh:mm:ss","dd/MM/YYYY",_sEventDate);
                        event.setDatePosted("Date Posted : " + sEventDate);
                        event.setImageUrl(jsonObject.getString("ImageUrl"));
                        event.setProfilePic(jsonObject.getString("ProfilePic"));
                        event.setEventVibes(jsonObject.getString("EventVibes"));
                        event.setEventReviews(jsonObject.getString("EventReviews"));
                        event.setEventStartDate(jsonObject.getString("EventStartDateTime"));
                        event.setEntranceFee(jsonObject.getString("EntranceFee"));
                        event.setLineUp(jsonObject.getString("LineUp"));
                        event.setNotes(jsonObject.getString("Notes"));
                        event.setEventReviews(jsonObject.getString("EventReviews"));
                        event.setEventID(jsonObject.getString("EventID"));
                        eventsList.add(event);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley",error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    public void LoadEventDetailsBundle(int position)
    {
        String sVenue,_sStartDate,sEntranceFee,sDescription,sLineUp,sNotes,sFlyer;

        //Set variables
        sVenue = eventsList.get(position).getVenue();
        _sStartDate = eventsList.get(position).getEventStartDate();
        sEntranceFee = eventsList.get(position).getEntranceFee();
        sDescription = eventsList.get(position).getDescription();
        sLineUp = eventsList.get(position).getLineUp();
        sNotes = eventsList.get(position).getNotes();
        sFlyer = eventsList.get(position).getImageUrl();

        Bundle eventBundle = new Bundle();
        eventBundle.putString("Venue",sVenue);

        //String date = jsonObject.getString("DatePosted");
        String rawStartDate = _sStartDate.substring(0,19);
        String sStartDate = MoVibesTools.formateDateFromstring("yyyy-MM-dd'T'hh:mm:ss","dd/MM/YYYY",rawStartDate);

        eventBundle.putString("Date",sStartDate);
        eventBundle.putString("EntranceFee",sEntranceFee);
        eventBundle.putString("Description",sDescription);
        eventBundle.putString("LineUp",sLineUp);
        eventBundle.putString("Notes",sNotes);
        eventBundle.putString("ImageUrl",sFlyer);
        //Toast.makeText(EstUserActivity.this,sFlyer,Toast.LENGTH_LONG).show();
        Intent requestLink = new Intent(VibeyActivity.this, EventDetailsActivity.class);
        requestLink.putExtras(eventBundle);
        startActivity(requestLink);
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onRowClicked(int position) {
        LoadEventDetailsBundle(position);
    }

    @Override
    public void onVibeClicked(View v, int position) {

    }

    @Override
    public void onReviewClicked(View review, int position) {

    }

    @Override
    public void onShareClicked(View review, int position) {

    }

    @Override
    public void onDeleteClicked(View review, int position) {

    }

    @Override
    public void onEditClicked(View review, int position) {

    }

    @Override
    public void onVenueClicked(int position) {

    }
}