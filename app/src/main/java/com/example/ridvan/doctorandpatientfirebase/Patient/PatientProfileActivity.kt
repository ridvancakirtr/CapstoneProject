package com.example.ridvan.doctorandpatientfirebase.Patient

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import com.example.ridvan.doctorandpatientfirebase.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.District
import com.example.ridvan.doctorandpatientfirebase.PatientDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_patient_profile.*
import java.io.ByteArrayOutputStream
import java.util.*


class PatientProfileActivity : AppCompatActivity() {
    private var contentURI: Uri? = null
    var strogeRef= FirebaseStorage.getInstance().reference
    lateinit var bitmap: Bitmap
    var mAuth = FirebaseAuth.getInstance()
    var updatePatient= PatientDataModel()
    lateinit var ds:String
    lateinit var spinnerDistrict: Spinner
    var selectDistrict = arrayOf("Please Select District Specialty")
    val dataDistrict = ArrayList<String>(Arrays.asList(*selectDistrict))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_profile)
        userReadData()
        btnUpdatePatient.setOnClickListener {
            updateUserAllData()
        }
        patientProfilePicture.setOnClickListener {
            selectProfilePicture()
        }
    }

    private fun updateUserAllData() {
        if (updatePatientNameSurname.text.isNotEmpty() && updateAdress.text.isNotEmpty() && updatePatientMobilePhone.text.isNotEmpty() && spinnerDistrict.selectedItemPosition!=0){
            val gender = if(updatePatMale.isChecked){ "Male" }else { "FeMale" }
            val updatePatientProfile: HashMap<String, String> = HashMap()
            updatePatientProfile["patient_name_surname"] = updatePatientNameSurname.editableText.toString()
            updatePatientProfile["patient_gender"] = gender
            updatePatientProfile["district"] = spinnerDistrict.selectedItem.toString()
            updatePatientProfile["adress"] = updateAdress.text.toString()
            updatePatientProfile["patient_mobile_phone"] = updatePatientMobilePhone.text.toString()
            FirebaseDatabase.getInstance().reference
                    .child("Patient")
                    .child(mAuth.currentUser?.uid)
                    .updateChildren(updatePatientProfile as Map<String, String>?)
                    .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(this@PatientProfileActivity,"Profile Successfully Updated",Toast.LENGTH_SHORT).show()
                            redirectMainPage()
                        }else{
                            Toast.makeText(this@PatientProfileActivity,"ERROR Profile Not Updated",Toast.LENGTH_SHORT).show()
                        }
                    }
        }else{
            Toast.makeText(this@PatientProfileActivity,"Please fill in the blank fields",Toast.LENGTH_SHORT).show()
        }

    }

    private fun spinnerReadAndSelectDistrict(patientDistrict: String){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("District").orderByKey()

        var spinnerAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataDistrict)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for ((i, singleSnapshot) in p0!!.children.withIndex()){
                    var readUser=singleSnapshot.getValue(District::class.java)
                    dataDistrict.add(readUser?.district.toString())
                }
                spinnerDistrict.setSelection(spinnerAdapter.getPosition(patientDistrict))
            }
        })

        spinnerDistrict=findViewById(R.id.spinnerDistrict)
        spinnerDistrict.adapter= spinnerAdapter
        spinnerDistrict.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Log.e("Data", spinnerDistrict.getItemIdAtPosition(position).toString())

            }
        }
    }

    private fun userReadData() {
        var ref = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            var query=ref.child("Patient").orderByKey().equalTo(user.uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }
                override fun onDataChange(p0: DataSnapshot?) {
                    var ds:String
                    for (singleSnapshot in p0!!.children){
                        var readUser = singleSnapshot?.getValue(PatientDataModel::class.java)

                        updateAdress.setText(readUser?.adress)
                        updatePatientMobilePhone.setText(readUser?.patient_mobile_phone)
                        updatePatientNameSurname.setText(readUser?.patient_name_surname)
                        //if (readUser?.patient_profile_picture=="null"){
                            Picasso.with(this@PatientProfileActivity)
                                    .load(readUser?.patient_profile_picture)
                                    .into(patientProfilePicture)
                        //}
                        ds = readUser?.district!!
                        spinnerReadAndSelectDistrict(ds)
                        if(readUser?.patient_gender.toString()=="Male"){
                            updatePatMale.isChecked=true
                        }else{
                            updatePatFeMale.isChecked=true
                        }
                    }
                }
            })
        }

    }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@PatientProfileActivity, PatientActivity::class.java)
        startActivity(redirectMainPage)
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
            patientProfilePicture.setImageURI(contentURI)
            pictureCompressi(contentURI!!)
        }
    }

    inner class pictureCompressionBack : AsyncTask<Uri, Double, ByteArray?>(){

        override fun doInBackground(vararg p0: Uri?): ByteArray? {
            bitmap = MediaStore.Images.Media.getBitmap(this@PatientProfileActivity.contentResolver,p0[0])
            var imageByte:ByteArray?=null
            for (i in 1..5){
                imageByte=ConvertBitmapByte(bitmap,100/i)
                publishProgress(imageByte!!.size.toDouble()) // Main Thread with Worker Thread arasında köprü olur sonucu ->  onProgressUpdate()
            }
            return imageByte
        }

        private fun ConvertBitmapByte(bitmap: Bitmap?, i: Int): ByteArray? {
            var stream= ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG,i,stream)
            return stream.toByteArray()
        }

        override fun onPostExecute(result: ByteArray?) {
            super.onPostExecute(result)
            uploadImagesToFirebase(result)


        }

        private fun uploadImagesToFirebase(result: ByteArray?) {
            var refStore=FirebaseStorage.getInstance().reference
                    .child("images/patients/"+mAuth.currentUser?.uid+"/profilePicture")
            refStore.putBytes(result!!)
                    .addOnSuccessListener { p0 ->
                        var firebaseUri=p0?.downloadUrl
                        Toast.makeText(this@PatientProfileActivity, "Updated Profile Picture",Toast.LENGTH_SHORT).show()
                        FirebaseDatabase.getInstance().reference.child("Patient")
                                .child(mAuth.currentUser?.uid)
                                .child("patient_profile_picture")
                                .setValue(firebaseUri.toString())

                    }.addOnFailureListener {
                        Toast.makeText(this@PatientProfileActivity, "Hata Oluştu!", Toast.LENGTH_SHORT).show()
                    }
        }

    }
}
