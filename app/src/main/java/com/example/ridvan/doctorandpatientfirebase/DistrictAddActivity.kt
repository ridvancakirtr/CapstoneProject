package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_district_add.*

class DistrictAddActivity : AppCompatActivity() {
    var ref=FirebaseDatabase.getInstance().reference
    var district=District()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_district_add)
        val actionBar=supportActionBar
        actionBar!!.title="District Add Page"
        actionBar.setDisplayHomeAsUpEnabled(true)

        btnDistrict.setOnClickListener {
            if (editTextDistrict.text.isNotEmpty()){
                addDistrict()
            }else{
                Toast.makeText(this@DistrictAddActivity,"Please Fill in the Blank Fields",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addDistrict() {
        var id = ref.push().key
        district.district=editTextDistrict.text.toString()
        district.district_id=id
                ref.child("District").child(id)
                        .setValue(district)
                        .addOnCompleteListener {task ->
                            if (task.isSuccessful){
                                Toast.makeText(this@DistrictAddActivity,"District is Saved",Toast.LENGTH_SHORT).show()
                                redirectMainPage()
                            }else{
                                Toast.makeText(this@DistrictAddActivity,"ERROR District is Not Saved",Toast.LENGTH_SHORT).show()
                            }

                        }
    }
    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@DistrictAddActivity, DistrictManagementActivity::class.java)
        startActivity(redirectMainPage)
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@DistrictAddActivity,DistrictManagementActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@DistrictAddActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@DistrictAddActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
