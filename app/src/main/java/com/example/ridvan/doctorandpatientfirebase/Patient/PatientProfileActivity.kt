package com.example.ridvan.doctorandpatientfirebase.Patient

import android.content.Intent
import com.example.ridvan.doctorandpatientfirebase.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.District
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_add_doctor.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_patient_add.*
import java.util.*
import com.google.firebase.auth.EmailAuthProvider


class PatientProfileActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var ref= FirebaseDatabase.getInstance().reference
    var updatePatient= PatientDataModel()
    lateinit var ds:String
    lateinit var spinnerDistrict: Spinner
    //var gender=""
    var selectDistrict = arrayOf("Please Select District Specialty")
    val dataDistrict = ArrayList<String>(Arrays.asList(*selectDistrict))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        //userReadData()
        //readDistrict()
        spinnerAddDistrict()
        btnUpdatePatient.setOnClickListener {
            val credential = EmailAuthProvider.getCredential(editTextPatientEmail.text.toString(),updatePatientPassword.text.toString())
            mAuth.currentUser?.reauthenticate(credential)!!
                    .addOnCompleteListener {  }
        }
    }

    private fun spinnerAddDistrict(){
        spinnerDistrict=findViewById(R.id.spinnerDistrict)
        spinnerDistrict.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataDistrict)
        spinnerDistrict.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            Log.e("Data", spinnerDistrict.getItemIdAtPosition(position).toString())

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

    private fun userReadData() {
        var ref = FirebaseDatabase.getInstance().reference
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            PatEmail.setText(user.email.toString())
            var query=ref.child("Patient").orderByKey().equalTo(user.uid)
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError?) {

                }
                override fun onDataChange(p0: DataSnapshot?) {
                    var ds:String
                    for (singleSnapshot in p0!!.children){
                    var readUser = singleSnapshot?.getValue(PatientDataModel::class.java)
                   Adress.setText(readUser?.adress)
                   patientMobilePhone.setText(readUser?.patient_mobile_phone)
                   updatePatientNameSurname.setText(readUser?.patient_name_surname)
                        ds= readUser?.district!!
                        if(readUser?.patient_gender.toString()!=="Male"){
                            PatMale.isChecked=true
                        }else{
                            PatFeMale.isChecked=true
                        }
                }
            }
            })
        }

    }
    /*
    private fun updatePatient(email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        updatePatient.patient_name_surname=updatePatientNameSurname.text.toString()
                        updatePatient.patient_gender=gender
                        updatePatient.district=spinnerDistrict.selectedItem.toString()
                        updatePatient.adress=updateAdress.text.toString()
                        updatePatient.patient_mobile_phone=updatePatientMobilePhone.text.toString()
                        ref.child("Patient").orderByKey().equalTo(mAuth.uid)
                                ref.setValue(updatePatient)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@PatientProfileActivity, "Patient is Successfully Added", Toast.LENGTH_SHORT).show()
                                        redirectMainPage()
                                    } else {
                                        Toast.makeText(this@PatientProfileActivity, "Patient is Not Add" + task.exception?.message, Toast.LENGTH_SHORT).show()
                                    }

                                }
                    } else {
                        Toast.makeText(this@PatientProfileActivity,task.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }*/
    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@PatientProfileActivity, PatientActivity::class.java)
        startActivity(redirectMainPage)
    }

}
