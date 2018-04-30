package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {
    var mAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        btnAddDoctor.setOnClickListener {
            var doctorAddIntent=Intent(this@AdminActivity, DoctorAddActivity::class.java)
            startActivity(doctorAddIntent)
        }
        btnAllDoctor.setOnClickListener {
            var doctorListIntent=Intent(this@AdminActivity, AllDoctorActivity::class.java)
            startActivity(doctorListIntent)
        }
        btnAllPatient.setOnClickListener {
            var patientListIntent=Intent(this@AdminActivity, AllPatientActivity::class.java)
            startActivity(patientListIntent)
        }
        btnAddPatient.setOnClickListener {
            var patientAddIntent=Intent(this@AdminActivity, PatientAddActivity::class.java)
            startActivity(patientAddIntent)
        }

        btnAllHospital.setOnClickListener {
            var allDoctorIntent=Intent(this@AdminActivity, AllHospitalActivity::class.java)
            startActivity(allDoctorIntent)
        }
        btnHospitalAdd.setOnClickListener {
            var addHospitalIntent=Intent(this@AdminActivity, HospitalAddActivity::class.java)
            startActivity(addHospitalIntent)
        }
        btnDistrictAdd.setOnClickListener {
            var addDistrictIntent=Intent(this@AdminActivity, DistrictAddActivity::class.java)
            startActivity(addDistrictIntent)
        }
        btnAssignmentResult.setOnClickListener {
            var addAssignmentIntent=Intent(this@AdminActivity, AssignmentResultActivity::class.java)
            startActivity(addAssignmentIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        checkCurrentUser()

    }


    private fun checkCurrentUser() {
        var user=mAuth.currentUser
        if (user==null){
            var intent=Intent(this@AdminActivity,LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }
}