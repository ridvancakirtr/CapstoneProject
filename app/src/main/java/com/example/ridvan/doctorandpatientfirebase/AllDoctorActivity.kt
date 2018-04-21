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
import kotlinx.android.synthetic.main.activity_all_doctor.*
import java.util.ArrayList

class AllDoctorActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    lateinit var mPostReference: DatabaseReference
    var allDoctors= ArrayList<DoctorDataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_doctor)

        var myAdapter=DoctorAdapter(allDoctors)
        recycleListViewDoctors.adapter=myAdapter

        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewDoctors.layoutManager=linearLayoutManeger

        readHospitalFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ref.child("Doctors").child(allDoctors[viewHolder.adapterPosition].doctor_user_id).removeValue()
                Toast.makeText(this@AllDoctorActivity,"Removed ${allDoctors[viewHolder.adapterPosition].doctor_hospital_name}",Toast.LENGTH_LONG).show()
                allDoctors.clear()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewDoctors)

    }

    private fun readHospitalFirebase() {
        mPostReference = FirebaseDatabase.getInstance().reference
        val query = mPostReference.child("Doctors").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(DoctorDataModel::class.java)
                    allDoctors.add(DoctorDataModel(post!!.doctor_name_surname, post.doctor_area_expertise,post.doctor_user_id,post.doctor_profile_pictures))

                }

                val doctorAdapter = DoctorAdapter(allDoctors)
                recycleListViewDoctors.adapter = doctorAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}
