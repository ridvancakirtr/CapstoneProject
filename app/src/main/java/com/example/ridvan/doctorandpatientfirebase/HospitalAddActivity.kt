package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_hospital_add.*
import kotlinx.android.synthetic.main.activity_patient_profile.*
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class HospitalAddActivity : AppCompatActivity() {
    lateinit var spinnerDistrict: Spinner
    var selectDistrict = arrayOf("Please Select District Specialty")
    val dataDistrict = ArrayList<String>(Arrays.asList(*selectDistrict))
    var hospital=HospitalDataModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital_add)
        val actionBar=supportActionBar
        actionBar!!.title="Hospital Add"
        actionBar.setDisplayHomeAsUpEnabled(true)
        readDistrict()
        spinnerAddDistrict()
        btnHospitalAdd.setOnClickListener {
            if (editTextHospitalName.text.isNotEmpty() &&spinnerHospitalDistrict.selectedItemPosition !== 0){
                addHospital()
            }else{
                Toast.makeText(this@HospitalAddActivity,"Please Fill in the Blank Fields",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addHospital() {
        var ref=FirebaseDatabase.getInstance().reference
        var id=ref.push().key
        hospital.hospital_name=editTextHospitalName.text.toString()
        hospital.hospital_city=spinnerHospitalDistrict.selectedItem.toString()
        hospital.hospital_id=id
        ref.child("Hospital").child(id)
                .setValue(hospital)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        Toast.makeText(this@HospitalAddActivity,"Hospital is Saved",Toast.LENGTH_SHORT).show()
                        redirectMainPage()
                    }
                    else{
                        Toast.makeText(this@HospitalAddActivity,"ERROR Hospital is Not Saved",Toast.LENGTH_SHORT).show()
                    }

                }
    }

    private fun spinnerAddDistrict(){
        spinnerDistrict=findViewById(R.id.spinnerHospitalDistrict)
        spinnerDistrict.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataDistrict)
        spinnerDistrict.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }
        }
    }

    private fun readDistrict(){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("District").orderByKey()
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for (singleSnapshot in p0!!.children){
                    var readUser=singleSnapshot.getValue(District::class.java)
                    dataDistrict.add(readUser?.district.toString())

                }
            }

        })
    }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@HospitalAddActivity, HospitalManagementActivity::class.java)
        startActivity(redirectMainPage)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@HospitalAddActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@HospitalAddActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }


}
