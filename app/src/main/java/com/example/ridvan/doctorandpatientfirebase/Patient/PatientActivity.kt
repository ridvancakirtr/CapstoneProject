package com.example.ridvan.doctorandpatientfirebase.Patient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_patient.*


class PatientActivity : AppCompatActivity() {
    var user = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        if (user != null) {
            btnUserProfile.setOnClickListener {
                var userProfile=Intent(this@PatientActivity, PatientProfileActivity::class.java)
                startActivity(userProfile)
            }
            btnLiveTest.setOnClickListener {
                var liveTest = Intent(this@PatientActivity, PatientLineChartActivity::class.java)
                startActivity(liveTest)
            }
        }


    }
}
