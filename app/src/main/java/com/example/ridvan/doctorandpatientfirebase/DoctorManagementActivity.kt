package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_doctor_management.*

class DoctorManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_management)
        val actionBar=supportActionBar
        actionBar!!.title="Doctor Management"
        actionBar.setDisplayHomeAsUpEnabled(true)
        btnAddDoctor.setOnClickListener {
            var doctorAddIntent=Intent(this@DoctorManagementActivity, DoctorAddActivity::class.java)
            startActivity(doctorAddIntent)
        }
        btnAllDoctor.setOnClickListener {
            var doctorListIntent= Intent(this@DoctorManagementActivity, AllDoctorActivity::class.java)
            startActivity(doctorListIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@DoctorManagementActivity,AdminActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@DoctorManagementActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@DoctorManagementActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
