package com.example.ridvan.doctorandpatientfirebase.Patient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.ridvan.doctorandpatientfirebase.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.util.ArrayList
import com.example.ridvan.doctorandpatientfirebase.HourAxisValueFormatter

class PatientDetailDataActivity : AppCompatActivity() {
    //var id:String?=null
    //var patient_id:String?=null

    var type:String?=null
    var data:String?=null
    var session_id:String?=null
    var created_at:String?=null
    var updated_at:String?=null



    private val entriesEkg = ArrayList<Entry>()
    private val entriesTemp = ArrayList<Entry>()
    private val entriesPulse = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_detail_data)
        Toast.makeText(this@PatientDetailDataActivity, intent.getStringExtra("session_id"), Toast.LENGTH_SHORT).show()

        val dataSetEkg = LineDataSet(entriesEkg, "Ekg")
        val dataSetTemp = LineDataSet(entriesTemp, "Temperature")
        val dataSetPulse = LineDataSet(entriesPulse, "Pulse")

        val lineDataEkg = LineData(dataSetEkg)
        val lineDataTemp = LineData(dataSetTemp)
        val lineDataPulse = LineData(dataSetPulse)

        val chartEkg = findViewById<LineChart>(R.id.chartEkg)
        val chartTemp = findViewById<LineChart>(R.id.chartTemp)
        val chartPulse = findViewById<LineChart>(R.id.chartPulse)

        /*
         * Chart kütüphanesinde bug olduğu için boşken hata veriyor.
         */
        dataSetEkg.addEntry(Entry(0.0F, 0.0F))
        dataSetTemp.addEntry(Entry(0.0F, 0.0F))
        dataSetPulse.addEntry(Entry(0.0F, 0.0F))

        dataSetEkg.notifyDataSetChanged()
        dataSetPulse.notifyDataSetChanged()
        dataSetEkg.notifyDataSetChanged()

        chartEkg.data = lineDataEkg
        chartTemp.data = lineDataTemp
        chartPulse.data = lineDataPulse

        val url = "http://ciu.ysr.net.tr/sensor/1?session_id=${intent.getStringExtra("session_id")}"

        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->

                    for (item in 0 until response.length()){
                        type=response.getJSONObject(item).getString("type")
                        if(type=="1"){
                            val data=response.getJSONObject(item).getString("data")
                            session_id=response.getJSONObject(item).getString("session_id")
                            val created_at=response.getJSONObject(item).getString("created_at")
                            updated_at=response.getJSONObject(item).getString("updated_at")

                            dataSetTemp.addEntry(Entry(lineDataTemp.entryCount.toFloat(), data.toFloat()))
                            dataSetTemp.notifyDataSetChanged()
                            lineDataTemp.notifyDataChanged()
                            chartTemp.notifyDataSetChanged()
                            chartTemp.invalidate()

                            val axisFormatter = HourAxisValueFormatter(dateToTimestamp(created_at))
                            chartTemp.xAxis.valueFormatter = axisFormatter

                        }

                        if(type=="2"){
                            val data=response.getJSONObject(item).getString("data")
                            session_id=response.getJSONObject(item).getString("session_id")
                            val created_at=response.getJSONObject(item).getString("created_at")
                            updated_at=response.getJSONObject(item).getString("updated_at")

                            dataSetEkg.addEntry(Entry(lineDataEkg.entryCount.toFloat(), data.toFloat()))
                            val axisFormatter = HourAxisValueFormatter(dateToTimestamp(created_at))
                            chartEkg.xAxis.valueFormatter = axisFormatter
                            dataSetEkg.notifyDataSetChanged()
                            lineDataEkg.notifyDataChanged()
                            chartEkg.notifyDataSetChanged()
                            chartEkg.invalidate()


                        }

                        if(type=="3"){
                            val data=response.getJSONObject(item).getString("data")
                            session_id=response.getJSONObject(item).getString("session_id")
                            val created_at=response.getJSONObject(item).getString("created_at")
                            updated_at=response.getJSONObject(item).getString("updated_at")

                            dataSetPulse.addEntry(Entry(lineDataPulse.entryCount.toFloat(), data.toFloat()))
                            val axisFormatter = HourAxisValueFormatter(dateToTimestamp(created_at))
                            chartPulse.xAxis.valueFormatter = axisFormatter
                            dataSetPulse.notifyDataSetChanged()
                            lineDataPulse.notifyDataChanged()
                            chartPulse.notifyDataSetChanged()
                            chartPulse.invalidate()
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

    private fun dateToTimestamp(s: String): Long {
        return java.sql.Timestamp.valueOf(s).time
    }
}
