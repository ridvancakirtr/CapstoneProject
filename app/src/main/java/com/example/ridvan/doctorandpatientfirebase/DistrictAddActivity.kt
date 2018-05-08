package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_district_add.*

class DistrictAddActivity : AppCompatActivity() {
    var ref=FirebaseDatabase.getInstance().reference
    var district=District()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_district_add)
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
        var redirectMainPage= Intent(this@DistrictAddActivity, AdminActivity::class.java)
        startActivity(redirectMainPage)
    }
}
