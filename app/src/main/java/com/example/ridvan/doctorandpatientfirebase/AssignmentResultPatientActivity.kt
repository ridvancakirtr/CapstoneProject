package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_assignment_result_patient.*
import java.util.ArrayList

class AssignmentResultPatientActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var allPatient= ArrayList<PatientDataModel>()
    var doctorUserID:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_result_patient)
        doctorUserID = intent.getStringExtra("DoctorUserID")

        var linearLayoutManeger = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycleListViewAssginmentResultPatient.layoutManager = linearLayoutManeger

        val query = ref.child("Patient").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(PatientDataModel::class.java)
                    if (post!!.doctor_user_id==doctorUserID){
                        allPatient.add(PatientDataModel(post!!.patient_name_surname, post.patient_profile_picture, post.district,post.patient_mobile_phone,post.patient_user_id,post.doctor_user_id))
                    }
                }

                var myAdapter=PatientAdapter(allPatient)
                recycleListViewAssginmentResultPatient.adapter=myAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}