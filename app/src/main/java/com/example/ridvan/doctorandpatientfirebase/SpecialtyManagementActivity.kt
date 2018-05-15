package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_specialty_management.*

class SpecialtyManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialty_management)
        val actionBar=supportActionBar
        actionBar!!.title="Specialty Management"
        actionBar.setDisplayHomeAsUpEnabled(true)
        specialtyAdd.setOnClickListener {
            var specialtyAddIntent=Intent(this@SpecialtyManagementActivity, SpecialtyAddActivity::class.java)
            startActivity(specialtyAddIntent)
        }
        specialtyAll.setOnClickListener {
            var specialtyAllIntent= Intent(this@SpecialtyManagementActivity, SpecialtyAllActivity::class.java)
            startActivity(specialtyAllIntent)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@SpecialtyManagementActivity,AdminActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@SpecialtyManagementActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@SpecialtyManagementActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
