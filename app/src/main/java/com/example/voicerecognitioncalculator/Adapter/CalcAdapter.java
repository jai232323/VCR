package com.example.voicerecognitioncalculator.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.voicerecognitioncalculator.Module.CalCData;
import com.example.voicerecognitioncalculator.R;

import java.util.ArrayList;

public class CalcAdapter extends RecyclerView.Adapter<CalcAdapter.ViewHolder>  {


    Context context;
    ArrayList<CalCData> list;

    public CalcAdapter(Context context, ArrayList<CalCData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.calc_adapter,parent,false);

        return new ViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CalCData data = list.get(position);

        holder.Operation.setText(data.getOperation());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView Operation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            Operation=itemView.findViewById(R.id.Operation);
        }
    }
}
