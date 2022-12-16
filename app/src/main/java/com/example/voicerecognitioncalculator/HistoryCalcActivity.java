package com.example.voicerecognitioncalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.voicerecognitioncalculator.Adapter.CalcAdapter;
import com.example.voicerecognitioncalculator.Module.CalCData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryCalcActivity extends AppCompatActivity {


    RecyclerView CalcRecyclerView;
    LinearLayout CalcNOData;

    private ArrayList<CalCData> list;
    private CalcAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_calc);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("History");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CalcRecyclerView=findViewById(R.id.CalcRecyclerView);
        CalcNOData=findViewById(R.id.CalcNOData);


        LinearLayoutManager linearLayoutManager =  new LinearLayoutManager(HistoryCalcActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        CalcRecyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new CalcAdapter(HistoryCalcActivity.this,list);
        CalcRecyclerView.setAdapter(adapter);



        getStaffData();



    }

    private void getStaffData() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Calculator");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                if (!snapshot.exists()){
                    CalcNOData.setVisibility(View.VISIBLE);
                    CalcRecyclerView.setVisibility(View.GONE);
                }else {
                    CalcNOData.setVisibility(View.GONE);
                    CalcRecyclerView.setVisibility(View.VISIBLE);

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CalCData data = dataSnapshot.getValue(CalCData.class);
                        list.add(0, data);
                    }

                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}