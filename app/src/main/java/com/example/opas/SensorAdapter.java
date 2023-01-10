package com.example.opas;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Objects;

public class SensorAdapter extends FirestoreRecyclerAdapter<Sensor, SensorAdapter.SensorViewHolder> {
    Context context;

    public SensorAdapter(@NonNull FirestoreRecyclerOptions<Sensor> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SensorViewHolder holder, int position, @NonNull Sensor sensor) {
        holder.nameTextView.setText(sensor.Name);
        holder.zoneTextView.setText(sensor.Zone);

        if(Objects.equals(sensor.Type, "Activity")){
            holder.iconImageView.setImageResource(R.drawable.ic_baseline_camera_alt_24);
            holder.iconImageView.setColorFilter(Color.argb(255, 0, 204, 102));
        }else if(Objects.equals(sensor.Type, "Temperature")){
            holder.iconImageView.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
            holder.iconImageView.setColorFilter(Color.argb(255, 255, 128, 0));
        }
        else if(Objects.equals(sensor.Type, "Humidity")){
            holder.iconImageView.setImageResource(R.drawable.ic_baseline_water_drop_24);
            holder.iconImageView.setColorFilter(Color.argb(255, 51, 153, 255));
        }
        else if(Objects.equals(sensor.Type, "CO2")){
            holder.iconImageView.setImageResource(R.drawable.ic_baseline_co2_24);
            holder.iconImageView.setColorFilter(Color.argb(255, 128, 128, 128));
        }


        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,SensorDetailsActivity.class);
            intent.putExtra("name",sensor.Name);
            intent.putExtra("zone",sensor.Zone);
            intent.putExtra("singleValueId",sensor.SingleValueId);
            intent.putExtra("rangeValuesId",sensor.RangeValuesId);
            intent.putExtra("type",sensor.Type);

            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);

        });

    }

    @NonNull
    @Override
    public SensorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sensor_item,parent,false);
        return new SensorViewHolder(view);
    }

    class SensorViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,zoneTextView;
        ImageView iconImageView;
        public SensorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView=itemView.findViewById(R.id.sensor_name_text_view);
            zoneTextView=itemView.findViewById(R.id.sensor_zone_text_view);
            iconImageView=itemView.findViewById(R.id.sensor_icon);

        }
    }
}
