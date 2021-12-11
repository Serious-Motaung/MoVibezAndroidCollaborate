package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.Utils;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Boolean isValid = false;
    SessionManager sessionManager;
    TextInputLayout etPassword;
    EditText etUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLoginLogin);
        TextView tvForgotPassword = findViewById(R.id.tvLoginForgotPassword);
        TextView tvSignUp = findViewById(R.id.tvLoginSignUp);
        sessionManager = new SessionManager(getApplicationContext());

        if(sessionManager.getLogin())
        {
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String _sUsername = etUsername.getText().toString().trim();

                if (etUsername.getText().toString().trim().isEmpty()) {
                    etUsername.setError("Please enter username.");
                } else if (etPassword.getEditText().getText().toString().trim().isEmpty()) {
                    etPassword.setError("Please enter password");
                } else {
                    String sResponse = MoVibesTools.ValidateCellphone(_sUsername);
                    Boolean isEmail = MoVibesTools.ValidateEmail(_sUsername);

                    if (!isEmail) {
                        if (sResponse == "Valid") {
                            String sUsername = _sUsername.replaceFirst("0", "+27");
//
//                        if(CheckUserExistence(sUsername))
//                        {
                            loginUser(sUsername, etPassword.getEditText().getText().toString().trim());
//                        }
//                        else
//                        {
//                            String sCellResponse  = "Username " + sUsername + " does not exist. Create a new account";
//                            Toast.makeText(LoginActivity.this,sCellResponse,Toast.LENGTH_SHORT).show();
//                        }

                        } else {
                            Toast.makeText(LoginActivity.this, sResponse, Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                    if(CheckUserExistence(_sUsername))
//                    {
                        loginUser(_sUsername, etPassword.getEditText().getText().toString().trim());

//                    }
//                    else
//                    {
//                        String sEmailResponse  = "Username " + _sUsername + " does not exist. Create a new account";
//                        Toast.makeText(LoginActivity.this,sEmailResponse,Toast.LENGTH_SHORT).show();
//                    }
                    }
                    //loginUser(etUsername.getText().toString(), etPassword.getText().toString());
                    //loginUser("ntsima","ntsima");
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(signUp);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomOTPDialog();
            }
        });
    }

    private void loginUser(String username, String password)
    {

        //Utils.enableProgressBar(progressBar);

        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/UserLogin?_username=" + username + "&_password=" + password;
        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Utils.hideProgressDialog(progressBar);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject != null) {

                        String sProfileId = jsonObject.getString("ProfileID");
                        String sName = jsonObject.getString("Name");
                        String sSurname = jsonObject.getString("Surname");
                        String sEstablishmentName = jsonObject.getString("EstablishmentName");
                        String sAddress = jsonObject.getString("Address");
                        String sContact = jsonObject.getString("Contact");
                        String sGender = jsonObject.getString("Gender");
                        String sInterests = jsonObject.getString("Interests");
                        String sProfilePic = jsonObject.getString("ImageUrl");
                        String sRole = jsonObject.getString("Role");
                        String sUserId = jsonObject.getString("UserID");

                        sessionManager.setLogin(true);
                        sessionManager.setProfileID(sProfileId);
                        sessionManager.setUserRole(sRole);
                        sessionManager.setUserProfilePic(sProfilePic);
                        sessionManager.setUserEstablishment(sEstablishmentName);
                        sessionManager.setUserName(sName + " " + sSurname);
                        sessionManager.setUserAddress(sAddress);
                        sessionManager.setUserContact(sContact);
                        sessionManager.setUserId(sUserId);
                        sessionManager.setUserGender(sGender);
                        sessionManager.setUserInterests(sInterests);

                        Intent login = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(login);
                        finish();
                    } else {
                        //Utils.alertDialogShow(context, "Try again");
                        Toast.makeText(LoginActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    //Utils.hideProgressDialog(progressBar);
                }
            }
        }, (VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(LoginActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(LoginActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(LoginActivity.this, "Sever Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(LoginActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
            }

        });
        //Starts Request
        requestQueue.add(request);
    }

    private void showCustomOTPDialog()
    {
        final Dialog dialog = new Dialog(LoginActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_otp_dialog);

        //Initializing the views of the dialog.
        final EditText etEmailOTP = dialog.findViewById(R.id.etOtpEmailCell);
        Button btnOtpRequest = dialog.findViewById(R.id.btnOtpRequest);
        Button btnOtpCancel = dialog.findViewById(R.id.btnOtpCancel);


        btnOtpRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String _sUsername = etEmailOTP.getText().toString();

                if (etEmailOTP.getText().toString().trim().isEmpty()) {
                    etEmailOTP.setError( "Enter username.");
                } else {
                    String sResponse = MoVibesTools.ValidateCellphone(_sUsername);
                    Boolean isEmail = MoVibesTools.ValidateEmail(_sUsername);

                    if(!isEmail)
                    {
                        if(sResponse == "Valid")
                        {
                            String sUsername = _sUsername.replaceFirst("0", "+27");

//                            if(CheckUserExistence(sUsername))
//                            {
//                                String sCellResponse  = "Username " + sUsername + " does not exist. Create a new account";
//                                Toast.makeText(LoginActivity.this,sCellResponse,Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                            {
                                sendOTP(sUsername);
                                dialog.dismiss();
                            Intent signUp = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                            signUp.putExtra("Username",sUsername);
                            startActivity(signUp);
//                            }

                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,sResponse,Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
//                        if(CheckUserExistence(_sUsername))
//                        {
//                            String sEmailResponse  = "Username " + _sUsername + " does not exist. Create a new account";
//                            Toast.makeText(LoginActivity.this,sEmailResponse,Toast.LENGTH_SHORT).show();
//                        }
//                        else
//                        {
                            sendOTP(_sUsername);
                            dialog.dismiss();
                        Intent signUp = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                        signUp.putExtra("Username",_sUsername);
                        startActivity(signUp);
//                        }
                    }
                }
            }
        });

        btnOtpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void sendOTP(String _sUsername)
    {
        String OTP_url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/GetOTP?_username="+_sUsername;

        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, OTP_url,

                response ->
                {

                    //Utils.hideProgressDialog(progressBar);
                    //Utils.hideProgressDialog(progressBar);
                    //Utils.alertDialogShow(context, "Event Added.");
                    Toast.makeText(LoginActivity.this, "OTP Sent. Check mail.", Toast.LENGTH_SHORT).show();
                    requestQueue.stop();

                },(VolleyError volleyError) -> {
            //Utils.hideProgressDialog(progressBar);
            //Toast.makeText(RegisterActivity.this, "No Connection", Toast.LENGTH_LONG).show();
            if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                Toast.makeText(LoginActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(LoginActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(LoginActivity.this, ""+volleyError.toString(), Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof NetworkError) {
                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
            } else if (volleyError instanceof ParseError) {
                Toast.makeText(LoginActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
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

    private boolean CheckUserExistence(String sUsername)
    {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/CheckIfUserExist?_sUsername="+ sUsername;

        final RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse( String s ) {
                isValid = Boolean.parseBoolean(s);
            }
        }, volleyError -> {
            try {
                String responseBody = new String( volleyError.networkResponse.data, "utf-8" );
                JSONObject jsonObject = new JSONObject( responseBody );
            } catch ( JSONException e ) {
                //Handle a malformed json response
            } catch (UnsupportedEncodingException error){

            }
        });
        requestQueue.add(request);
        return isValid;
    }
}