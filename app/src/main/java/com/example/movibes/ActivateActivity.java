package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ActivateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);

        EditText etActivateOTP = findViewById(R.id.etActivateOTP);
        Button btnActivateActivate = findViewById(R.id.btnActivateActivate);
        Button btnActivateCancel = findViewById(R.id.btnActivateCancel);

        btnActivateActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle eventBundle = getIntent().getExtras();

                String sUsername = eventBundle.getString("Username");
                VerifyAccount(etActivateOTP.getText().toString().trim(),sUsername);
            }
        });

        btnActivateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(ActivateActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
    private void VerifyAccount(String _OTP,String _username)
    {
        String url ="http://movibez.af-south-1.elasticbeanstalk.com/Authentication/VerifyAccount?_username="+_username+"&_OTP="+_OTP;
        //Utils.enableProgressBar(progressBar);
        final RequestQueue requestQueue = Volley.newRequestQueue(ActivateActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        //((RecyclerViewClickListener)ResetPasswordActivity.this).onSuccess(response);
                        Toast.makeText(ActivateActivity.this,"Account is activated.",Toast.LENGTH_SHORT).show();
                        Intent registerIntent = new Intent(ActivateActivity.this,RegisterActivity.class);
                        startActivity(registerIntent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(ActivateActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(ActivateActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(ActivateActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(ActivateActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(ActivateActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
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
}