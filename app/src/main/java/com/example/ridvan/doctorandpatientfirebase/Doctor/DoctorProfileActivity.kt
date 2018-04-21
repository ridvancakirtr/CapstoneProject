package com.example.ridvan.doctorandpatientfirebase.Doctor

import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.DoctorDataModel
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_doctor_profile.*
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DoctorProfileActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    lateinit var bitmap:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
        readDoctorData()
        doctorProfilePicture.setImageURI(mAuth.currentUser?.photoUrl)
        btnSaveAll.setOnClickListener {
            updateDoctors()
        }

        doctorProfilePicture.setOnClickListener {
            updateProfilePicture()

        }

    }

    private fun updateProfilePicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (data != null) {
                val contentURI = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,contentURI)
                    doctorProfilePicture!!.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(this@DoctorProfileActivity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateDoctors() {
        if (doctorNameSurname.text.isNotEmpty() && doctorOfficePhone.text.isNotEmpty() && doctorMobilePhone.text.isNotEmpty() ){
            val updateDoctorProfile: HashMap<String, String> = HashMap()
            updateDoctorProfile["doctor_name_surname"]=doctorNameSurname.text.toString()
            updateDoctorProfile["doctor_mobile_phone"]=doctorMobilePhone.text.toString()
            updateDoctorProfile["doctor_office_phone"]=doctorOfficePhone.text.toString()
            FirebaseDatabase.getInstance().reference
                    .child("Doctors")
                    .child(mAuth.currentUser!!.uid)
                    .updateChildren(updateDoctorProfile as Map<String, String>?)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(this@DoctorProfileActivity,"Profile Successfully Updated", Toast.LENGTH_SHORT).show()
                            redirectMainPage()
                        }else{
                            Toast.makeText(this@DoctorProfileActivity,"ERROR Profile Not Updated", Toast.LENGTH_SHORT).show()
                        }
                    }
        }else{
            Toast.makeText(this@DoctorProfileActivity,"Please fill in the blank fields", Toast.LENGTH_SHORT).show()
        }

    }

    private fun readDoctorData() {
        var ref = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            var query=ref.child("Doctors").orderByKey().equalTo(user.uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }
                override fun onDataChange(p0: DataSnapshot?) {
                    for (singleSnapshot in p0!!.children){
                        var readUser = singleSnapshot?.getValue(DoctorDataModel::class.java)
                        doctorNameSurname.setText(readUser!!.doctor_name_surname)
                        doctorAreaExpertise.setText(readUser!!.doctor_area_expertise)
                        doctorEmail.setText(readUser!!.doctor_email)
                        doctorHospitalName.setText(readUser!!.doctor_hospital_name)
                        doctorGender.setText(readUser!!.doctor_gender)
                        doctorMobilePhone.setText(readUser!!.doctor_mobile_phone)
                        doctorOfficePhone.setText(readUser!!.doctor_office_phone)
                    }
                }
            })
        }
    }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@DoctorProfileActivity, DoctorActivity::class.java)
        startActivity(redirectMainPage)
    }

}
