package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
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
                    allPatient.add(PatientDataModel(post!!.patient_name_surname, post.patient_profile_picture, post.district,post.patient_mobile_phone,post.patient_user_id))

                }

                val doctorAdapter = PatientAdapter(allPatient)
                recycleListViewPatient.adapter = doctorAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}
