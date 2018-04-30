package com.example.ridvan.doctorandpatientfirebase.Patient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.PatientDataTempEkgPulse
import com.example.ridvan.doctorandpatientfirebase.R
import com.example.ridvan.doctorandpatientfirebase.R.id.dataDate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_patient_data.*

class PatientDataActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var mAuth= FirebaseAuth.getInstance().currentUser
    var allData= ArrayList<PatientDataTempEkgPulse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_data)
        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewPatientData.layoutManager=linearLayoutManeger

        var dataList = object : PatientDataAdapter(allData) {
            override fun onBindViewHolder(holder: PatientDataAdapter.MyDataHolder?, position: Int) {
                holder?.date?.text= dataDate[position].datadate
                holder?.oneLineData?.setOnClickListener {
                    Toast.makeText(this@PatientDataActivity,"Tıklandı"+position,Toast.LENGTH_SHORT).show()
                }
            }

        }

        var query=ref.child("PatientDataTempEkgPulse").child(mAuth!!.uid).orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    val post=postSnapshot.getValue(PatientDataTempEkgPulse::class.java)
                    allData.add(PatientDataTempEkgPulse(post!!.temperature,post.ekg,post.pulse,post.datadate,post.id))
                }
                recycleListViewPatientData.adapter = dataList
            }

        })
    }

}
