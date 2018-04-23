package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_edit_doctor.*
import java.util.*

class EditDoctorActivity : AppCompatActivity() {
    var mPostReference=FirebaseDatabase.getInstance().reference
    var strogeRef= FirebaseStorage.getInstance().reference
    lateinit var spinnerAreaExpertise: Spinner
    var selectAreaExpertise = arrayOf("Please Select AreaExpertise")
    val dataAreaExpertise = ArrayList<String>(Arrays.asList(*selectAreaExpertise))

    lateinit var spinnerHospital: Spinner
    var selectHospital = arrayOf("Please Select Hospital")
    val dataHospital = ArrayList<String>(Arrays.asList(*selectHospital))

    var userId: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_doctor)
        userId=intent.getStringExtra("userId")
        userReadData()
        btnEditDoctor.setOnClickListener {
            updateDoctor()
        }

        editDoctorPictures.setOnClickListener {
            resetProfilePicture()
        }
    }

    private fun resetProfilePicture() {
        mPostReference.child("Doctors").child(userId).child("doctor_profile_pictures").setValue("http://ishowmy.support/img/user-icon-360x360.jpg")
                .addOnCompleteListener {task ->
                    if (task.isSuccessful){
                        Picasso.with(this@EditDoctorActivity)
                                .load("http://ishowmy.support/img/user-icon-360x360.jpg")
                                .into(editDoctorPictures)
                        Toast.makeText(this@EditDoctorActivity, "Doctor Profile is Successfully Reset", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@EditDoctorActivity, "Doctor Profile NOT Reset", Toast.LENGTH_SHORT).show()
                    }
                }
    }

    private fun updateDoctor() {
        if (editDoctorNameSurname.text.isNotEmpty() &&
            editAreaExpertise.selectedItemPosition!=0 &&
            editHospitalName.selectedItemPosition!=0 &&
            editOfficePhone.text.isNotEmpty() &&
            editMobilePhone.text.isNotEmpty() &&
            editDoctorEmail.text.isNotEmpty() &&
            editPassword.text.isNotEmpty()
            ){
            val gender = if(radioMale.isChecked){ "Male" }else { "FeMale" }
                    val updateDoctorProfile: HashMap<String, String> = HashMap()
                    updateDoctorProfile["doctor_name_surname"] = editDoctorNameSurname.editableText.toString()
                    updateDoctorProfile["doctor_gender"] = gender
                    updateDoctorProfile["doctor_area_expertise"] = editAreaExpertise.selectedItem.toString()
                    updateDoctorProfile["doctor_hospital_name"] = editHospitalName.selectedItem.toString()
                    updateDoctorProfile["doctor_mobile_phone"] = editMobilePhone.text.toString()
                    updateDoctorProfile["doctor_office_phone"] = editOfficePhone.text.toString()

                    FirebaseDatabase.getInstance().reference
                            .child("Doctors")
                            .child(userId)
                            .updateChildren(updateDoctorProfile as Map<String, String>?)
                            .addOnCompleteListener {task->
                                if (task.isSuccessful){
                                    Toast.makeText(this@EditDoctorActivity,"Profile Successfully Updated",Toast.LENGTH_SHORT).show()
                                    redirectMainPage()
                                }else{
                                    Toast.makeText(this@EditDoctorActivity,"ERROR Profile Not Updated",Toast.LENGTH_SHORT).show()
                                }
                            }
        }else{
            Toast.makeText(this@EditDoctorActivity,"Please fill in the blank fields",Toast.LENGTH_SHORT).show()
        }
    }

    private fun userReadData() {
        if (userId != null) {
            var query=mPostReference.child("Doctors").orderByKey().equalTo(userId)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }
                override fun onDataChange(p0: DataSnapshot?) {
                    for (singleSnapshot in p0!!.children){
                        var readUser = singleSnapshot?.getValue(DoctorDataModel::class.java)
                        editDoctorNameSurname.setText(readUser!!.doctor_name_surname)
                        spinnerReadAndSelectAreaExpertise(readUser.doctor_area_expertise!!)
                        editDoctorEmail.setText(readUser.doctor_email)
                        spinnerReadAndSelectHospital(readUser.doctor_hospital_name!!)
                        editPassword.setText(readUser.doctor_password)
                        if(readUser.doctor_gender.toString()=="Male"){
                            radioMale.isChecked=true
                        }else{
                            radioFeMale.isChecked=true
                        }
                        editMobilePhone.setText(readUser.doctor_mobile_phone)
                        editOfficePhone.setText(readUser.doctor_office_phone)
                        Picasso.with(this@EditDoctorActivity).load(readUser.doctor_profile_pictures).into(editDoctorPictures)
                    }
                }
            })
        }
    }

    private fun spinnerReadAndSelectAreaExpertise(areaexpertise: String){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("AreaExpertise").orderByKey()

        var spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataAreaExpertise)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for ((i, singleSnapshot) in p0!!.children.withIndex()){
                    var readUser=singleSnapshot.getValue(AreaExpertise::class.java)
                    dataAreaExpertise.add(readUser?.AreaExpertise.toString())
                }
                spinnerAreaExpertise.setSelection(spinnerAdapter.getPosition(areaexpertise))
            }
        })

        spinnerAreaExpertise=findViewById(R.id.editAreaExpertise)
        spinnerAreaExpertise.adapter= spinnerAdapter
        spinnerAreaExpertise.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("Data", spinnerAreaExpertise.getItemIdAtPosition(position).toString())

            }
        }
    }

    private fun spinnerReadAndSelectHospital(hospital: String){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("Hospital").orderByKey()

        var spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataHospital)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for ((i, singleSnapshot) in p0!!.children.withIndex()){
                    var readUser=singleSnapshot.getValue(Hospital::class.java)
                    dataHospital.add(readUser?.hospital_name!!)
                }
                spinnerHospital.setSelection(spinnerAdapter.getPosition(hospital))
            }
        })

        spinnerHospital=findViewById(R.id.editHospitalName)
        spinnerHospital.adapter= spinnerAdapter
        spinnerHospital.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("Data", spinnerHospital.getItemIdAtPosition(position).toString())

            }
        }
    }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@EditDoctorActivity, AllDoctorActivity::class.java)
        startActivity(redirectMainPage)
    }
}
