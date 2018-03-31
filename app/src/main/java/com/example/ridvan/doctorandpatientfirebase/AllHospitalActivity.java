package com.example.ridvan.doctorandpatientfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AllHospitalActivity extends AppCompatActivity {
    DatabaseReference mPostReference;
    private static final String TAG = "Data";
    ArrayList<Hospital> allHospital=new ArrayList<>();
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_all_hospital );

        recyclerView=findViewById(R.id.recyclerViewHospital);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Query query = mPostReference.child("Hospital").orderByKey();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    Hospital post= postSnapshot.getValue(Hospital.class);
                    allHospital.add( new Hospital(post.hospital_name, post.hospital_city) );

                }

                HospitalAdapter hospitalAdapter=new HospitalAdapter(AllHospitalActivity.this,allHospital);
                recyclerView.setAdapter(hospitalAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

}


