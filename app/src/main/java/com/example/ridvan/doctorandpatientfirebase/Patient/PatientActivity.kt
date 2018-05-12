package com.example.ridvan.doctorandpatientfirebase.Patient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import com.example.ridvan.doctorandpatientfirebase.LoginActivity
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_patient.*


class PatientActivity : AppCompatActivity() {
    var user = FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient)
        val actionBar=supportActionBar
        actionBar!!.title="Personal Page of the Patient"
        if (user != null) {
            btnUserProfile.setOnClickListener {
                var userProfile=Intent(this@PatientActivity, PatientProfileActivity::class.java)
                startActivity(userProfile)
            }

            btnUserData.setOnClickListener {
                var userData=Intent(this@PatientActivity, PatientDataActivity::class.java)
                startActivity(userData)
            }

            btnLiveTest.setOnClickListener {
                var liveTest = Intent(this@PatientActivity, PatientLineChartActivity::class.java)
                startActivity(liveTest)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@PatientActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@PatientActivity,"Log Out",Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}
