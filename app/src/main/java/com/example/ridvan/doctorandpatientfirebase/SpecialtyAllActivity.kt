package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_specialty_all.*
import java.util.ArrayList

class SpecialtyAllActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var allSpecialty= ArrayList<SpecialtyBranches>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specialty_all)
        val actionBar=supportActionBar
        actionBar!!.title="Specialty All Page"
        actionBar.setDisplayHomeAsUpEnabled(true)

        recycleListViewSpecialty.layoutManager= LinearLayoutManager(this, LinearLayout.VERTICAL,false)

        readSpecialtyFirebase()

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ref.child("SpecialtyBranches").child(allSpecialty[viewHolder.adapterPosition].SpecialtyBranches_id).removeValue()
                Toast.makeText(this@SpecialtyAllActivity,"Removed ${allSpecialty[viewHolder.adapterPosition].SpecialtyBranches}",Toast.LENGTH_LONG).show()
                allSpecialty.clear()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recycleListViewSpecialty)

    }

    private fun readSpecialtyFirebase() {
        var query=ref.child("SpecialtyBranches").orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children){
                    var post=postSnapshot.getValue(SpecialtyBranches::class.java)
                    allSpecialty.add(SpecialtyBranches(post?.SpecialtyBranches,post?.SpecialtyBranches_id))
                }
                recycleListViewSpecialty.adapter=SpecialtyAdapter(allSpecialty)
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
            var intent = Intent(this@SpecialtyAllActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@SpecialtyAllActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}
