package com.example.companies_json;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class MainActivity extends AppCompatActivity {

    private EditText companyText;
    private TextView responseText;
    private TextView nameText;
    private ProgressBar progressBar;
    private ImageView logoImageView;

    //THIS IS AILEEN'S API KEY
    private static final String API_KEY = "8ea9fa44fe430665";

    //FOR SOME REASON, MY API KEY DOESN'T WORK
    //private static final String API_KEY = "FvHGTQIGopnKJxenESUjgATS9mmKIDu1";

    private static final String API_URL = "https://api.fullcontact.com/v2/company/search.json?";
    private RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        companyText = findViewById(R.id.companyText);
        responseText = findViewById(R.id.responseText);
        nameText = findViewById(R.id.nameText);
        progressBar = findViewById(R.id.progressBar);
        logoImageView = findViewById(R.id.imageView);

        Button queryButton = findViewById(R.id.queryButton);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = companyText.getText().toString();
                requestData(name);
            }
        });
    }

    private void requestData(String name) {
        progressBar.setVisibility(View.VISIBLE);
        responseText.setText("");
        nameText.setText("");
        logoImageView.setImageResource(android.R.color.transparent);

        queue = Volley.newRequestQueue(this);

        //create URL for request
        String url = API_URL + "companyName=" + name + "&apiKey=" + API_KEY;

        //Request a string response from the provided URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseJSON(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage(), error);
            }
        });

        //Add the request to the RequestQueue
        queue.add(stringRequest);
    }

    private void parseJSON(String response) {

        //response should be a String with JSON objects
        if (response == null) {
            response = "THERE WAS AN ERROR";
        }
        progressBar.setVisibility(View.GONE);

        //parse JSON array
        try {
            //parse JSON for companyName
            JSONArray array = (JSONArray) new JSONTokener(response).nextValue();
            JSONObject object = array.getJSONObject(0);
            String companyName = object.getString("orgName");
            nameText.setText(companyName);

            //parse JSON for imageURL
            String imageURL = object.getString("logo");
            requestImage(imageURL);

            //parse JSON for address
            JSONObject location = object.getJSONObject("location");
            String locality = location.getString("locality");
            JSONObject region = location.getJSONObject("region");
            String regionName = region.getString("name");
            JSONObject country = location.getJSONObject("country");
            String countryName = country.getString("name");
            responseText.append("City: " + locality + "\n" +
                    "State: " + regionName + "\n" +
                    "Country: " + countryName + "\n");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestImage(String imageURL){
        // Initialize a new ImageRequest
        ImageRequest imageRequest = new ImageRequest(imageURL, // Image URL
                new Response.Listener<Bitmap>() { // Bitmap listener
                    @Override
                    public void onResponse(Bitmap response) {
                        // Do something with response
                        Log.d("IMAGE", "in onResponse");
                        logoImageView.setImageBitmap(response);
                    }
                },
                0,
                0,
                null,
                null,
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", error.getMessage(), error);
                    }
                }
        );

        // Add ImageRequest to the RequestQueue
        queue.add(imageRequest);
    }
}
