package com.example.movibes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DUAdminActivity extends AppCompatActivity implements RecyclerViewClickListener {
    String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/GetEventByPostedDateDescending";
    private RecyclerView mList;
    private LinearLayoutManager linearLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<Events> eventsList;
    private RecyclerView.Adapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String sVibeResponse = "";

    //ActionBar actionBar;
    BottomNavigationView navigationView;
    Button btnDuaEditProf,btnDuaLogout;
    SessionManager sessionManager;
    TextView tvDuaProfileEstablishment,tvDuaProfileOwnerName,tvDuaProfileContact,tvDuaProfileAddress;
    ImageView ivDuaEstUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duadmin);

        mList = findViewById(R.id.du_main_list);
        eventsList = new ArrayList<>();
        adapter = new AdminEventAdapter(getApplicationContext(), eventsList, this);
        //adapter.notifyDataSetChanged();
        btnDuaLogout = findViewById(R.id.btnDuaLogout);
        btnDuaEditProf = findViewById(R.id.btnDuaEditProf);
        sessionManager = new SessionManager(getApplicationContext());
        tvDuaProfileEstablishment = findViewById(R.id.tvDuaEstUserEstablishmentName);
        tvDuaProfileOwnerName =findViewById(R.id.tvDuaOwner);
        tvDuaProfileContact = findViewById(R.id.tvDuaContact);
        tvDuaProfileAddress = findViewById(R.id.tvDuaEstUserEstablishmentAddress);
        ivDuaEstUserProfile = findViewById(R.id.ivDuaEstUserProfile);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());
        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        getSupportActionBar().hide();

        Glide.with(getApplicationContext()).load(sessionManager.getUserProfilePic()).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivDuaEstUserProfile);

        btnDuaLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoVibesTools.Logout(getApplicationContext(),sessionManager);
                startActivity(new Intent(DUAdminActivity.this,LoginActivity.class));
                finish();
            }
        });
        btnDuaEditProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DUAdminActivity.this,EditProfileActivity.class));
            }
        });

        navigationView = findViewById(R.id.dunavigation);
        //navigationView.setOnItemSelectedListener(selectedListener);

        navigationView.setOnItemSelectedListener(item ->
        {
            // do stuff
            switch (item.getItemId()) {

                case R.id.nav_home:
                    Intent homeIntent = new Intent(DUAdminActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return true;

                case R.id.nav_profile:
                    Intent profileIntent = new Intent(DUAdminActivity.this, EditProfileActivity.class);
                    startActivity(profileIntent);
                    finish();
                    return true;

                case R.id.nav_vibey:
                    Intent vibeyIntent = new Intent(DUAdminActivity.this, VibeyActivity.class);
                    startActivity(vibeyIntent);
                    finish();
                    return true;
            }
            return false;
        });

        ReadEventsFromAPI();
    }
    private void ReadEventsFromAPI() {
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
                        event.setImageUrl(jsonObject.getString("ImageUrl"));
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
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onSuccess(String result) {

    }

    @Override
    public void onRowClicked(int position) {

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
        int iEventID = Integer.parseInt(eventsList.get(position).getEventID());
        String sEvent = eventsList.get(position).getDescription();

        AlertDialog.Builder alert = new AlertDialog.Builder(DUAdminActivity.this);
        alert.setTitle("Delete the event");
        alert.setMessage("Are you sure you want to delete "+sEvent.toUpperCase(Locale.ROOT)+"?");
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                DeleteEvent(iEventID);
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // close dialog
                dialog.cancel();
            }
        });
        alert.show();
    }

    @Override
    public void onEditClicked(View review, int position) {

    }

    @Override
    public void onVenueClicked(int position) {

    }

    private void DeleteEvent(int _iEventId) {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/DeleteEvent?_eventId="+_iEventId;

        //Utils.enableProgressBar(progressBar);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), "Event deleted.", Toast.LENGTH_SHORT).show();
                        eventsList.clear();
                        ReadEventsFromAPI();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Parse Error", Toast.LENGTH_LONG).show();
                        }
                        requestQueue.stop();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };
        requestQueue.add(postRequest);
    }
}