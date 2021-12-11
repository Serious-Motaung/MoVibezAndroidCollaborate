package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        TextView tvEvent = findViewById(R.id.tvReviewEvent);
        TextView tvReviews = findViewById(R.id.tvReviewReviews);
        Button btnAddReview = findViewById(R.id.btnReviewAdd);
        EditText etEventReview = findViewById(R.id.etEventReview);

        Bundle eventBundle = getIntent().getExtras();

        String sEvent = eventBundle.getString("Description");
        String sReviews = eventBundle.getString("Reviews");
        String sEventId = eventBundle.getString("EventId");

        tvEvent.setText("Reviewing " + sEvent);

        int iReviews = Integer.parseInt(sReviews);
        String sWording = "";
        if(iReviews > 1)
        {
            sWording = "reviews.";
        }
        else
        {
            sWording = "review.";
        }
        tvReviews.setText("This event has " + sReviews + " " + sWording);

        //sEventReviews = etEventReview.getText().toString();

        int iEventId = Integer.parseInt(sEventId);
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sEventReview = etEventReview.getText().toString();
                AddReview(1,iEventId,sEventReview);
                //Toast.makeText(ReviewActivity.this,sEventReview,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void AddReview(int sProfileId,int sEventId,String sReview){
        //String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/Register/";
        //String url ="http://movibez.af-south-1.elasticbeanstalk.com/Authentication/Register?_username=poster&_password=poster";
        //String sProfileId = "",sEventId="",sReview="";
        String url ="http://movibez.af-south-1.elasticbeanstalk.com/Reviews/AddReview?_profileId="+sProfileId+"" +
                "&_eventId="+sEventId+"" +
                "&_review="+sReview;
        //Utils.enableProgressBar(progressBar);

        final RequestQueue requestQueue = Volley.newRequestQueue(ReviewActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                response ->
                {

                    //Utils.hideProgressDialog(progressBar);
                    //Utils.hideProgressDialog(progressBar);
                    //Utils.alertDialogShow(context, "Event Added.");
                    Toast.makeText(ReviewActivity.this, "Review added successfully.", Toast.LENGTH_SHORT).show();
                    //requestQueue.stop();

                },(VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            //Toast.makeText(RegisterActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(ReviewActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(ReviewActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(ReviewActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(ReviewActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(ReviewActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
            }
            requestQueue.stop();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        //Starts Request
        requestQueue.add(stringRequest);
    }
}