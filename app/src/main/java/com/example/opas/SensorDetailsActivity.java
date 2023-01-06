package com.example.opas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import java.util.Objects;

public class SensorDetailsActivity extends AppCompatActivity {
    TextView nameEditText, zoneEditText;
    ImageButton editSensorButton, deleteSensorButton;
    ImageView iconImageView,refreshImageView;
    String name, zone, id, type, docId;


    private RequestQueue requestQueue;
    private Gson gson;
    private static final String urlSingleValue = "https://mdl.frederick.ac.cy/cyriciotwebapis/api/Data/GetCardWidgetData";
    private static final String bearerTokenSingleValue = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhY3NjNDIzQGZyZWRlcmljay5hYy5jeSIsImp0aSI6IjVhMTVhMzdhLTdiMGMtNDY5Mi1hNzMzLTlkNjI4YTljZjJiOCIsImVtYWlsIjoiYWNzYzQyM0BmcmVkZXJpY2suYWMuY3kiLCJ1aWQiOiIwODk1MDQ2NS01YmZlLTRjZjYtYjQwNS1mM2YyNjNjY2YxNDkiLCJyb2xlcyI6IkN1c3RvbWVyIiwiZXhwIjoxNzAyMzY5Njk3LCJpc3MiOiJTZWN1cmVBcGkiLCJhdWQiOiJTZWN1cmVBcGlVc2VyIn0.kn_u0mZflNxTmb2Gn5moyZCF_gmQatzIsE1wLOpbM_w";

    private static final String urlRangeValues= "https://mdl.frederick.ac.cy/cyriciotwebapis/api/Data/GetLineChartSingleSensorData";
    private static final String bearerRangeValues = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhY3NjNDIzQGZyZWRlcmljay5hYy5jeSIsImp0aSI6IjVhMTVhMzdhLTdiMGMtNDY5Mi1hNzMzLTlkNjI4YTljZjJiOCIsImVtYWlsIjoiYWNzYzQyM0BmcmVkZXJpY2suYWMuY3kiLCJ1aWQiOiIwODk1MDQ2NS01YmZlLTRjZjYtYjQwNS1mM2YyNjNjY2YxNDkiLCJyb2xlcyI6IkN1c3RvbWVyIiwiZXhwIjoxNzAyMzY5Njk3LCJpc3MiOiJTZWN1cmVBcGkiLCJhdWQiOiJTZWN1cmVBcGlVc2VyIn0.kn_u0mZflNxTmb2Gn5moyZCF_gmQatzIsE1wLOpbM_w";


    private TextView resultSingleValue,resultRangeValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);

        resultSingleValue = findViewById(R.id.live_data_text_view);
        resultRangeValues = findViewById(R.id.range_data_text_view);

        requestQueue = (RequestQueue) Volley.newRequestQueue(this);
        refreshImageView = findViewById(R.id.refresh);

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSingleSensorData(Integer.parseInt(id),urlSingleValue,bearerTokenSingleValue);
                Utility.showToast(SensorDetailsActivity.this, "Data Refreshed");

            }
        });

        id = getIntent().getStringExtra("id");
        getSingleSensorData(Integer.parseInt(id),urlSingleValue,bearerTokenSingleValue);

        getRangeSensorData(96,urlRangeValues,bearerRangeValues,"2022-12-12","2022-12-12");

        nameEditText = findViewById(R.id.sensor_name_text_view);
        zoneEditText = findViewById(R.id.sensor_zone_text_view);
        iconImageView = findViewById(R.id.live_data_icon);
        editSensorButton = findViewById(R.id.edit_sensor_btn);
        deleteSensorButton = findViewById(R.id.delete_sensor_btn);

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        type = getIntent().getStringExtra("type");
        docId = getIntent().getStringExtra("docId");

        nameEditText.setText(name);
        zoneEditText.setText(zone);

        if (Objects.equals(type, "Activity")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            iconImageView.setColorFilter(Color.argb(255, 0, 204, 102));
        } else if (Objects.equals(type, "Temperature")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
            iconImageView.setColorFilter(Color.argb(255, 255, 128, 0));
        } else if (Objects.equals(type, "Humidity")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_water_drop_24);
            iconImageView.setColorFilter(Color.argb(255, 51, 153, 255));
        } else if (Objects.equals(type, "CO2")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_co2_24);
            iconImageView.setColorFilter(Color.argb(255, 128, 128, 128));
        }

        deleteSensorButton.setOnClickListener((v) -> deleteSensor());

        editSensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SensorDetailsActivity.this, NewSensorActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("zone", zone);
                intent.putExtra("id", id);
                intent.putExtra("type", type);
                intent.putExtra("docId", docId);
                startActivity(intent);
                finish();
            }
        });
    }

    void deleteSensor() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceFotSensors().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(SensorDetailsActivity.this, "Sensor deleted successfully");
                    finish();
                } else {
                    Utility.showToast(SensorDetailsActivity.this, "Failed to delete sensor");
                }
            }
        });

    }



    private void getSingleSensorData(Integer id,String url,String bearerToken)  {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id",id);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,requestBody, response -> {
                gson = new Gson();
                SingleValue sensorData = gson.fromJson(response.toString(),SingleValue.class);
                resultSingleValue.setText(String.format(sensorData.getValue()+sensorData.getUnit()));
            },error -> {
                resultSingleValue.setText(error.getMessage());
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "+bearerToken);
                    return headers;

                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getRangeSensorData(Integer id,String url,String bearerToken,String fromDate,String toDate)  {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id",id);
            requestBody.put("fromDate",fromDate);
            requestBody.put("toDate",toDate);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,requestBody, response -> {
                gson = new Gson();
                RangeValues sensorData = gson.fromJson(response.toString(),RangeValues.class);
                resultRangeValues.setText(String.format(sensorData.getValues()+sensorData.getUnit()));
            },error -> {
                resultRangeValues.setText(error.getMessage());
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer "+bearerToken);
                    return headers;

                }
            };
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}