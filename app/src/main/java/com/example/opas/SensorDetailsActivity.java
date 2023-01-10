package com.example.opas;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SensorDetailsActivity extends AppCompatActivity {
    LineGraphSeries<DataPoint> series;
    TextView nameEditText, zoneEditText;
    ImageButton editSensorButton, deleteSensorButton;
    ImageView iconImageView,refreshImageView,saveDate;
    String name, zone, singleValueId,rangeValuesId, type, docId;
    GraphView graph;
    DatePickerDialog picker;
    EditText dateEdit;

    private RequestQueue requestQueue;
    private Gson gson;

    private static final String urlSingleValue = "https://mdl.frederick.ac.cy/cyriciotwebapis/api/Data/GetCardWidgetData";
    private static final String urlRangeValues= "https://mdl.frederick.ac.cy/cyriciotwebapis/api/Data/GetLineChartSingleSensorData";

    private static final String bearerToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhY3NjNDIzQGZyZWRlcmljay5hYy5jeSIsImp0aSI6IjVhMTVhMzdhLTdiMGMtNDY5Mi1hNzMzLTlkNjI4YTljZjJiOCIsImVtYWlsIjoiYWNzYzQyM0BmcmVkZXJpY2suYWMuY3kiLCJ1aWQiOiIwODk1MDQ2NS01YmZlLTRjZjYtYjQwNS1mM2YyNjNjY2YxNDkiLCJyb2xlcyI6IkN1c3RvbWVyIiwiZXhwIjoxNzAyMzY5Njk3LCJpc3MiOiJTZWN1cmVBcGkiLCJhdWQiOiJTZWN1cmVBcGlVc2VyIn0.kn_u0mZflNxTmb2Gn5moyZCF_gmQatzIsE1wLOpbM_w";

    private TextView resultSingleValue;
    String currentDate = String.valueOf(java.time.LocalDate.now());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);
        System.out.println("Current Date: "+currentDate);

        resultSingleValue = findViewById(R.id.live_data_text_view);

        requestQueue = Volley.newRequestQueue(this);
        refreshImageView = findViewById(R.id.refresh);

        singleValueId = getIntent().getStringExtra("singleValueId");
        rangeValuesId = getIntent().getStringExtra("rangeValuesId");

        refreshImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getSingleSensorData(Integer.parseInt(singleValueId),urlSingleValue,bearerToken);
                getRangeSensorData(Integer.valueOf(rangeValuesId),urlRangeValues,bearerToken,currentDate,currentDate);
                Utility.showToast(SensorDetailsActivity.this, "Data Refreshed");

            }
        });

        dateEdit= findViewById(R.id.date_edit_view);
        saveDate= findViewById(R.id.date_save);
        dateEdit.setInputType(InputType.TYPE_NULL);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                picker = new DatePickerDialog(SensorDetailsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                dateEdit.setText(year+ "-" + String.format("%02d", (monthOfYear + 1))+"-"+String.format("%02d", dayOfMonth) );
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        saveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String date= dateEdit.getText().toString();
                 System.out.println("Date: "+date);
                 if(!Objects.equals(date, ""))
                    getRangeSensorData(Integer.valueOf(rangeValuesId),urlRangeValues,bearerToken,date,date);
            }
        });

        getSingleSensorData(Integer.parseInt(singleValueId),urlSingleValue,bearerToken);
        getRangeSensorData(Integer.valueOf(rangeValuesId),urlRangeValues,bearerToken,currentDate,currentDate);

        nameEditText = findViewById(R.id.sensor_name_text_view);
        zoneEditText = findViewById(R.id.sensor_zone_text_view);
        iconImageView = findViewById(R.id.live_data_icon);
        editSensorButton = findViewById(R.id.edit_sensor_btn);
        deleteSensorButton = findViewById(R.id.delete_sensor_btn);

        graph =findViewById(R.id.graph);
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Time");
        gridLabel.getVerticalAxisTitleWidth();

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        type = getIntent().getStringExtra("type");
        docId = getIntent().getStringExtra("docId");

        nameEditText.setText(name);
        zoneEditText.setText(zone);

        if (Objects.equals(type, "Activity")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            iconImageView.setColorFilter(Color.argb(255, 0, 204, 102));
            gridLabel.setVerticalAxisTitle("People");

        } else if (Objects.equals(type, "Temperature")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
            iconImageView.setColorFilter(Color.argb(255, 255, 128, 0));
            gridLabel.setVerticalAxisTitle("℃");

        } else if (Objects.equals(type, "Humidity")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_water_drop_24);
            iconImageView.setColorFilter(Color.argb(255, 51, 153, 255));
            gridLabel.setVerticalAxisTitle("RH");

        } else if (Objects.equals(type, "CO2")) {
            iconImageView.setImageResource(R.drawable.ic_baseline_co2_24);
            iconImageView.setColorFilter(Color.argb(255, 128, 128, 128));
            gridLabel.setVerticalAxisTitle("ppm");
        }

        deleteSensorButton.setOnClickListener((v) -> deleteSensor());

        editSensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SensorDetailsActivity.this, NewSensorActivity.class);

                intent.putExtra("name", name);
                intent.putExtra("zone", zone);
                intent.putExtra("singleValueId", singleValueId);
                intent.putExtra("rangeValuesId", rangeValuesId);
                intent.putExtra("type", type);
                intent.putExtra("docId", docId);
                startActivity(intent);
                finish();
            }
        });
    }

    void deleteSensor() {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForSensors().document(docId);
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
        try {graph.removeAllSeries();}
        catch (Exception e){}
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id",id);
            requestBody.put("fromDate",fromDate);
            requestBody.put("toDate",toDate);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,requestBody, response -> {
                gson = new Gson();
                RangeValues sensorData = gson.fromJson(response.toString(),RangeValues.class);
                series = new LineGraphSeries<DataPoint>();

                List<Object> array=sensorData.getValues();
                Integer size=array.size();

                for (int i=0; i<array.size(); i++) {
                    String row =String.format(array.get(i).toString());
                    String[] arrOfStr = row.split(", value=", 2);
                    String dateTime= arrOfStr[0].replace("{date=","");
                    String value= arrOfStr[1].replace("}","");
                    Integer index=dateTime.indexOf('T');
                    String time=dateTime.substring(index+1, index+6);
                    time= time.replace("00:","24.");
                    time= time.replace(":",".");
                    if(time.contains("01.")){ size--; }
                    else {
                    series.appendData(new DataPoint(Double.parseDouble(time), Double.parseDouble(value)),true,size);
                    }
                }

                if (Objects.equals(sensorData.getDescription(), "Activity")) {
                    series.setColor(Color.argb(255, 0, 204, 102));
                } else if (Objects.equals(sensorData.getDescription(), "Temperature")) {
                    series.setColor(Color.argb(255, 255, 128, 0));
                } else if (Objects.equals(sensorData.getDescription(), "Humidity")) {
                    series.setColor(Color.argb(255, 51, 153, 255));
                } else if (Objects.equals(sensorData.getDescription(), "CO2")) {
                    series.setColor(Color.argb(255, 128, 128, 128));
                }

                graph.addSeries(series);
                graph.getViewport().setScrollable(true);
                graph.getViewport().setScalable(true);
            },error -> {
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