package com.example.ridvan.doctorandpatientfirebase.Doctor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ridvan.doctorandpatientfirebase.R
import kotlinx.android.synthetic.main.activity_doctor.*

class DoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        supportActionBar!!.title = "Personal Page of the Doctor"

        btnMyProfile.setOnClickListener {
            var intent=Intent(this@DoctorActivity,DoctorProfileActivity::class.java)
            startActivity(intent)
        }
        btnMyPatient.setOnClickListener {
            var intent=Intent(this@DoctorActivity,DoctorPatientListActivity::class.java)
            startActivity(intent)
        }
    }
}
