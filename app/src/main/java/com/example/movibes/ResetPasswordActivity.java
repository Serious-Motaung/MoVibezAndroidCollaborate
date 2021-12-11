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
import java.util.Locale;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText etResetOTP, etResetPassword, etResetConfirmPassword;
    Button btnResetReset, btnResetCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etResetOTP = findViewById(R.id.etResetOTP);
        etResetPassword = findViewById(R.id.etResetPassword);
        etResetConfirmPassword = findViewById(R.id.etResetConfirmPassword);
        btnResetReset = findViewById(R.id.btnResetReset);
        btnResetCancel = findViewById(R.id.btnResetCancel);

        btnResetCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });
        btnResetReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sPassword = etResetPassword.getText().toString().trim();
                String sConfirm = etResetConfirmPassword.getText().toString().trim();

                if (etResetOTP.getText().toString().trim().isEmpty()) {
                    etResetOTP.setError("Please enter OTP number.");
                } else if (etResetPassword.getText().toString().trim().isEmpty()) {
                    etResetPassword.setError("Please enter password");
                } else if (etResetConfirmPassword.getText().toString().trim().isEmpty()) {
                    etResetConfirmPassword.setError("Please confirm password");
                }else if (sPassword.equals(sConfirm)) {
                    Bundle eventBundle = getIntent().getExtras();
                    String sUsername = eventBundle.getString("Username");
                    ResetPassword(etResetOTP.getText().toString().trim(),
                            sUsername,
                            etResetPassword.getText().toString().trim());
                    Intent loginIntent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                } else
                    {
                        Toast.makeText(ResetPasswordActivity.this,
                                "Your passwords do not match.",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void ResetPassword(String _OTP, String _username, String _password) {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/ResetPassword?_username=" + _username + "&_password=" + _password + "&_otp=" + _OTP;
        //Utils.enableProgressBar(progressBar);
        final RequestQueue requestQueue = Volley.newRequestQueue(ResetPasswordActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        //((RecyclerViewClickListener)ResetPasswordActivity.this).onSuccess(response);
                        Toast.makeText(ResetPasswordActivity.this, "Password was reset", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(ResetPasswordActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(ResetPasswordActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(ResetPasswordActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(ResetPasswordActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(ResetPasswordActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
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