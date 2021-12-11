package com.example.movibes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class ToggleActivity extends AppCompatActivity {

    boolean isValid = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);

        TextInputEditText tiedPassword = findViewById(R.id.tiedPassword);

        EditText etSample = findViewById(R.id.etSample);

        //TextInputLayout rr = findViewById(R.id.etPassword99);

        Button button3 = findViewById(R.id.btnTT);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean myBool = CheckUserExistence(etSample.getText().toString().trim());
                Toast.makeText(ToggleActivity.this,""+myBool,Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validate = false;
    private boolean CheckUserExistence(String sUsername)
    {
        String url = "http://movibez.af-south-1.elasticbeanstalk.com/Authentication/CheckIfUserExist?_sUsername="+ sUsername;

        final RequestQueue requestQueue = Volley.newRequestQueue(ToggleActivity.this);
        StringRequest request = new StringRequest( Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse( String s ) {
                validate = Boolean.parseBoolean(s);
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
        return validate;
    }
}