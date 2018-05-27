package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_all_doctor.*
import java.util.ArrayList

class AllDoctorActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    lateinit var mPostReference: DatabaseReference
    var allDoctors= ArrayList<DoctorDataModel>()
    lateinit var doctorID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_doctor)
        val actionBar=supportActionBar
        actionBar!!.title="All Doctor"
        actionBar.setDisplayHomeAsUpEnabled(true)

        var myAdapter=DoctorAdapter(allDoctors)
        recycleListViewDoctors.adapter=myAdapter

        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewDoctors.layoutManager=linearLayoutManeger

        readHospitalFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                doctorID= allDoctors[viewHolder.adapterPosition].doctor_user_id!!
                ref.child("Doctors").child(doctorID).removeValue()
                Toast.makeText(this@AllDoctorActivity,"Removed ${allDoctors[viewHolder.adapterPosition].doctor_hospital_name}",Toast.LENGTH_LONG).show()
                patientResetDoctor(doctorID)
                allDoctors.clear()
                recycleListViewDoctors.clearOnChildAttachStateChangeListeners()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewDoctors)

    }

    private fun patientResetDoctor(doctorID: String) {
        ref.child("Patient").orderByKey().addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {

                    val post = postSnapshot.getValue(PatientDataModel::class.java)
                    if (post!!.doctor_user_id==doctorID){
                        ref.child("Patient").child(post!!.patient_user_id).child("doctor_user_id").setValue("0")
                    }
                }
            }

        })
    }

    private fun readHospitalFirebase() {
        mPostReference = FirebaseDatabase.getInstance().reference
        val query = mPostReference.child("Doctors").orderByKey()

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(DoctorDataModel::class.java)
                    allDoctors.add(DoctorDataModel(post!!.doctor_name_surname, post.doctor_profile_pictures,post.doctor_area_expertise,post.doctor_hospital_name,post.doctor_user_id))

                }

                val doctorAdapter = DoctorAdapter(allDoctors)
                recycleListViewDoctors.adapter = doctorAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@AllDoctorActivity,DoctorManagementActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@AllDoctorActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AllDoctorActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
