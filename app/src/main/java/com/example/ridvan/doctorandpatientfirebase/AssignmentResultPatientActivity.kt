package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
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
        val actionBar=supportActionBar
        actionBar!!.title="Doctor's Patients"
        actionBar.setDisplayHomeAsUpEnabled(true)



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

                var myAdapter=PatientAssignmentAdapter(allPatient)
                recycleListViewAssginmentResultPatient.adapter=myAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@AssignmentResultPatientActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AssignmentResultPatientActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}