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

    //    TextInputLayout typeEditText;
    EditText nameEditText, zoneEditText, idEditText;
    ImageButton saveSensorButton;
    TextView pageTitleTextView;
    String name, zone, id, type, docId;
    boolean isEditMode = false;
    String[] types = {"Activity", "Temperature", "Humidity", "CO2"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sensor);

        nameEditText = findViewById(R.id.sensor_name);
        zoneEditText = findViewById(R.id.sensor_zone);
        idEditText = findViewById(R.id.sensor_id);
        typeEditText = findViewById(R.id.spinner_type);
        saveSensorButton = findViewById(R.id.save_sensor_button);
        pageTitleTextView = findViewById(R.id.page_title);

        name = getIntent().getStringExtra("name");
        zone = getIntent().getStringExtra("zone");
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        docId = getIntent().getStringExtra("docId");

        typeEditText = findViewById(R.id.spinner_type);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);
        typeEditText.setAdapter(adapter);
//        typeEditText.setOnItemSelectedListener(this);


        if (docId != null && !docId.isEmpty()) {
            isEditMode = true;
        }

        if (isEditMode) {
            pageTitleTextView.setText("Edit Sensor");
            nameEditText.setText(name);
            zoneEditText.setText(zone);
            System.out.println("type:"+type);
            int spinnerPosition = adapter.getPosition(type);
            typeEditText.setSelection(spinnerPosition);
            idEditText.setText(id);
        }
        saveSensorButton.setOnClickListener((v) -> saveSensor());
    }

    void saveSensor() {
        TextView typeEditTextSpinner = (TextView)typeEditText.getSelectedView();


        String sensorName = nameEditText.getText().toString();
        String sensorZone = zoneEditText.getText().toString();
        String sensorId = idEditText.getText().toString();
        String sensorType = typeEditTextSpinner.getText().toString();

        if (sensorName == null || sensorName.isEmpty()) {
            nameEditText.setError("Name is required");
            return;
        }
        if (sensorZone == null || sensorZone.isEmpty()) {
            zoneEditText.setError("Zone is required");
            return;
        }

        if (sensorId == null || sensorId.isEmpty()) {
            idEditText.setError("Id is required");
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
        if (isNumeric(sensorId)) {
            sensor.setId(sensorId);
            saveSensorToFirebase(sensor);
        } else {
            idEditText.setError("Id not formatted correctly");
            return;
        }

    }

    public static boolean isNumeric(String string) {
        int intValue;

        System.out.println(String.format("Parsing string: \"%s\"", string));

        if (string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }

    void saveSensorToFirebase(Sensor sensor) {
        DocumentReference documentReference;
        if (isEditMode) {
            documentReference = Utility.getCollectionReferenceFotSensors().document(docId);

        } else {
            documentReference = Utility.getCollectionReferenceFotSensors().document();
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