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
import com.example.ridvan.doctorandpatientfirebase.PatientDataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_my_profile.*
import java.util.*


class PatientProfileActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var updatePatient= PatientDataModel()
    lateinit var ds:String
    lateinit var spinnerDistrict: Spinner
    var selectDistrict = arrayOf("Please Select District Specialty")
    val dataDistrict = ArrayList<String>(Arrays.asList(*selectDistrict))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        userReadData()
        btnUpdatePatient.setOnClickListener {
            updateUserAllData()
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

}
