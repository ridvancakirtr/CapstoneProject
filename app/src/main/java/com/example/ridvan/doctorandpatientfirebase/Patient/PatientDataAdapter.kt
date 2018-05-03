package com.example.ridvan.doctorandpatientfirebase.Patient


import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ridvan.doctorandpatientfirebase.R
import kotlinx.android.synthetic.main.patient_data_list.view.*

abstract class PatientDataAdapter(data: ArrayList<JSONPatientData>): RecyclerView.Adapter<PatientDataAdapter.MyDataHolder>() {
    var dataDate=data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyDataHolder {
        var inflater= LayoutInflater.from(parent?.context)
        var oneLineData=inflater.inflate(R.layout.patient_data_list,parent,false)
        return MyDataHolder(oneLineData)
    }

    override fun getItemCount(): Int {
        return  dataDate.size
    }


    class MyDataHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        var oneLineData=itemView as CardView
        var startDate= oneLineData.startDate!!
        var endDate= oneLineData.endDate!!
    }
}