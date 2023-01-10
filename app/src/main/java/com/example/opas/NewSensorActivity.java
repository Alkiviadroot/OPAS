package com.example.opas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;

public class NewSensorActivity extends AppCompatActivity {
    Spinner typeEditText;
    EditText nameEditText, zoneEditText, singleValueIdEditText,rangeValuesIdEditText;
    ImageButton saveSensorButton;
    TextView pageTitleTextView;
    String name, zone, singleValueId,rangeValuesId, type, docId;
    String[] types = {"Activity", "Temperature", "Humidity", "CO2"};
    boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sensor);

        nameEditText = findViewById(R.id.sensor_name);
        zoneEditText = findViewById(R.id.sensor_zone);
        singleValueIdEditText = findViewById(R.id.sensor_id_single);
        rangeValuesIdEditText = findViewById(R.id.sensor_id_range);
        typeEditText = findViewById(R.id.spinner_type);
        saveSensorButton = findViewById(R.id.save_sensor_button);
        pageTitleTextView = findViewById(R.id.page_title);

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        singleValueId = getIntent().getStringExtra("singleValueId");
        rangeValuesId = getIntent().getStringExtra("rangeValuesId");
        type = getIntent().getStringExtra("type");
        docId = getIntent().getStringExtra("docId");

        typeEditText = findViewById(R.id.spinner_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        typeEditText.setAdapter(adapter);

        if (docId != null && !docId.isEmpty()) {isEditMode = true;}

        if (isEditMode) {
            pageTitleTextView.setText("Edit Sensor");
            nameEditText.setText(name);
            zoneEditText.setText(zone);
            singleValueIdEditText.setText(singleValueId);
            rangeValuesIdEditText.setText(rangeValuesId);

            int spinnerPosition = adapter.getPosition(type);
            typeEditText.setSelection(spinnerPosition);
        }
        saveSensorButton.setOnClickListener((v) -> saveSensor());
    }

    void saveSensor() {
        TextView typeEditTextSpinner = (TextView)typeEditText.getSelectedView();
        String sensorName = nameEditText.getText().toString();
        String sensorZone = zoneEditText.getText().toString();
        String sensorSingleValueId = singleValueIdEditText.getText().toString();
        String sensorRangeValuesId = rangeValuesIdEditText.getText().toString();
        String sensorType = typeEditTextSpinner.getText().toString();

        if (sensorName == null || sensorName.isEmpty()) {
            nameEditText.setError("Name is required");
            return;
        }
        if (sensorZone == null || sensorZone.isEmpty()) {
            zoneEditText.setError("Zone is required");
            return;
        }

        if (sensorSingleValueId == null || sensorSingleValueId.isEmpty()) {
            singleValueIdEditText.setError("Id is required");
            return;
        }

        if (sensorRangeValuesId == null || sensorRangeValuesId.isEmpty()) {
            rangeValuesIdEditText.setError("Id is required");
            return;
        }

        if (sensorType == null || sensorType.isEmpty()) {
            typeEditTextSpinner.setError("Type is required");
            return;
        }

        Sensor sensor = new Sensor();
        sensor.setName(sensorName);
        sensor.setZone(sensorZone);
        sensor.setType(sensorType);
        if (isNumeric(sensorSingleValueId)) {
            sensor.setSingleValueId(sensorSingleValueId);
        } else {
            singleValueIdEditText.setError("Id not formatted correctly");
            return;
        }
        if (isNumeric(sensorRangeValuesId)) {
            sensor.setRangeValuesId(sensorRangeValuesId);
        } else {
            rangeValuesIdEditText.setError("Id not formatted correctly");
            return;
        }
        saveSensorToFirebase(sensor);
    }

    public static boolean isNumeric(String string) {
        int intValue;
        if (string == null || string.equals("")) {
            return false;
        }
        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {}
        return false;
    }

    void saveSensorToFirebase(Sensor sensor) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceForSensors().document(docId);
        } else {
            documentReference = Utility.getCollectionReferenceForSensors().document();
        }
        documentReference.set(sensor).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Utility.showToast(NewSensorActivity.this, "Sensor added successfully");
                    finish();
                } else {
                    Utility.showToast(NewSensorActivity.this, "Failed to add sensor");
                }
            }
        });
    }
}