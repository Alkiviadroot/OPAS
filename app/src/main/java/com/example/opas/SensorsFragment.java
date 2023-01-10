package com.example.opas;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class SensorsFragment extends Fragment {
    FloatingActionButton addSensorBtn;
    RecyclerView recyclerView;
    SensorAdapter sensorAdapter;

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addSensorBtn = view.findViewById(R.id.add_sensor_btn);
        recyclerView = view.findViewById(R.id.recycler_view);
        addSensorBtn.setOnClickListener((v)->startActivity(new Intent(getActivity(),NewSensorActivity.class)));
        setupRecyclerView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForSensors();
        FirestoreRecyclerOptions<Sensor> options= new FirestoreRecyclerOptions.Builder<Sensor>().setQuery(query,Sensor.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sensorAdapter= new SensorAdapter(options,getActivity());
        recyclerView.setAdapter(sensorAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        sensorAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        sensorAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorAdapter.notifyDataSetChanged();
    }
}