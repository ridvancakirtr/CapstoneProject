package com.example.ridvan.doctorandpatientfirebase.Patient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ridvan.doctorandpatientfirebase.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.auth.FirebaseAuth
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException
import com.github.nkzawa.emitter.Emitter
import org.json.*
import java.util.ArrayList

class PatientLineChartActivity : AppCompatActivity() {
    private var mSocket: Socket? = null
    val entries = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.line_chart)


        val dataSet = LineDataSet(entries, "Ekg")
        val lineData = LineData(dataSet)
        val chart = findViewById<LineChart>(R.id.chart)
        /*
         * Chart kütüphanesinde sorun bug olduğu için boşken hata veriyor.
         */
        dataSet.addEntry(Entry(0.0F, 0.0F))
        dataSet.notifyDataSetChanged()
        lineData.notifyDataChanged()

        chart.data = lineData

        run {
            try {
                mSocket = IO.socket("http://46.45.162.122:8000/tty")
                println("Connected")
            } catch (e: URISyntaxException) {
                println("Not Connected")
            }
        }

        mSocket!!.connect()
        mSocket!!.on("message") { args ->
            this@PatientLineChartActivity.runOnUiThread(java.lang.Runnable {
                val receivedJson: JSONObject

                try {
                    receivedJson = JSONObject(args[0].toString())
                } catch (e: JSONException)  {
                    return@Runnable
                }

                if (receivedJson.has("Ekg")) {
                    dataSet.addEntry(Entry(lineData.entryCount.toFloat(), receivedJson.getInt("Ekg").toFloat()))
                }

                dataSet.notifyDataSetChanged()
                lineData.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()
            })
        }

    }
}
