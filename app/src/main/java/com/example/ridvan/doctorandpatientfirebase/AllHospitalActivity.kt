package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_all_activity.*
import java.util.ArrayList

class AllHospitalActivity : AppCompatActivity() {
    internal lateinit var mPostReference: DatabaseReference
    var allDoctors= ArrayList<Hospital>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_activity)
        var myAdapter=HospitalAdapter(allDoctors)
        recycleListViewHospital.adapter=myAdapter

        var linearLayoutManeger=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recycleListViewHospital.layoutManager=linearLayoutManeger

        mPostReference = FirebaseDatabase.getInstance().reference
        val query = mPostReference.child("Hospital").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(Hospital::class.java)
                    allDoctors.add(Hospital(post!!.hospital_name, post.hospital_city))

                }

                val hospitalAdapter = HospitalAdapter(allDoctors)
                recycleListViewHospital.adapter = hospitalAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}
        