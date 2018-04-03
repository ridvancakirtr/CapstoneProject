package com.example.ridvan.doctorandpatientfirebase.Patient

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.ridvan.doctorandpatientfirebase.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import java.net.URISyntaxException
import org.json.*
import java.util.ArrayList

class PatientLineChartActivity : AppCompatActivity() {
    private var mSocket: Socket? = null

    private val entriesEkg = ArrayList<Entry>()
    private val entriesTemp = ArrayList<Entry>()
    private val entriesPulse = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.line_chart)

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

        run {
            try {
                mSocket = IO.socket("http://46.45.162.122:8000/tty")
            } catch (e: URISyntaxException) { }
        }

        mSocket!!.connect()
        mSocket!!.on("message") { args ->
            this@PatientLineChartActivity.runOnUiThread(java.lang.Runnable {
                val receivedJson: JSONObject

                try {
                    receivedJson = JSONObject(args[0].toString())
                    println(receivedJson.toString())
                } catch (e: JSONException)  {
                    return@Runnable
                }

                if (receivedJson.has("Ekg")) {
                    dataSetEkg.addEntry(Entry(lineDataEkg.entryCount.toFloat(), receivedJson.getInt("Ekg").toFloat()))
                    dataSetEkg.notifyDataSetChanged()
                    lineDataEkg.notifyDataChanged()
                    chartEkg.notifyDataSetChanged()
                    chartEkg.invalidate()
                }

                if (receivedJson.has("Temp")) {
                    dataSetTemp.addEntry(Entry(lineDataTemp.entryCount.toFloat(), receivedJson.getInt("Temp").toFloat()))
                    dataSetTemp.notifyDataSetChanged()
                    lineDataTemp.notifyDataChanged()
                    chartTemp.notifyDataSetChanged()
                    chartTemp.invalidate()
                }

                if (receivedJson.has("Puls")) {
                    dataSetPulse.addEntry(Entry(lineDataPulse.entryCount.toFloat(), receivedJson.getInt("Puls").toFloat()))
                    dataSetPulse.notifyDataSetChanged()
                    lineDataPulse.notifyDataChanged()
                    chartPulse.notifyDataSetChanged()
                    chartPulse.invalidate()
                }
            })
        }

    }
}
