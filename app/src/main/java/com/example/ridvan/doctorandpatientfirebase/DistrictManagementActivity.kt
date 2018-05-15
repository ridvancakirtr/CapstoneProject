package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_district_management.*

class DistrictManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_district_management)
        val actionBar=supportActionBar
        actionBar!!.title="District Management"
        actionBar.setDisplayHomeAsUpEnabled(true)
        btnDistrictAdd.setOnClickListener {
            var addDistrictIntent=Intent(this@DistrictManagementActivity, DistrictAddActivity::class.java)
            startActivity(addDistrictIntent)
        }
        btnDistrictAll.setOnClickListener {
            var addDistrictAll= Intent(this@DistrictManagementActivity, AllDistrict::class.java)
            startActivity(addDistrictAll)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@DistrictManagementActivity,AdminActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@DistrictManagementActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@DistrictManagementActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
