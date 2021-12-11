package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private Context mContext;
    private Activity mActivity;
    EditText etRegisterUsername,etRegisterPassword,etRegisterConfirm;
    Button btnRegisterRegister,btnRegisterCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mContext = getApplicationContext();
        mActivity = RegisterActivity.this;

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirm = findViewById(R.id.etRegisterConfirmPassword);
        btnRegisterRegister = findViewById(R.id.btnRegisterRegister);
        btnRegisterCancel = findViewById(R.id.btnRegisterCancel);

        btnRegisterCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        btnRegisterRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _sUsername = etRegisterUsername.getText().toString().trim();
                String sPassword = etRegisterPassword.getText().toString().trim();
                String sConfirm = etRegisterConfirm.getText().toString().trim();

                if (etRegisterUsername.getText().toString().trim().isEmpty()) {
                    etRegisterUsername.setError("Please enter username.");
                } else if (etRegisterPassword.getText().toString().trim().isEmpty()) {
                    etRegisterPassword.setError("Please enter password");
                } else if (etRegisterConfirm.getText().toString().trim().isEmpty()) {
                    etRegisterConfirm.setError("Please confirm password");
                } else if (sPassword.equals(sConfirm)){

                    //Check existence???
                    String sResponse = MoVibesTools.ValidateCellphone(_sUsername);
                    Boolean isEmail = MoVibesTools.ValidateEmail(_sUsername);

                    if (!isEmail) {
                        if (sResponse == "Valid") {
                            String sUsername = _sUsername.replaceFirst("0", "+27");
                            //Toast.makeText(RegisterActivity.this,sUsername,Toast.LENGTH_SHORT).show();
                            Register(sUsername, sPassword);
                            sendOTP(sUsername);
                            Intent activateIntent = new Intent(RegisterActivity.this, ActivateActivity.class);
                            activateIntent.putExtra("Username", sUsername);
                            startActivity(activateIntent);
                        } else {
                            Toast.makeText(RegisterActivity.this, sResponse, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Register(_sUsername, sPassword);
                        sendOTP(_sUsername);
                        Intent activateIntent = new Intent(RegisterActivity.this, ActivateActivity.class);
                        activateIntent.putExtra("Username", _sUsername);
                        startActivity(activateIntent);
                    }
                } else
                {
                    Toast.makeText(RegisterActivity.this,
                            "Your passwords do not match.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void Register(String _username, String _password){

        int DEFAULT_TIMEOUT_MS = 10000 ;
        int DEFAULT_MAX_RETRIES = 3 ;

        String url ="http://movibez.af-south-1.elasticbeanstalk.com/Authentication/Register?_username="+_username+"&_password="+_password;
        //Utils.enableProgressBar(progressBar);

        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
        //RequestQueue requestQueue = VolleyManager.getInstance (context) .getRequestQueue ();
        //requestQueue.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,

                response ->
                {

                    //Utils.hideProgressDialog(progressBar);
                    //Utils.hideProgressDialog(progressBar);
                    //Utils.alertDialogShow(context, "Event Added.");
                    Toast.makeText(RegisterActivity.this, "User registered successfully.Please activate the account by providing OTP sent to you.", Toast.LENGTH_SHORT).show();
                    requestQueue.stop();

                },(VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            //Toast.makeText(RegisterActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(RegisterActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(RegisterActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(RegisterActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(RegisterActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
            }
            //requestQueue.stop();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        //Starts Request
        //requestQueue.add(stringRequest);
        //Set Volley timeout retry policy
        stringRequest.setRetryPolicy ( new DefaultRetryPolicy (
                0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//DEFAULT_TIMEOUT_MS,DEFAULT_MAX_RETRIES
        requestQueue.add (stringRequest);

        //Singleton.getInstance(mContext).addToRequestQueue(stringRequest);
    }
    private void sendOTP(String _sUsername)
    {
        String OTP_url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/GetOTP?_username="+_sUsername;

        final RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, OTP_url,

                response ->
                {

                    //Utils.hideProgressDialog(progressBar);
                    //Utils.hideProgressDialog(progressBar);
                    //Utils.alertDialogShow(context, "Event Added.");
                    Toast.makeText(RegisterActivity.this, "OTP Sent. Check mail.", Toast.LENGTH_SHORT).show();
                    requestQueue.stop();

                },(VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            //Toast.makeText(RegisterActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(RegisterActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(RegisterActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(RegisterActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(RegisterActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(RegisterActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
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
        stringRequest.setRetryPolicy ( new DefaultRetryPolicy (
                0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//DEFAULT_TIMEOUT_MS,DEFAULT_MAX_RETRIES
        requestQueue.add (stringRequest);
    }
}