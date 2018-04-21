package com.example.ridvan.doctorandpatientfirebase.Doctor

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.DoctorDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_doctor_profile.*
import android.provider.MediaStore
import android.view.View
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.*


class DoctorProfileActivity : AppCompatActivity() {
    private var contentURI: Uri? = null
    var strogeRef=FirebaseStorage.getInstance().reference
    var mAuth = FirebaseAuth.getInstance()
    lateinit var bitmap:Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_profile)
        readDoctorData()
        btnSaveAll.setOnClickListener {
            updateDoctors()
        }

        doctorProfilePicture.setOnClickListener {
                selectProfilePicture()
        }

    }

    private fun pictureCompressi(contentURI: Uri) {
        var compress=pictureCompressionBack()
        compress.execute(contentURI)

    }

    /*
    private fun Permissions() {
        var per= arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(this@DoctorProfileActivity,per[0])==PackageManager.PERMISSION_GRANTED){
            requestPermiss=true
        }else{
            ActivityCompat.requestPermissions(this@DoctorProfileActivity,per,150)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==150){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                updateProfilePicture()
            }else{
                Toast.makeText(this@DoctorProfileActivity,"İzin ver", Toast.LENGTH_SHORT).show()
            }
        }

    }
    */

    private fun selectProfilePicture() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode== Activity.RESULT_OK && data!=null) {
            contentURI = data.data
            doctorProfilePicture.setImageURI(contentURI)
            pictureCompressi(contentURI!!)
        }
    }

    inner class pictureCompressionBack : AsyncTask<Uri,Double,ByteArray?>(){

        override fun doInBackground(vararg p0: Uri?): ByteArray? {
            bitmap = MediaStore.Images.Media.getBitmap(this@DoctorProfileActivity.contentResolver,p0[0])
            var imageByte:ByteArray?=null
            for (i in 1..5){
                imageByte=ConvertBitmapByte(bitmap,100/i)
                publishProgress(imageByte!!.size.toDouble()) // Main Thread with Worker Thread arasında köprü olur sonucu ->  onProgressUpdate()
            }
            return imageByte
        }

        private fun ConvertBitmapByte(bitmap: Bitmap?, i: Int): ByteArray? {
            var stream=ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG,i,stream)
            return stream.toByteArray()
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            uploadImagesToFirebase(result)


        }

        private fun uploadImagesToFirebase(result: ByteArray?) {
            var refStore=FirebaseStorage.getInstance().reference
                    .child("images/users/"+mAuth.currentUser?.uid+"/profilePicture")
            refStore.putBytes(result!!)
                    .addOnSuccessListener { p0 ->
                        var firebaseUri=p0?.downloadUrl
                        Toast.makeText(this@DoctorProfileActivity, "Updated Profile Picture",Toast.LENGTH_SHORT).show()
                    FirebaseDatabase.getInstance().reference.child("Doctors")
                            .child(mAuth.currentUser?.uid)
                            .child("doctor_profile_pictures")
                            .setValue(firebaseUri.toString())

                    }.addOnFailureListener {
                        Toast.makeText(this@DoctorProfileActivity, "Hata Oluştu!", Toast.LENGTH_SHORT).show()
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
                        Picasso.with(this@DoctorProfileActivity).load(readUser.doctor_profile_pictures).into(doctorProfilePicture)
                    }
                }
            })
        }
    }

}
