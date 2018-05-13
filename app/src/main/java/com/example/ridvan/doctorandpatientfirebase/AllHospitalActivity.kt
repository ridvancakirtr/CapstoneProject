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
import kotlinx.android.synthetic.main.activity_all_hospital.*
import java.util.ArrayList

class AllHospitalActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    lateinit var mPostReference: DatabaseReference
    var allDoctors= ArrayList<HospitalDataModel>()
    lateinit var hospitalName:String
    lateinit var hospitalID:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_hospital)
        var myAdapter=HospitalAdapter(allDoctors)
        val actionBar=supportActionBar
        actionBar!!.title="All Hospital Page"
        actionBar.setDisplayHomeAsUpEnabled(true)
        recycleListViewHospital.adapter=myAdapter

        var linearLayoutManeger=LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recycleListViewHospital.layoutManager=linearLayoutManeger

        readHospitalFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                hospitalName=allDoctors[viewHolder.adapterPosition].hospital_name
                hospitalID=allDoctors[viewHolder.adapterPosition].hospital_id
                ref.child("Hospital").child(allDoctors[viewHolder.adapterPosition].hospital_id).removeValue()
                Toast.makeText(this@AllHospitalActivity,"Removed ${allDoctors[viewHolder.adapterPosition].hospital_name}",Toast.LENGTH_LONG).show()
                hospitalResetForDoctor(hospitalID,hospitalName)
                allDoctors.clear()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewHospital)
    }

    private fun hospitalResetForDoctor(hospitalID: String,hospitalName: String) {
        ref.child("Doctors").orderByKey().addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot in p0.children){
                    val post = dataSnapshot.getValue(DoctorDataModel::class.java)
                    if (post!!.doctor_hospital_name==hospitalName){
                        ref.child("Doctors").child(post.doctor_user_id).child("doctor_hospital_name").setValue("Hospital Not Specified")
                    }
                }
            }

        })
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
            var intent = Intent(this@AllHospitalActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AllHospitalActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
        