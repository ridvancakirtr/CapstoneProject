package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_assignment_result.*
import kotlinx.android.synthetic.main.doctor_list.view.*
import java.util.ArrayList

class AssignmentResultActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var allDoctors= ArrayList<DoctorDataModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_result)
        val actionBar=supportActionBar
        actionBar!!.title="All Doctors"
        actionBar.setDisplayHomeAsUpEnabled(true)

        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewAssginmentResult.layoutManager=linearLayoutManeger

        var doctorList = object : AssignmentDoctorAdapter(allDoctors) {
            override fun onBindViewHolder(holder: AssignmentDoctorAdapter.DoctorsViewHolder, position: Int) {
                //benim oluşturduğum sınıfın nesnesini geriğe dönecek
                holder?.textViewDoctorNameSurname?.text=allDoctors.get(position).doctor_name_surname
                holder?.textViewAreaExpertise?.text=allDoctors.get(position).doctor_area_expertise
                Picasso.with(holder.itemView.context).load(allDoctors.get(position).doctor_profile_pictures)
                        .into(holder.itemView.doctorPictures)
                holder?.textViewHospitalName?.text=allDoctors.get(position).doctor_hospital_name

                holder.oneLineDoctor.setOnClickListener {
                    Toast.makeText(holder.itemView.context, allDoctors[position].doctor_name_surname,Toast.LENGTH_SHORT).show()

                    var intent= Intent(holder.itemView.context, AssignmentResultPatientActivity::class.java)
                    intent.putExtra("DoctorUserID",allDoctors.get(position).doctor_user_id!!)
                    holder.itemView.context.startActivity(intent)
                }

            }
        }


        val query = ref.child("Doctors").orderByKey()

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {

                    val post = postSnapshot.getValue(DoctorDataModel::class.java)
                    allDoctors.add(DoctorDataModel(post!!.doctor_name_surname, post.doctor_profile_pictures,post.doctor_area_expertise,post.doctor_hospital_name,post.doctor_user_id))

                }

                recycleListViewAssginmentResult.adapter = doctorList
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
            var intent = Intent(this@AssignmentResultActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@AssignmentResultActivity, "Log Out", Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)

        }
    }
}