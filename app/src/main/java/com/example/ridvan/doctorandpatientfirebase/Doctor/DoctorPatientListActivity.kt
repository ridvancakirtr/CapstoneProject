package com.example.ridvan.doctorandpatientfirebase.Doctor

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.example.ridvan.doctorandpatientfirebase.LoginActivity
import com.example.ridvan.doctorandpatientfirebase.PatientDataModel
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_doctor_patient_list.*
import java.util.ArrayList

class DoctorPatientListActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var mAuth=FirebaseAuth.getInstance().currentUser
    var allPatient= ArrayList<PatientDataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_patient_list)
        val actionBar=supportActionBar
        actionBar!!.title="All My Patients"
        actionBar.setDisplayHomeAsUpEnabled(true)

        var linearLayoutManeger = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recycleListViewPatientList.layoutManager = linearLayoutManeger

        val query = ref.child("Patient").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(PatientDataModel::class.java)
                    if (post!!.doctor_user_id==mAuth?.uid){
                        allPatient.add(PatientDataModel(post!!.patient_name_surname, post.patient_profile_picture, post.district,post.patient_mobile_phone,post.patient_user_id,post.doctor_user_id))
                    }
                }

                var myAdapter=DoctorPatientAdapter(allPatient)
                recycleListViewPatientList.adapter=myAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@DoctorPatientListActivity , LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@DoctorPatientListActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
