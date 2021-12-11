package com.example.movibes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.Utils;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.util.EventLog;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements RecyclerViewClickListener {

    String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/GetEventByPostedDateDescending";
    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Events> eventsList;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    //private String sProfileId;

    private String sVibeResponse = "";

    //ActionBar actionBar;
    BottomNavigationView navigationView;
    private ShimmerFrameLayout mShimmerViewContainer;
    SessionManager sessionManager;
    String sProfileID,sRole;
    FloatingActionButton fbMainAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(getApplicationContext());
        fbMainAddEvent = findViewById(R.id.fbMainAddEvent);

        sProfileID = sessionManager.getProfileID();
        sRole = sessionManager.getUserRole();

        mList = findViewById(R.id.main_list);
        eventsList = new ArrayList<>();
        adapter = new EventAdapter(getApplicationContext(),eventsList,this);
        //adapter.notifyDataSetChanged();

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(),linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getSupportActionBar().hide();

        navigationView = findViewById(R.id.navigation);
        //navigationView.setOnItemSelectedListener(selectedListener);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        navigationView.setOnItemSelectedListener(item ->
        {
            // do stuff
            switch (item.getItemId()){

                case R.id.nav_home:
                    ReadEventsFromAPI();
                    return true;

                case R.id.nav_profile:

                    Intent profileIntent = new Intent(MainActivity.this,EditProfileActivity.class);
                    profileIntent.putExtra("UserRole",sRole);
                    startActivity(profileIntent);
                    finish();

                    return true;

                case R.id.nav_vibey:
                    //actionBar.setTitle("Vibey");
                    Intent vibeyIntent = new Intent(MainActivity.this,VibeyActivity.class);
                    startActivity(vibeyIntent);
                    finish();
                    return true;
            }
            return false;
        });
        fbMainAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddEventActivity.class));
                finish();
            }
        });

        ReadEventsFromAPI();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                eventsList.clear();
                ReadEventsFromAPI();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
    private void vibeManagement1(int profileId,int eventId){
        String url ="http://movibez.af-south-1.elasticbeanstalk.com/Vibes/VibesManagement?_profileId="+profileId+"&_eventId="+eventId;
        //Utils.enableProgressBar(progressBar);
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        ((RecyclerViewClickListener)MainActivity.this).onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(MainActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(MainActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(MainActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(MainActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                        }
                        requestQueue.stop();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        requestQueue.add(postRequest);
    }

    private String vibeManagement(int profileId,int eventId){
        String url ="http://movibez.af-south-1.elasticbeanstalk.com/Vibes/VibesManagement?_profileId="+profileId+"&_eventId="+eventId;
        //Utils.enableProgressBar(progressBar);
        String sResults = "";
        final RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                response ->
                {

                    //Utils.hideProgressDialog(progressBar);
                    //Utils.hideProgressDialog(progressBar);
                    //Utils.alertDialogShow(context, "Event Added.");

                    Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_SHORT).show();
                    //requestQueue.stop();

                },(VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            //Toast.makeText(RegisterActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(MainActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(MainActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(MainActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(MainActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
            }
            requestQueue.stop();
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        //Starts Request
        requestQueue.add(stringRequest);
        //adapter.notifyItemChanged(1);
        //adapter.notifyDataSetChanged();
        return sResults;
    }
    private void ReadEventsFromAPI()
    {
        //final ProgressDialog progressDialog = new ProgressDialog(this);
        //progressDialog.setMessage("Loading...");
        //progressDialog.show();

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
                        event.setEventStartDate("EventStartDateTime");
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
                        event.setProfileID(jsonObject.getString("ProfileID"));
                        event.setEventEndDate(jsonObject.getString("EventEndDateTime"));
                        eventsList.add(event);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //progressDialog.dismiss();
                    }
                }
                adapter.notifyDataSetChanged();
                //progressDialog.dismiss();
                mList.setVisibility(View.VISIBLE);
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley",error.toString());
                //progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onSuccess(String result) {
        sVibeResponse = result;
    }

    @Override
    public void onRowClicked(int position) {
        LoadEventDetailsBundle(position);
    }

    @Override
    public void onVibeClicked(View v, int position) {
        int iProfileId = Integer.parseInt(sProfileID);
        int iEventId = Integer.parseInt(eventsList.get(position).getEventID());
        int iEventVibes = Integer.parseInt(eventsList.get(position).getEventVibes());
        String sFinalVibes="";
        vibeManagement1(iProfileId,iEventId);



        eventsList.get(position).setEventVibes(String.valueOf(iEventVibes));
        adapter.notifyItemChanged(position);

        //UpdateClickedEventObject(position);
        Toast.makeText(MainActivity.this, sVibeResponse.toString(), Toast.LENGTH_SHORT).show();
        eventsList.get(position).setEventVibes(String.valueOf(iEventVibes--));
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onReviewClicked(View review, int position)
    {
        LoadEventReviewBundle(position);
    }

    @Override
    public void onShareClicked(View review, int position)
    {
        String sMessageContent="Hi there, I am attending " + eventsList.get(position).getDescription() +
                " at " + eventsList.get(position).getVenue() + ". The event start at " + eventsList.get(position).getEventStartDate() +
                ". Get more vibey events using MoVibes App. Available on iOS and Android.";
        ShareEventDetails(sMessageContent);
    }

    @Override
    public void onDeleteClicked(View review, int position) {

    }

    @Override
    public void onEditClicked(View review, int position) {

    }

    @Override
    public void onVenueClicked(int position) {
        String sProfileId = eventsList.get(position).getProfileID();
        Intent userestIntent = new Intent(MainActivity.this, EstUserActivity.class);
        userestIntent.putExtra("ProfileID",sProfileId);
        startActivity(userestIntent);
        //Toast.makeText(MainActivity.this,sProfileId,Toast.LENGTH_SHORT).show();
    }

    public void LoadEventDetailsBundle(int position)
    {
        String sVenue,_sStartDate,sEntranceFee,sDescription,sLineUp,sNotes,sFlyer,sStartTime,sEndTime;

        //Set variables
        sVenue = eventsList.get(position).getVenue();
        _sStartDate = eventsList.get(position).getEventStartDate();
        sEntranceFee = eventsList.get(position).getEntranceFee();
        sDescription = eventsList.get(position).getDescription();
        sLineUp = eventsList.get(position).getLineUp();
        sNotes = eventsList.get(position).getNotes();
        sFlyer = eventsList.get(position).getImageUrl();
        sStartTime = eventsList.get(position).getEventStartDate();
        sEndTime = eventsList.get(position).getEventEndDate();


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
        eventBundle.putString("StartDate",sStartTime);
        eventBundle.putString("EndDate",sEndTime);

        Intent requestLink = new Intent(MainActivity.this, EventDetailsActivity.class);
        //Bundle bun = new Bundle();
        //bun.putString("Link",sendLink);
        //bun.putString("Search", addressInput);

        requestLink.putExtras(eventBundle);
        startActivity(requestLink);
    }
    public void LoadEventReviewBundle(int position)
    {
        String sEventId,sReviews,sDescription;

        //Set variables
        sEventId = eventsList.get(position).getEventID();
        sReviews = eventsList.get(position).getEventReviews();
        sDescription = eventsList.get(position).getDescription();

        Bundle eventBundle = new Bundle();
        eventBundle.putString("EventId",sEventId);
        eventBundle.putString("Reviews",sReviews);
        eventBundle.putString("Description",sDescription);

        Intent requestLink = new Intent(MainActivity.this, ReviewActivity.class);

        requestLink.putExtras(eventBundle);
        startActivity(requestLink);
    }
    public void ShareEventDetails(String sContent)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, sContent);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
    public void UpdateClickedEventObject(int position)
    {
        eventsList.get(position);
        adapter.notifyItemChanged(position);
    }
}