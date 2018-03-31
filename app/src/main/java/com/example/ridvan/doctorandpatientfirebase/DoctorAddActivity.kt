package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_doctor.*
import java.util.*
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class DoctorAddActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var ref= FirebaseDatabase.getInstance().reference

    var selectHostipal = arrayOf("Please Select Hospital")
    val dataHospital = ArrayList<String>(Arrays.asList(*selectHostipal))

    var selectExpertise = arrayOf("Please Select Doctor Specialty")
    val dataAreaExpertise = ArrayList<String>(Arrays.asList(*selectExpertise))

    lateinit var spinnerDoctorSpecialty: Spinner
    lateinit var spinnerHospital: Spinner

    var addDoctor = DoctorDataModel()
    var addHospital= Hospital()
    var addAreaExpertise= AreaExpertise()
    var gender:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_doctor)

        readHospital()
        readAreaExpertise()
        HospitalAddSpinner()
        DoctorSpecialtyAddSpinner()
        btnAddDoctor.setOnClickListener {
            if (
                    editTextDoctorNameSurname.text.isNotEmpty() &&
                    editTextOfficePhone.text.isNotEmpty() &&
                    editTextMobilePhone.text.isNotEmpty() &&
                    editTextPatientEmail.text.isNotEmpty() &&
                    editTextPassword.text.isNotEmpty() &&
                    editTextPasswordRe.text.isNotEmpty()

            ){
                    if (spinnerHospital.selectedItemPosition !==0 && spinnerDoctorSpecialty.selectedItemPosition !==0){
                        if(editTextPassword.text.toString() == editTextPasswordRe.text.toString()){
                            if(radioMale.isChecked){
                                gender="Male"
                                newUserAdd(editTextPatientEmail.text.toString(),editTextPassword.text.toString())
                            }else if (radioFeMale.isChecked){
                                gender="FeMale"
                                newUserAdd(editTextPatientEmail.text.toString(),editTextPassword.text.toString())
                            }else{
                                Toast.makeText(this@DoctorAddActivity, "Please Select Gender", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else{
                            Toast.makeText(this@DoctorAddActivity, "Does Not Match Password", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(this@DoctorAddActivity, "Please Select Hospital and Area of Expertise", Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this@DoctorAddActivity,"Please Fill in the Blank Fields", Toast.LENGTH_SHORT).show()
            }
        }
        }

    private fun HospitalAddSpinner() {
        spinnerHospital=findViewById(R.id.spinnerHospitalName)
        spinnerHospital.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataHospital)
        spinnerHospital.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }

        }
    }

    private fun DoctorSpecialtyAddSpinner() {
        spinnerDoctorSpecialty=findViewById(R.id.spinnerAreaExpertise)
        spinnerDoctorSpecialty.adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataAreaExpertise)
        spinnerDoctorSpecialty.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

            }

        }
    }

    private fun newUserAdd(email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        addDoctor.doctor_name_surname=editTextDoctorNameSurname.text.toString()
                        addDoctor.doctor_gender=gender
                        addDoctor.doctor_area_expertise=spinnerAreaExpertise.selectedItem.toString()
                        addDoctor.doctor_hospital_name=spinnerHospitalName.selectedItem.toString()
                        addDoctor.doctor_office_phone=editTextOfficePhone.text.toString()
                        addDoctor.doctor_mobile_phone=editTextMobilePhone.text.toString()
                        addDoctor.doctor_email=editTextPatientEmail.text.toString()
                        addDoctor.doctor_password=editTextPassword.text.toString()
                        addDoctor.level = "1"
                        addDoctor.doctor_user_id=mAuth.currentUser?.uid
                        FirebaseDatabase.getInstance().reference
                                .child("Doctors")
                                .child(mAuth.currentUser?.uid)
                                .setValue(addDoctor)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@DoctorAddActivity, "Doctor is Successfully Added", Toast.LENGTH_SHORT).show()
                                        redirectMainPage()
                                    } else {
                                        Toast.makeText(this@DoctorAddActivity, "Doctor is Not Add" + task.exception?.message, Toast.LENGTH_SHORT).show()
                                    }

                                }
                    } else {
                        Toast.makeText(this@DoctorAddActivity,task.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                })
        }

    private fun redirectMainPage(){
        var redirectMainPage= Intent(this@DoctorAddActivity, AdminActivity::class.java)
        startActivity(redirectMainPage)
    }

    private fun readHospital(){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("Hospital")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for (singleSnapshot in p0!!.children){
                    var readUser=singleSnapshot.getValue(Hospital::class.java)
                    dataHospital.add(readUser?.hospital_name.toString()+"  |  "+readUser?.hospital_city)
                }
            }

        })
    }

    private fun readAreaExpertise(){
        var ref=FirebaseDatabase.getInstance().reference
        var query=ref.child("AreaExpertise")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for (singleSnapshot in p0!!.children){
                    var readUser=singleSnapshot.getValue(AreaExpertise::class.java)
                    dataAreaExpertise.add(readUser?.AreaExpertise.toString())
                }
            }

        })
    }

}
