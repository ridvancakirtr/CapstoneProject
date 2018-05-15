package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_patient_management.*

class PatientManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_management)
        val actionBar=supportActionBar
        actionBar!!.title="Patient Management"
        actionBar.setDisplayHomeAsUpEnabled(true)
        btnAllPatient.setOnClickListener {
            var patientListIntent=Intent(this@PatientManagementActivity, AllPatientActivity::class.java)
            startActivity(patientListIntent)
        }
        btnAddPatient.setOnClickListener {
            var patientAddIntent= Intent(this@PatientManagementActivity, PatientAddActivity::class.java)
            startActivity(patientAddIntent)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@PatientManagementActivity,AdminActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@PatientManagementActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@PatientManagementActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
