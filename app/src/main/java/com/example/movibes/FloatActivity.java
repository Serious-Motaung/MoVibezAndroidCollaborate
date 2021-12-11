package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class FloatActivity extends AppCompatActivity {

    EditText etFloat1;
    Button btnFloat2;
    JSONObject response;
    boolean bUserExist = false;

    String sResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float);

        etFloat1 = findViewById(R.id.etFloat1);
        btnFloat2 = findViewById(R.id.btnFloat2);

        btnFloat2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sUsername = etFloat1.getText().toString().trim();
                if(CheckUserExistence(sUsername) == true)
                {
                    Toast.makeText(getApplicationContext(),"Exist",Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Not existing.",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private boolean CheckUserExistence(String sUsername)
    {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/CheckIfUserExist?_sUsername="+ sUsername;

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse( String s ) {
                try {
                    response = new JSONObject(s);
                    Log.e("Resp SUCCESS", "" + response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if(response.getBoolean("true"))
                    {
                        bUserExist = true;
                    }
                    else
                    {
                        bUserExist = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(),"" + s,Toast.LENGTH_SHORT).show();
                //bUserExist = Boolean.parseBoolean(s);
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
        return bUserExist;
    }
}