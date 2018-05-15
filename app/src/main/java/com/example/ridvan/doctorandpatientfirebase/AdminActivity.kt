package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_admin.*



class AdminActivity : AppCompatActivity() {
    var mAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        val actionBar=supportActionBar
        actionBar!!.title="Management Panel"
        doctorManagement.setOnClickListener {
            var doctorManagement=Intent(this@AdminActivity, DoctorManagementActivity::class.java)
            startActivity(doctorManagement)
        }
        patientManagement.setOnClickListener {
            var patientManagement=Intent(this@AdminActivity, PatientManagementActivity::class.java)
            startActivity(patientManagement)
        }
        hospitalManagement.setOnClickListener {
            var hospitalManagement=Intent(this@AdminActivity, HospitalManagementActivity::class.java)
            startActivity(hospitalManagement)
        }
        districtManagement.setOnClickListener {
            var districtManagement=Intent(this@AdminActivity, DistrictManagementActivity::class.java)
            startActivity(districtManagement)
        }
        specialtyManagement.setOnClickListener {
            var specialtyManagement=Intent(this@AdminActivity, SpecialtyManagementActivity::class.java)
            startActivity(specialtyManagement)
        }
        btnAssignmentResult.setOnClickListener {
            var assginmentDoctor=Intent(this@AdminActivity, AssignmentResultActivity::class.java)
            startActivity(assginmentDoctor)
        }

    }

    override fun onResume() {
        checkCurrentUser()
        super.onResume()

    }


    private fun checkCurrentUser() {
        var user=mAuth.currentUser
        if (user==null){
            var intent=Intent(this@AdminActivity,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@AdminActivity ,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AdminActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }

}