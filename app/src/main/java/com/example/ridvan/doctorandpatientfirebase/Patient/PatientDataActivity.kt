package com.example.ridvan.doctorandpatientfirebase.Patient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.ridvan.doctorandpatientfirebase.PatientDataTempEkgPulse
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_patient_data.*

class PatientDataActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var mAuth= FirebaseAuth.getInstance().currentUser
    var allData= ArrayList<PatientDataTempEkgPulse>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_data)

        /*
        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewPatientData.layoutManager=linearLayoutManeger

        var dataList = object : PatientDataAdapter(allData) {
            override fun onBindViewHolder(holder: PatientDataAdapter.MyDataHolder?, position: Int) {
                holder?.date?.text= dataDate[position].datadate
                holder?.oneLineData?.setOnClickListener {
                    Toast.makeText(this@PatientDataActivity,"Tıklandı"+position,Toast.LENGTH_SHORT).show()
                }
            }

        }

        var query=ref.child("PatientDataTempEkgPulse").child(mAuth!!.uid).orderByKey()
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }
            override fun onDataChange(p0: DataSnapshot) {
                for (postSnapshot in p0.children) {
                    val post=postSnapshot.getValue(PatientDataTempEkgPulse::class.java)
                    allData.add(PatientDataTempEkgPulse(post!!.temperature,post.ekg,post.pulse,post.datadate,post.id))
                }
                recycleListViewPatientData.adapter = dataList
            }

        })
        */
        patientJsonData()

    }

    private fun patientJsonData() {
        val url = "http://ciu.ysr.net.tr/sensor/1"
                             //JsonArrayRequest -> Json ın array oldugunu belirtiyoruz
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->//Response ile Array i çekiyoruz
                    var object_1=response.getJSONObject(0)//Array in birinci indisindeki değerleri çekiyoruz
                    var id=object_1?.getString("id")
                    var patient_id=object_1?.getString("patient_id")
                    var type=object_1?.getString("type")
                    var data=object_1?.getString("data")
                    var session_id=object_1?.getString("session_id")
                    var created_at=object_1?.getString("created_at")
                    var updated_at=object_1?.getString("updated_at")
                },
                Response.ErrorListener { error ->
                    Log.e("ERROR",error.toString())
                }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

}
