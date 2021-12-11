package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EstUserActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private RecyclerView mList;
    private RecyclerView.LayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Events> eventsList;
    private RecyclerView.Adapter adapter;
    private ImageView ivEstUserProfile;
    private TextView tvEstUserEstablishmentName,tvEstUserEstablishmentAddress;

    String ss = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_est_user);

        mList = findViewById(R.id.est_user_main_list);
        eventsList = new ArrayList<>();
        adapter = new EventEstUserAdapter(getApplicationContext(),eventsList,this);
        //adapter.notifyDataSetChanged();

        linearLayoutManager = new GridLayoutManager(this,3,GridLayoutManager.VERTICAL, false);
        //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //dividerItemDecoration = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        //mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        //mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        ivEstUserProfile = findViewById(R.id.ivDuaEstUserProfile);
        tvEstUserEstablishmentName = findViewById(R.id.tvDuaEstUserEstablishmentName);
        tvEstUserEstablishmentAddress = findViewById(R.id.tvDuaEstUserEstablishmentAddress);

        int iProfileId = 0;
        Bundle userestIntent = getIntent().getExtras();
        if (userestIntent != null) {
            iProfileId = Integer.parseInt(userestIntent.getString("ProfileID"));
            //The key argument here must match that used in the other activity
        }

        //int iProfileId = 2;
        ReadEventsFromAPI(iProfileId);
        GetProfileData(iProfileId);
    }
    private void ReadEventsFromAPI(int profileId)
    {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/GetEventByPostedDateProfileDescending?_profileId="+profileId;
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
        Intent requestLink = new Intent(EstUserActivity.this, EventDetailsActivity.class);
        requestLink.putExtras(eventBundle);
        startActivity(requestLink);
    }

    private void GetProfileData(int iUserId){
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/ProfileManagement/GetUserProfileByID?_userId="+iUserId;

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse( String s ) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject!=null){

                        String sProfilePic = jsonObject.getString("ImageUrl");
                        String sEstablishmentName = jsonObject.getString("EstablishmentName");
                        String sAddress = jsonObject.getString("Address");
                        String sName = jsonObject.getString("Name");
                        String sSurname = jsonObject.getString("Surname");
                        String sContactNumber = jsonObject.getString("Contact");

                        Glide.with(getApplicationContext()).load(sProfilePic).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivEstUserProfile);


                        tvEstUserEstablishmentName.setText(sEstablishmentName);

                        tvEstUserEstablishmentAddress.setText(sAddress);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Try again",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),""+e.toString(),Toast.LENGTH_LONG).show();
                }
            }
        }, (VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(getApplicationContext(), "No Connection", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(getApplicationContext(), "Sever Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(getApplicationContext(), "Parse Error", Toast.LENGTH_LONG).show();
            }

        });
        //Starts Request
        requestQueue.add(request);
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