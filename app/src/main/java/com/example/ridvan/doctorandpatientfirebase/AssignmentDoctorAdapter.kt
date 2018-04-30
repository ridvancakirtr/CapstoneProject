package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.doctor_list.view.*

abstract class AssignmentDoctorAdapter(allDoctors: ArrayList<DoctorDataModel>) : RecyclerView.Adapter<AssignmentDoctorAdapter.DoctorsViewHolder>() {
    var doctorAll=allDoctors
    override fun getItemCount(): Int {
        //Adapter iik çalıştığı zaman buraya bakar
        return doctorAll.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsViewHolder {
        //viewHolder burada oluşturulacak (inflater işlemi)
        var inflater= LayoutInflater.from(parent?.context)
        var oneLineHospital=inflater.inflate(R.layout.doctor_list,parent,false)

        return DoctorsViewHolder(oneLineHospital)
    }

    class DoctorsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //içindeki elemanlara buradan erişeceğim
        var oneLineDoctor=itemView as CardView
        var textViewDoctorNameSurname=oneLineDoctor.textViewDoctorNameSurname
        var textViewAreaExpertise=oneLineDoctor.textViewAreaExpertise
        var textViewHospitalName=oneLineDoctor.dataDate

    }
}