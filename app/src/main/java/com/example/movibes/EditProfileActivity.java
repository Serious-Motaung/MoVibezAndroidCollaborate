package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    ImageView ivProfileImagePic,ivProfileExit;
    TextView tvProfileEstablishment,tvProfileAddress,tvProfileOwnerName,tvProfileContact,tvProfileOwnerSurname;
    EditText etProfileEstablishment,etProfileAddress,etProfileOwnerName,etProfileContact,etProfileOwnerSurname;
    Button btnUpdate1,btnUpdate2,btnCancel;
    BottomNavigationView navProfileNavigation;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        sessionManager = new SessionManager(getApplicationContext());
        String sUserRole = sessionManager.getUserRole();
        Toast.makeText(EditProfileActivity.this,sUserRole,Toast.LENGTH_SHORT).show();
        if(sUserRole.equals("1")) {
            Intent profileIntent = new Intent(EditProfileActivity.this, DUAdminActivity.class);
            startActivity(profileIntent);
            finish();
        }

        navProfileNavigation = findViewById(R.id.navProfileNavigation);

        ivProfileExit = findViewById(R.id.ivProfileExit);
        ivProfileImagePic = findViewById(R.id.ivProfileImagePic);
        tvProfileEstablishment = findViewById(R.id.tvProfileEstablishment);
        tvProfileAddress = findViewById(R.id.tvProfileAddress);
        tvProfileOwnerName = findViewById(R.id.tvProfileOwnerName);
        tvProfileContact = findViewById(R.id.tvProfileContact);
        tvProfileOwnerSurname = findViewById(R.id.tvProfileOwnerSurname);

        etProfileEstablishment = findViewById(R.id.etProfileEstablishment);
        etProfileAddress = findViewById(R.id.etProfileAddress);
        etProfileOwnerName = findViewById(R.id.etProfileOwnerName);
        etProfileContact = findViewById(R.id.etProfileContact);
        etProfileOwnerSurname = findViewById(R.id.etProfileOwnerSurname);

        btnUpdate1 = findViewById(R.id.btnUpdate1);
        btnUpdate2 = findViewById(R.id.btnUpdate2);
        btnCancel = findViewById(R.id.btnCancel);

        GetProfileData(1);

        ivProfileExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoVibesTools.Logout(getApplicationContext(),sessionManager);
                startActivity(new Intent(EditProfileActivity.this,LoginActivity.class));
                finish();
            }
        });
        navProfileNavigation.setOnItemSelectedListener(item ->
        {
            switch (item.getItemId()){

                case R.id.nav_home:
                    Intent homeIntent = new Intent(EditProfileActivity.this,MainActivity.class);
                    startActivity(homeIntent);
                    finish();
                    return true;

                case R.id.nav_profile:
                    GetProfileData(1);
                    return true;

                case R.id.nav_vibey:
                    Intent vibeyIntent = new Intent(EditProfileActivity.this,VibeyActivity.class);
                    startActivity(vibeyIntent);
                    finish();
                    return true;
            }
            return false;
        });

        btnUpdate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideTextViews();
            }
        });
        btnUpdate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iRole = 1;

                if(iRole == 1)
                {
                    UpdateProfile(etProfileOwnerName.getText().toString()
                            ,etProfileOwnerSurname.getText().toString()
                    ,etProfileEstablishment.getText().toString()
                    ,etProfileAddress.getText().toString()
                    ,etProfileContact.getText().toString()
                    ,-1,"Interests","https://picsum.photos/id/10/2500/1667",1,1);
                    //GetProfileData(1);
                    HideEditText();
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HideEditText();
            }
        });

    }
    private void GetProfileData(int iUserId){

        //Utils.enableProgressBar(progressBar);
        //String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/UserLogin?_username="+username+"&_password="+password;
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/ProfileManagement/GetUserProfileByID?_userId="+iUserId;
        //String url = LOGIN_USER_URL+"username="+username+"&&password="+password;
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse( String s ) {
                //Utils.hideProgressDialog(progressBar);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if(jsonObject!=null){

                        String sProfilePic = jsonObject.getString("ImageUrl");
                        String sEstablishmentName = jsonObject.getString("EstablishmentName");
                        String sAddress = jsonObject.getString("Address");
                        String sName = jsonObject.getString("Name");
                        String sSurname = jsonObject.getString("Surname");
                        String sContactNumber = jsonObject.getString("Contact");

                        Glide.with(getApplicationContext()).load(sProfilePic).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivProfileImagePic);

                        tvProfileEstablishment.setText(sEstablishmentName);
                        etProfileEstablishment.setText(sEstablishmentName);

                        tvProfileOwnerName.setText(sName);
                        etProfileOwnerName.setText(sName);

                        tvProfileOwnerSurname.setText(sSurname);
                        etProfileOwnerSurname.setText(sSurname);

                        tvProfileContact.setText(sContactNumber);
                        etProfileContact.setText(sContactNumber);

                        tvProfileAddress.setText(sAddress);
                        etProfileAddress.setText(sAddress);
                        //JSONObject user = jsonObject.getJSONObject("User");
                        //int userID = user.getInt("Id");
                        //Toast.makeText(getApplicationContext(),""+sProfilePic,Toast.LENGTH_LONG).show();

                        //Intent login = new Intent(context, MainActivity.class);
                        //Bundle bundle = new Bundle();
                        //bundle.putString("userRole", userRole);
                        //bundle.putInt("userID", userID);

                        //login.putExtras(bundle);
                        //startActivity(login);
                    }
                    else {
                        //Utils.alertDialogShow(context, "Try again");
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

    private void HideTextViews()
    {
        tvProfileEstablishment.setVisibility(View.GONE);
        tvProfileAddress.setVisibility(View.GONE);
        tvProfileOwnerName.setVisibility(View.GONE);
        tvProfileContact.setVisibility(View.GONE);
        tvProfileOwnerSurname.setVisibility(View.GONE);
        btnUpdate1.setVisibility(View.GONE);

        etProfileEstablishment.setVisibility(View.VISIBLE);
        etProfileAddress.setVisibility(View.VISIBLE);
        etProfileOwnerName.setVisibility(View.VISIBLE);
        etProfileContact.setVisibility(View.VISIBLE);
        etProfileOwnerSurname.setVisibility(View.VISIBLE);
        btnUpdate2.setVisibility(View.VISIBLE);
    }

    private void HideEditText()
    {
        tvProfileEstablishment.setVisibility(View.VISIBLE);
        tvProfileAddress.setVisibility(View.VISIBLE);
        tvProfileOwnerName.setVisibility(View.VISIBLE);
        tvProfileContact.setVisibility(View.VISIBLE);
        tvProfileOwnerSurname.setVisibility(View.VISIBLE);
        btnUpdate1.setVisibility(View.VISIBLE);

        etProfileEstablishment.setVisibility(View.GONE);
        etProfileAddress.setVisibility(View.GONE);
        etProfileOwnerName.setVisibility(View.GONE);
        etProfileContact.setVisibility(View.GONE);
        etProfileOwnerSurname.setVisibility(View.GONE);
        btnUpdate2.setVisibility(View.GONE);
    }

    private void UpdateProfile(String _sName,String _sSurname,String _sEstablishment,String _sAddress,
                               String _sContact,int _iGender,String _sInterests,String _sProfilePic,
                               int _iRole,int _iUserId)
    {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/ProfileManagement/UpdateProfile?_name="+_sName+"&_surname="+_sSurname+"" +
                "&_establishmentName="+_sEstablishment+"&_address="+_sAddress+"&_contact="+_sContact+
                "&_gender="+_iGender+"&_interests="+_sInterests+"&_imageUrl="+_sProfilePic+"" +
                "&_role="+_iRole+"&_userId="+_iUserId;

        //Utils.enableProgressBar(progressBar);
        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.PUT, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        Toast.makeText(getApplicationContext(), "Profile updated.", Toast.LENGTH_SHORT).show();
                        GetProfileData(1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), ""+volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "Parse Error", Toast.LENGTH_LONG).show();
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