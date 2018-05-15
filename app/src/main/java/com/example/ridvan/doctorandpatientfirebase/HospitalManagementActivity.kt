package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_hospital_management.*
class HospitalManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_management)
        val actionBar=supportActionBar
        actionBar!!.title="Hospital Management"
        actionBar.setDisplayHomeAsUpEnabled(true)
        btnAllHospital.setOnClickListener {
            var allDoctorIntent=Intent(this@HospitalManagementActivity, AllHospitalActivity::class.java)
            startActivity(allDoctorIntent)
        }
        btnHospitalAdd.setOnClickListener {
            var addHospitalIntent=Intent(this@HospitalManagementActivity, HospitalAddActivity::class.java)
            startActivity(addHospitalIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@HospitalManagementActivity,AdminActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@HospitalManagementActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@HospitalManagementActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
