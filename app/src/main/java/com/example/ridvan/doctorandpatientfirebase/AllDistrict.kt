package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_all_district.*
import java.util.ArrayList

class AllDistrict : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var allDistrict= ArrayList<District>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_district)


        recycleListViewDistrict.layoutManager=LinearLayoutManager(this,LinearLayout.VERTICAL,false)

        readDistrictFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ref.child("District").child(allDistrict[viewHolder.adapterPosition].district_id).removeValue()
                Toast.makeText(this@AllDistrict,"Removed ${allDistrict[viewHolder.adapterPosition].district}",Toast.LENGTH_LONG).show()
                allDistrict.clear()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewDistrict)
    }

    private fun readDistrictFirebase() {
        var query=ref.child("District").orderByKey()
        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children){
                    var post=postSnapshot.getValue(District::class.java)
                    allDistrict.add(District(post?.district,post?.district_id))
                }
                recycleListViewDistrict.adapter=DistrictAdapter(allDistrict)
            }

        })
    }
}
