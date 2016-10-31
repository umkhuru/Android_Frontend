package com.example.ethon.car_service_station;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ethon.car_service_station.domain.Job;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Job> {

    private final Context context;
    private final ArrayList<Job> jobList;


    public MyAdapter(Context context, ArrayList<Job> jobList) {
        super(context,R.layout.row,jobList);
        this.context=context;
        this.jobList=jobList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView=inflater.inflate(R.layout.row,parent,false);

        TextView labelView=(TextView)rowView.findViewById(R.id.jobLabel);
        TextView descView=(TextView)rowView.findViewById(R.id.jobDesc);

        labelView.setText(jobList.get(position).getDescription());
        descView.setText(jobList.get(position).getJobDate());

        return rowView;
    }
}
