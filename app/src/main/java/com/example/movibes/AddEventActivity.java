package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class AddEventActivity extends AppCompatActivity {

    EditText etAddEventDescription, etAddEventLineUp, etAddEventEntranceFee, etAddEventDate, etAddEventNotes, etAddEventVenue,etAddEventTime;
    Button btnAddEventAdd;
    RadioGroup groupradio;
    ImageView ivAddEventProfilePic;
    public RadioButton rButton;
    public String str = "";
    int SELECT_PICTURE = 200;
    Bundle date_timeBundle;
    String _sStartDate,_sStartTime,_sEndDate,_sEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        date_timeBundle = getIntent().getExtras();

        String sVenue = "";
        etAddEventDescription = findViewById(R.id.etAddEventDescription);
        etAddEventLineUp = findViewById(R.id.etAddEventLineUp);
        etAddEventEntranceFee = findViewById(R.id.etAddEventEntranceFee);
        etAddEventDate = findViewById(R.id.etAddEventDate);
        etAddEventNotes = findViewById(R.id.etAddEventNotes);
        btnAddEventAdd = findViewById(R.id.btnAddEventAdd);
        etAddEventVenue = findViewById(R.id.etAddEventVenue);
        groupradio = findViewById(R.id.groupradio);
        ivAddEventProfilePic = findViewById(R.id.ivAddEventProfilePic);
        etAddEventTime = findViewById(R.id.etAddEventTime);

        String sDescription = etAddEventDescription.getText().toString();
        String sLineUp = etAddEventLineUp.getText().toString();
        String sEntranceFee = etAddEventLineUp.getText().toString();
        String sDate = etAddEventDate.getText().toString();
        String sNotes = etAddEventNotes.getText().toString();
        String sTime = etAddEventTime.getText().toString();

        isBundle(date_timeBundle);

        //ivAddEventProfilePic.getContext().getResources().getColor(R.color.black);
        ivAddEventProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        etAddEventEntranceFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomEntranceFeeDialog();
            }
        });

        etAddEventVenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomVenueDialog();
            }
        });

        etAddEventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( AddEventActivity.this,EventTimeActivity.class);
                startActivity(intent);
            }
        });

        btnAddEventAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAddEventDescription.getText().toString().trim().isEmpty()) {
                    etAddEventDescription.setError("Enter event description.");
                } else if (etAddEventLineUp.getText().toString().trim().isEmpty()) {
                    etAddEventLineUp.setError("Enter event lineup");
                } else if (etAddEventEntranceFee.getText().toString().trim().isEmpty()) {
                    etAddEventEntranceFee.setError("Enter event entrance fee.");
                } else if (etAddEventDate.getText().toString().trim().isEmpty()) {
                    etAddEventDate.setError("Enter event date");
                } else if (etAddEventTime.getText().toString().trim().isEmpty()) {
                    etAddEventTime.setError("Enter event times.");
                }else {
                    int selectedId = groupradio.getCheckedRadioButtonId();
                    rButton = (RadioButton) findViewById(selectedId);
                    if (rButton.getText().toString().equals("Existing")) {
                        //etAddEventVenue.setVisibility(View.GONE);
                        str = "Existing venue.";
                    }
                    if (rButton.getText().toString().equals("New")) {
                        //etAddEventVenue.setVisibility(View.VISIBLE);
                        str = etAddEventVenue.getText().toString();
                    }

                    String sFinalStart = _sStartDate + " " + _sStartTime;
                    String sFinalEnd = _sEndDate + " " + _sEndTime;
                    AddNewEvent(sFinalStart,
                            sFinalEnd,
                            etAddEventDescription.getText().toString(), str,
                            etAddEventNotes.getText().toString(), etAddEventLineUp.getText().toString(),
                            etAddEventEntranceFee.getText().toString(), "https://picsum.photos/id/1/5616/3744", 1);
                }
            }
        });

        groupradio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radio_new) {
                    etAddEventVenue.setVisibility(View.VISIBLE);

                } else if (checkedId == R.id.radio_existing) {
                    etAddEventVenue.setVisibility(View.GONE);
                }
            }
        });
    }

    //Function to display the custom dialog.
    private void showCustomEntranceFeeDialog() {
        final Dialog dialog = new Dialog(AddEventActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_entrance_dialog);

        //Initializing the views of the dialog.
        final EditText etGeneral = dialog.findViewById(R.id.name_et);
        final EditText etVIP = dialog.findViewById(R.id.age_et);
        final CheckBox termsCb = dialog.findViewById(R.id.terms_cb);
        Button submitButton = dialog.findViewById(R.id.submit_button);


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sGeneral = etGeneral.getText().toString();
                String sVIP = etVIP.getText().toString();
                Boolean hasAccepted = termsCb.isChecked();

                if (!hasAccepted) {
                    Toast.makeText(getApplicationContext(), "Please confirm entry fee(s).", Toast.LENGTH_LONG).show();
                } else {
                    if (MoVibesTools.isEditTextEmpty(etGeneral)) {
                        Toast.makeText(getApplicationContext(), "Enter at least general entry fee.", Toast.LENGTH_SHORT).show();
                    } else {
                        String sEntranceFee = "";
                        if (!MoVibesTools.isEditTextEmpty(etVIP)) {
                            sEntranceFee = "General: " + sGeneral + ", VIP: " + sVIP;
                            populateEntranceFeeField(sEntranceFee, true);
                            dialog.dismiss();
                        } else {
                            sEntranceFee = sGeneral;
                            populateEntranceFeeField(sEntranceFee, true);
                            dialog.dismiss();
                        }
                    }

                }
            }
        });

        dialog.show();
    }

    //Function to display the custom dialog.
    private void showCustomVenueDialog() {
        final Dialog dialog = new Dialog(AddEventActivity.this);
        //We have added a title in the custom layout. So let's disable the default title.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //The user will be able to cancel the dialog bu clicking anywhere outside the dialog.
        dialog.setCancelable(true);
        //Mention the name of the layout of your custom dialog.
        dialog.setContentView(R.layout.custom_venue_dialog);

        //Initializing the views of the dialog.
        final EditText etHouseNumber = dialog.findViewById(R.id.etOtpEmailCell);
        final EditText etAdd1 = dialog.findViewById(R.id.AddAddress1);
        final EditText etAdd2 = dialog.findViewById(R.id.AddAddress2);
        final EditText etAdd3 = dialog.findViewById(R.id.AddAddress3);
        final EditText etPostal = dialog.findViewById(R.id.AddPostal);
        final CheckBox cbAddTerms = dialog.findViewById(R.id.chAddTerms);
        Button btnAddSubmitButton = dialog.findViewById(R.id.AddSubmitButton);


        btnAddSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sHouseNumber = etHouseNumber.getText().toString();
                String sAddress1 = etAdd1.getText().toString();
                String sAddress2 = etAdd2.getText().toString();
                String sAddress3 = etAdd3.getText().toString();
                String sPostalCode = etPostal.getText().toString();
                Boolean hasAddAccepted = cbAddTerms.isChecked();

                if (!hasAddAccepted) {
                    Toast.makeText(getApplicationContext(), "Please verify the address.", Toast.LENGTH_LONG).show();
                } else {
                    String sVenue = sHouseNumber+", " + sAddress1 + ", " + sAddress2 + ", " + sAddress3+ ", " + sPostalCode;
                    populateVenueField(sVenue, true);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void populateEntranceFeeField(String general, Boolean hasAcceptedTerms) {
        String acceptedText = "have";
        if (!hasAcceptedTerms) {
            acceptedText = "have not";
        }
        etAddEventEntranceFee.setText(general);
    }

    private void populateVenueField(String venue, Boolean hasAcceptedTerms) {
        String acceptedText = "have";
        if (!hasAcceptedTerms) {
            acceptedText = "have not";
        }
        etAddEventVenue.setText(venue);
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout
                    ivAddEventProfilePic.setImageURI(selectedImageUri);
                }
            }
        }
    }

    public void AddNewEvent(String dtStartDate, String dtEndDate, String sDescription, String sVenue, String sNotes,
                            String sLineUp, String sEntranceFee, String sFlyer, int iProfileId) {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Events/AddEvent?_eventStartDateTime=" + dtStartDate +
                "&_eventEndDateTime=" + dtEndDate + "&_description=" + sDescription + "&_venue=" + sVenue +
                "&_notes=" + sNotes + "&_lineUp=" + sLineUp + "&_entranceFee=" + sEntranceFee + "&_imageUrl=" + sFlyer +
                "&_profileId=" + iProfileId;

        //Utils.enableProgressBar(progressBar);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        final RequestQueue requestQueue = Volley.newRequestQueue(AddEventActivity.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        //Log.d("Response", response);
                        //Toast.makeText(AddEventActivity.this,response,Toast.LENGTH_SHORT).show();
                        String sResult = "Your event is added.";
                        Toast.makeText(AddEventActivity.this, sResult, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(AddEventActivity.this,sResponse,Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        // error
                        if (volleyError instanceof TimeoutError || volleyError instanceof NoConnectionError) {
                            Toast.makeText(AddEventActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof AuthFailureError) {
                            Toast.makeText(AddEventActivity.this, "Authentication Failure", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ServerError) {
                            Toast.makeText(AddEventActivity.this, "" + volleyError.toString(), Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof NetworkError) {
                            Toast.makeText(AddEventActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        } else if (volleyError instanceof ParseError) {
                            Toast.makeText(AddEventActivity.this, "Parse Error", Toast.LENGTH_LONG).show();
                        }
                        requestQueue.stop();
                        progressDialog.dismiss();
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

    public void isBundle(Bundle bundle)
    {
        boolean isBundle = false;

        bundle = getIntent().getExtras();
        if(bundle != null)
        {
            _sStartTime = bundle.getString("StartTime");;
            //String sStartTime = bundle.getString("StartTime");
            _sStartDate = bundle.getString("StartDate");
            //String sStartDate = bundle.getString("StartDate");
            _sEndDate = bundle.getString("EndDate");
            //String sEndDate = bundle.getString("EndDate");
            _sEndTime = bundle.getString("EndTime");
            //String sEndTime = bundle.getString("EndTime");

            etAddEventDate.setText("Date : " + _sStartDate + " - " + _sEndDate);
            etAddEventTime.setText("Time : " + _sStartTime + " - " + _sEndTime);
        }
    }
}