package com.example.ridvan.doctorandpatientfirebase.Patient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.ridvan.doctorandpatientfirebase.R
import java.util.ArrayList

class PatientDetailDataActivity : AppCompatActivity() {
    var temp= ArrayList<JSONPatientData>()
    var ekg= ArrayList<JSONPatientData>()
    var pulse= ArrayList<JSONPatientData>()

    //var id:String?=null
    //var patient_id:String?=null

    var type:String?=null
    var data:String?=null
    var session_id:String?=null
    var created_at:String?=null
    var updated_at:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail_data)
        Toast.makeText(this@PatientDetailDataActivity, intent.getStringExtra("session_id"), Toast.LENGTH_SHORT).show()

        val url = "http://ciu.ysr.net.tr/sensor/1?session_id=${intent.getStringExtra("session_id")}"

        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->

                    for (item in 0 until response.length()){
                        type=response.getJSONObject(item).getString("type")
                        if(type=="1"){
                            data=response.getJSONObject(item).getString("data")
                            session_id=response.getJSONObject(item).getString("session_id")
                            created_at=response.getJSONObject(item).getString("created_at")
                            updated_at=response.getJSONObject(item).getString("updated_at")
                            temp.add(JSONPatientData(created_at,updated_at,session_id,type,data))
                        }

                        if(type=="2"){
                            data=response.getJSONObject(item).getString("data")
                            session_id=response.getJSONObject(item).getString("session_id")
                            created_at=response.getJSONObject(item).getString("created_at")
                            updated_at=response.getJSONObject(item).getString("updated_at")
                            ekg.add(JSONPatientData(created_at,updated_at,session_id,type,data))
                        }

                        if(type=="3"){
                            data=response.getJSONObject(item).getString("data")
                            session_id=response.getJSONObject(item).getString("session_id")
                            created_at=response.getJSONObject(item).getString("created_at")
                            updated_at=response.getJSONObject(item).getString("updated_at")
                            pulse.add(JSONPatientData(created_at,updated_at,session_id,type,data))
                        }
                    }

                },
                Response.ErrorListener { error ->
                    Log.e("ERROR",error.toString())
                }
        )

        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
}
