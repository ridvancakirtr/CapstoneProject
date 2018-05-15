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
import kotlinx.android.synthetic.main.activity_all_patient.*
import java.util.ArrayList

class AllPatientActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    lateinit var mPostReference: DatabaseReference
    var allPatient= ArrayList<PatientDataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_patient)
        val actionBar=supportActionBar
        actionBar!!.title="All Patient Page"
        actionBar.setDisplayHomeAsUpEnabled(true)

        var myAdapter=PatientAdapter(allPatient)
        recycleListViewPatient.adapter=myAdapter

        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewPatient.layoutManager=linearLayoutManeger

        readPatientFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ref.child("Patient").child(allPatient[viewHolder.adapterPosition].patient_user_id).removeValue()
                Toast.makeText(this@AllPatientActivity,"Removed ${allPatient[viewHolder.adapterPosition].patient_name_surname}",Toast.LENGTH_LONG).show()
                allPatient.clear()
                recycleListViewPatient.clearOnChildAttachStateChangeListeners()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewPatient)

    }

    private fun readPatientFirebase() {
        mPostReference = FirebaseDatabase.getInstance().reference
        val query = mPostReference.child("Patient").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(PatientDataModel::class.java)
                    if (post!!.doctor_user_id=="0"){
                        allPatient.add(PatientDataModel(post!!.patient_name_surname, post.patient_profile_picture, post.district,post.patient_mobile_phone,post.patient_email,post.patient_password,post.patient_user_id,"no doctor was assigned"))
                    }else{
                        allPatient.add(PatientDataModel(post!!.patient_name_surname, post.patient_profile_picture, post.district,post.patient_mobile_phone,post.patient_email,post.patient_password,post.patient_user_id,"doctor assigned"))
                    }

                }

                val doctorAdapter = PatientAdapter(allPatient)
                recycleListViewPatient.adapter = doctorAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        var intent = Intent(this@AllPatientActivity,PatientManagementActivity::class.java)
        startActivity(intent)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@AllPatientActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AllPatientActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
