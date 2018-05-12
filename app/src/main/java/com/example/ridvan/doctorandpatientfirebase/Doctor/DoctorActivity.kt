package com.example.ridvan.doctorandpatientfirebase.Doctor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.LoginActivity
import com.example.ridvan.doctorandpatientfirebase.R
import kotlinx.android.synthetic.main.activity_doctor.*

class DoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)
        val actionBar=supportActionBar
        actionBar!!.title="Personal Page of the Doctor"

        btnMyProfile.setOnClickListener {
            var intent=Intent(this@DoctorActivity,DoctorProfileActivity::class.java)
            startActivity(intent)
        }
        btnMyPatient.setOnClickListener {
            var intent=Intent(this@DoctorActivity,DoctorPatientListActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@DoctorActivity , LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@DoctorActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
