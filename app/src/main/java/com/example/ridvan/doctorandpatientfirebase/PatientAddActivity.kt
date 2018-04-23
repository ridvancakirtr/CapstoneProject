package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_patient_profile.*
import kotlinx.android.synthetic.main.activity_patient_add.*
import java.util.*

class PatientAddActivity : AppCompatActivity() {
    var mAuth = FirebaseAuth.getInstance()
    var ref= FirebaseDatabase.getInstance().reference
    var addPatient= PatientDataModel()
    var district=District()
    lateinit var  spinerDistrict:Spinner

    var selectDistrict = arrayOf("Please Select District")
    val dataDistrict = ArrayList<String>(Arrays.asList(*selectDistrict))

    var gender=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_add)
        //newDistrictAdd()
        DistrictAddSpinner()
        readHospital()

        AddPatient.setOnClickListener {
            if (
                    patientNameSurname.text.isNotEmpty() &&
                    Adress.text.isNotEmpty() &&
                    patientMobilePhone.text.isNotEmpty() &&
                    PatEmail.text.isNotEmpty() &&
                    updatePatPassword.text.isNotEmpty() &&
                    PatPasswordRe.text.isNotEmpty()
            ){
                if (spinerDistrict.selectedItemPosition!=0){

                    if(updatePatPassword.text.toString() == PatPasswordRe.text.toString()){
                        if(PatMale.isChecked){
                            gender="Male"
                            newUserAdd(PatEmail.text.toString(),updatePatPassword.text.toString())
                        }else if (PatFeMale.isChecked){
                            gender="FeMale"
                            newUserAdd(PatEmail.text.toString(),updatePatPassword.text.toString())
                        }else{
                            Toast.makeText(this@PatientAddActivity, "Please Select Gender", Toast.LENGTH_SHORT).show()
                        }
                    }
                    else{
                        Toast.makeText(this@PatientAddActivity, "Does Not Match Password", Toast.LENGTH_SHORT).show()
                    }

                }else{
                    Toast.makeText(this@PatientAddActivity, "Please Select District", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(this@PatientAddActivity, "Please Fill in the Blank Fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun newUserAdd(email:String,password:String){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                        addPatient.patient_name_surname=patientNameSurname.editableText.toString()
                        addPatient.patient_profile_picture="http://ishowmy.support/img/user-icon-360x360.jpg"
                        addPatient.patient_gender=gender
                        addPatient.district=spinerDistrict.selectedItem.toString()
                        addPatient.adress=Adress.text.toString()
                        addPatient.patient_mobile_phone=patientMobilePhone.text.toString()
                        addPatient.patient_email=PatEmail.text.toString()
                        addPatient.patient_password=updatePatPassword.text.toString()
                        addPatient.level = "2"
                        addPatient.patient_user_id=mAuth.currentUser?.uid
                        FirebaseDatabase.getInstance().reference
                                .child("Patient")
                                .child(mAuth.currentUser?.uid)
                                .setValue(addPatient)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this@PatientAddActivity, "Patient is Successfully Added", Toast.LENGTH_SHORT).show()
                                        redirectMainPage()
                                    } else {
                                        Toast.makeText(this@PatientAddActivity, "Patient is Not Add" + task.exception?.message, Toast.LENGTH_SHORT).show()
                                    }

                                }
                    } else {
                        Toast.makeText(this@PatientAddActivity,task.exception?.message, Toast.LENGTH_SHORT).show()
                    }

                })
    }

    private fun redirectMainPage(){
        val redirectMainPage= Intent(this@PatientAddActivity, AdminActivity::class.java)
        startActivity(redirectMainPage)
    }

    private fun DistrictAddSpinner() {
        spinerDistrict=findViewById(R.id.spinnerDistrict)
        spinerDistrict.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataDistrict)
        spinerDistrict.onItemSelectedListener=object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {


            }

        }
    }

    private fun readHospital(){
        val ref=FirebaseDatabase.getInstance().reference
        val query=ref.child("District")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                for (singleSnapshot in p0!!.children){
                    val readUser=singleSnapshot.getValue(PatientDataModel::class.java)
                    dataDistrict.add(readUser?.district!!)
                }
            }

        })
    }


}
