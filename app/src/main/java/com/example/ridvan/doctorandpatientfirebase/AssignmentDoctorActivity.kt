package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_assignment_doctor.*
import kotlinx.android.synthetic.main.doctor_list.view.*
import java.util.ArrayList

class AssignmentDoctorActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var allDoctors= ArrayList<DoctorDataModel>()
    var patientUserID: String? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_doctor)

        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewAssginmentDoctors.layoutManager= linearLayoutManeger as RecyclerView.LayoutManager?
        patientUserID=intent.getStringExtra("userId")
        var doctorList = object : AssignmentDoctorAdapter(allDoctors) {
            override fun onBindViewHolder(holder: AssignmentDoctorAdapter.DoctorsViewHolder, position: Int) {
                //benim oluşturduğum sınıfın nesnesini geriğe dönecek
                holder?.textViewDoctorNameSurname?.text=allDoctors.get(position).doctor_name_surname
                holder?.textViewAreaExpertise?.text=allDoctors.get(position).doctor_area_expertise
                Picasso.with(holder.itemView.context).load(allDoctors.get(position).doctor_profile_pictures)
                        .into(holder.itemView.doctorPictures)
                holder?.textViewHospitalName?.text=allDoctors.get(position).doctor_hospital_name

                holder.oneLineDoctor.setOnClickListener {
                    Toast.makeText(holder.itemView.context,"Choosing Doctor is "+allDoctors.get(position).doctor_name_surname+" for Assignment",Toast.LENGTH_LONG).show()
                    var intent= Intent(holder.itemView.context, EditPatientActivity::class.java)
                    ref.child("Patient").child(patientUserID).child("doctor_user_id").setValue(allDoctors.get(position).doctor_user_id!!)
                    intent.putExtra("DoctorUserID",allDoctors.get(position).doctor_user_id!!)
                    intent.putExtra("userId",patientUserID)
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

                recycleListViewAssginmentDoctors.adapter = doctorList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Hata", "loadPost:onCancelled", databaseError.toException())
            }
        })

    }
}