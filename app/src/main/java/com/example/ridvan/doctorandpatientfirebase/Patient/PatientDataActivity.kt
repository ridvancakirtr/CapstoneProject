package com.example.ridvan.doctorandpatientfirebase.Patient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.NonNull
import android.support.design.internal.SnackbarContentLayout
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.ridvan.doctorandpatientfirebase.LoginActivity
import com.example.ridvan.doctorandpatientfirebase.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_patient_data.*
import java.text.SimpleDateFormat
import java.util.*

class PatientDataActivity : AppCompatActivity() {
    var ref= FirebaseDatabase.getInstance().reference
    var mAuth= FirebaseAuth.getInstance().currentUser
    var allData= ArrayList<JSONPatientData>()

    var patient_id:String?=null
    var start_date:String?=null
    var end_date:String?=null
    var session_id:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_data)
        val actionBar=supportActionBar
        actionBar!!.title="My Data Date & Time"
        actionBar.setDisplayHomeAsUpEnabled(true)
        var linearLayoutManeger= LinearLayoutManager(this, LinearLayout.VERTICAL,false)
        recycleListViewPatientData.layoutManager=linearLayoutManeger

        var dataList = object : PatientDataAdapter(allData) {
            override fun onBindViewHolder(holder: PatientDataAdapter.MyDataHolder?, position: Int) {
                holder?.startDate?.text= dataDate[position].start_date
                holder?.endDate?.text= dataDate[position].end_date
                holder?.oneLineData?.setOnClickListener {
                    var intent=Intent(this@PatientDataActivity,PatientDetailDataActivity::class.java)
                    intent.putExtra("session_id",allData[position].session_id)
                    startActivity(intent)
                }
            }

        }
        progressBar.visibility=View.INVISIBLE
        val url = "http://ciu.ysr.net.tr/user/1"

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    patient_id = response.getString("id")

                    for (item in 0 until response.getJSONArray("sessions").length()){
                        var patient=response.getJSONArray("sessions").getJSONObject(item)
                        start_date=patient?.getString("start_date")
                        end_date=patient?.getString("end_date")
                        session_id=patient?.getString("session_id")
                        allData.add(JSONPatientData(patient_id,getDateTime(start_date!!),getDateTime(end_date!!),session_id))

                        //Log.e("DATA", allData.toString())
                    }

                    recycleListViewPatientData.adapter = dataList

                },
                Response.ErrorListener { error ->
                    Log.e("ERROR",error.toString())
                }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        progressBar.visibility=View.VISIBLE
    }

    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
            val netDate = Date(s.toLong()*1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu2,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        R.id.exitToolbar -> {
            var intent = Intent(this@PatientDataActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@PatientDataActivity,"Log Out",Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        R.id.liveData -> {
            var intent = Intent(this@PatientDataActivity, PatientLineChartActivity::class.java)
            startActivity(intent)
            finish()
            Toast.makeText(this@PatientDataActivity,"Live Data",Toast.LENGTH_SHORT).show()
            super.onOptionsItemSelected(item)
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
