package com.example.ridvan.doctorandpatientfirebase

import android.annotation.SuppressLint
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
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_patient.*
import java.util.*

class EditPatientActivity : AppCompatActivity() {
    var mPostReference= FirebaseDatabase.getInstance().reference
    var strogeRef= FirebaseStorage.getInstance().reference
    var userId: String? =null
    lateinit var spinnerDistrict: Spinner
    var selectDistrict = arrayOf("Please Select District")
    val dataDistrict = ArrayList<String>(Arrays.asList(*selectDistrict))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_patient)
        val actionBar=supportActionBar
        actionBar!!.title="Patient Update Profile"
        actionBar.setDisplayHomeAsUpEnabled(true)
        userId=intent.getStringExtra("userId")


        userReadData()
        btnEditPatient.setOnClickListener {
            updatePatient()
        }

        btnAddDoctor.setOnClickListener {
            var intent=Intent(this@EditPatientActivity,AssignmentDoctorActivity::class.java)
            intent.putExtra("userId",userId)
            startActivity(intent)
        }

        editPatientPictures.setOnClickListener {
            resetProfilePicture()
        }


    }

    private fun resetProfilePicture() {
        mPostReference.child("Patient").child(userId).child("patient_profile_picture").setValue("http://ishowmy.support/img/user-icon-360x360.jpg")
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        Picasso.with(this@EditPatientActivity)
                                .load("http://ishowmy.support/img/user-icon-360x360.jpg")
                                .into(editPatientPictures)
                        Toast.makeText(this@EditPatientActivity, "Patient Profile is Successfully Reset", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@EditPatientActivity, "Patient Profile NOT Reset", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun userReadData() {
        if (userId != null) {
            var query=mPostReference.child("Patient").orderByKey().equalTo(userId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }
                override fun onDataChange(p0: DataSnapshot?) {
                    for (singleSnapshot in p0!!.children){
                        var readUser = singleSnapshot?.getValue(PatientDataModel::class.java)
                        doctorReadData(readUser!!.patient_user_id!!)
                        editPatientNameSurname.setText(readUser!!.patient_name_surname)
                        spinnerReadAndSelectHospital(readUser.district!!)
                        editAdress.setText(readUser!!.adress)
                        editMobilePhone.setText(readUser!!.patient_mobile_phone)
                        editEmail.setText(readUser!!.patient_email)
                        editPassword.setText(readUser!!.patient_password)
                        if(readUser.patient_gender.toString()=="Male"){
                            editPatMale.isChecked=true
                        }else{
                            editPatFeMale.isChecked=true
                        }
                        Picasso.with(this@EditPatientActivity).load(readUser.patient_profile_picture).into(editPatientPictures)
                    }
                }
            })

            }

    }

    private fun doctorReadData(id :String) {
            var query=mPostReference.child("Doctors").orderByKey().equalTo(id)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }
                override fun onDataChange(p0: DataSnapshot?) {
                    for (singleSnapshot in p0!!.children){
                        var readUser = singleSnapshot?.getValue(DoctorDataModel::class.java)
                        editPatientDoctor.setText("DR. ${readUser!!.doctor_name_surname!!.toUpperCase()}")
                    }
                }
            })
    }

    private fun spinnerReadAndSelectHospital(district: String){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("District").orderByKey()

        var spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataDistrict)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for ((i, singleSnapshot) in p0!!.children.withIndex()){
                    var readUser=singleSnapshot.getValue(District::class.java)
                    dataDistrict.add(readUser?.district!!)
                }
                spinnerDistrict.setSelection(spinnerAdapter.getPosition(district))
            }
        })

        spinnerDistrict=findViewById(R.id.editDistrict)
        spinnerDistrict.adapter= spinnerAdapter
        spinnerDistrict.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
               // Log.e("Data", spinnerDistrict.getItemIdAtPosition(position).toString())

            }
        }
    }

    private fun updatePatient() {
        if (editPatientNameSurname.text.isNotEmpty() &&
                editDistrict.selectedItemPosition!=0 &&
                editAdress.text.isNotEmpty() &&
                editMobilePhone.text.isNotEmpty()
        ){
            val gender = if(editPatMale.isChecked){ "Male" }else { "FeMale" }
            val updatePatientProfile: HashMap<String, String> = HashMap()
            updatePatientProfile["patient_name_surname"] = editPatientNameSurname.text.toString()
            updatePatientProfile["patient_gender"] = gender
            updatePatientProfile["district"] = editDistrict.selectedItem.toString()
            updatePatientProfile["adress"] = editAdress.text.toString()
            updatePatientProfile["patient_mobile_phone"] = editMobilePhone.text.toString()

            FirebaseDatabase.getInstance().reference
                    .child("Patient")
                    .child(userId)
                    .updateChildren(updatePatientProfile as Map<String, String>?)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(this@EditPatientActivity,"Profile Successfully Updated",Toast.LENGTH_SHORT).show()
                            redirectMainPage()
                        }else{
                            Toast.makeText(this@EditPatientActivity,"ERROR Profile Not Updated",Toast.LENGTH_SHORT).show()
                        }
                    }
        }else{
            Toast.makeText(this@EditPatientActivity,"Please fill in the blank fields",Toast.LENGTH_SHORT).show()
        }
    }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@EditPatientActivity, AllPatientActivity::class.java)
        startActivity(redirectMainPage)
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@EditPatientActivity,AllPatientActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@EditPatientActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@EditPatientActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }


}

