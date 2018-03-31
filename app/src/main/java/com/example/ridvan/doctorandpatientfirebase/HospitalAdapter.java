package com.example.ridvan.doctorandpatientfirebase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.MyViewHolder> {
    ArrayList<Hospital> mDataList;
    LayoutInflater inflater;
    public HospitalAdapter(Context context, ArrayList<Hospital> data) {
        //inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(context);
        this.mDataList = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.hospital_list, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Hospital tiklanilanHospital = mDataList.get(position);
        holder.setData(tiklanilanHospital, position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHospitalName,textViewHospitalCity;
        public MyViewHolder(View itemView) {
            super(itemView);
            textViewHospitalName = (TextView) itemView.findViewById(R.id.textViewHospitalName);
            textViewHospitalCity = (TextView) itemView.findViewById(R.id.textViewHospitalCity);
        }
        public void setData(Hospital tiklanilanHospital, int position) {
            this.textViewHospitalName.setText( tiklanilanHospital.getHospital_city() );
            this.textViewHospitalCity.setText(tiklanilanHospital.getHospital_name());
        }
    }
}