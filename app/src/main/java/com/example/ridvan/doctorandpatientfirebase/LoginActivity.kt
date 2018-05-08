package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.Doctor.DoctorActivity
import com.example.ridvan.doctorandpatientfirebase.Patient.PatientActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    var mAuth= FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar!!.title = "Login Page"
        btnSignIn.setOnClickListener{
            if (editText_UserName.text.isNotEmpty() && editText_Password.text.isNotEmpty()) {
                mAuth.signInWithEmailAndPassword(editText_UserName.text.toString(), editText_Password.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                userInformation()
                            }else{
                                Toast.makeText(this@LoginActivity, "Username or Password Not Correct", Toast.LENGTH_SHORT).show()
                            }
                        }
            } else {
                Toast.makeText(this, "Please Enter Username and Password", Toast.LENGTH_SHORT).show()
            }
        }
    }
    fun userInformation(){
        var referance= FirebaseDatabase.getInstance().reference
        var user= FirebaseAuth.getInstance().currentUser

        //query
        var queryAdministrator=referance.child("Administrator")
                .orderByKey()
                .equalTo(user?.uid)
        queryAdministrator.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }
            override fun onDataChange(p0: DataSnapshot?) {
                for (DataSnapshot in p0!!.children){
                    var readUser=DataSnapshot.getValue(DoctorDataModel::class.java)
                    if (readUser?.level=="0"){
                        Toast.makeText(this@LoginActivity, "Administrator Logged in", Toast.LENGTH_SHORT).show()
                        var doctorIntent= Intent(this@LoginActivity,AdminActivity::class.java)
                        startActivity(doctorIntent)
                        finish()
                    }
                    else
                        FirebaseAuth.getInstance().signOut()
                }
            }
        })

        var queryUsers=referance.child("Doctors")
                .orderByKey()
                .equalTo(user?.uid)
        queryUsers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }
            override fun onDataChange(p0: DataSnapshot?) {
                for (DataSnapshot in p0!!.children){
                    var readUser=DataSnapshot.getValue(DoctorDataModel::class.java)
                    if (readUser?.level=="1"){
                        Toast.makeText(this@LoginActivity, "Admin Logged in", Toast.LENGTH_SHORT).show()
                        var doctorIntent= Intent(this@LoginActivity, DoctorActivity::class.java)
                        startActivity(doctorIntent)
                        finish()
                    }
                    else
                        FirebaseAuth.getInstance().signOut()
                }
            }
        })

        var queryPatient=referance.child("Patient")
                .orderByKey()
                .equalTo(user?.uid)
        queryPatient.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }
            override fun onDataChange(p0: DataSnapshot?) {
                for (DataSnapshot in p0!!.children){
                    var readUser=DataSnapshot.getValue(DoctorDataModel::class.java)
                    if (readUser?.level=="2"){
                        Toast.makeText(this@LoginActivity, "Patient Logged in", Toast.LENGTH_SHORT).show()
                        var patientIntent= Intent(this@LoginActivity, PatientActivity::class.java)
                        startActivity(patientIntent)
                        finish()
                    }
                    else
                        FirebaseAuth.getInstance().signOut()
                }
            }
        })
    }
}
