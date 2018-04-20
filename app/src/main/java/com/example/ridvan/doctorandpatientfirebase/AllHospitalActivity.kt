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
import kotlinx.android.synthetic.main.activity_all_hospital.*
import java.util.ArrayList

class AllHospitalActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    lateinit var mPostReference: DatabaseReference
    var allDoctors= ArrayList<HospitalDataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_hospital)
        var myAdapter=HospitalAdapter(allDoctors)
        recycleListViewHospital.adapter=myAdapter

        var linearLayoutManeger=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recycleListViewHospital.layoutManager=linearLayoutManeger

        readHospitalFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ref.child("Hospital").child(allDoctors[viewHolder.adapterPosition].hospital_id).removeValue()
                Toast.makeText(this@AllHospitalActivity,"Removed ${allDoctors[viewHolder.adapterPosition].hospital_name}",Toast.LENGTH_LONG).show()
                allDoctors.clear()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewHospital)
    }

    private fun readHospitalFirebase() {
        mPostReference = FirebaseDatabase.getInstance().reference
        val query = mPostReference.child("Hospital").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(HospitalDataModel::class.java)
                    allDoctors.add(HospitalDataModel(post!!.hospital_name, post.hospital_city,post.hospital_id))

                }

                val hospitalAdapter = HospitalAdapter(allDoctors)
                recycleListViewHospital.adapter = hospitalAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
}
        