package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.patient_list.view.*
import java.util.ArrayList
    /**
     * Kendi Oluşturacağımız view Holderın Adını Yazdık -> HospitalsViewHolder
     */
class PatientAdapter(allPatients: ArrayList<PatientDataModel>) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {
    var patientAll=allPatients
    override fun getItemCount(): Int {
        //Adapter iik çalıştığı zaman buraya bakar
        return patientAll.size
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):PatientViewHolder? {
        //viewHolder burada oluşturulacak (inflater işlemi)
        var inflater= LayoutInflater.from(parent?.context)
        var oneLineHospital=inflater.inflate(R.layout.patient_list,parent,false)

        return PatientViewHolder(oneLineHospital)
    }
    override fun onBindViewHolder(holder: PatientViewHolder?, position: Int) {
        //benim oluşturduğum sınıfın nesnesini geriğe dönecek

        holder?.textViewPatientNameSurname?.text=patientAll.get(position).patient_name_surname
        holder?.textViewDistrict?.text=patientAll.get(position).district
    }

    class PatientViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //içindeki elemanlara buradan erişeceğim
        var oneLinePatient=itemView as CardView
        var textViewPatientNameSurname=oneLinePatient.textViewPatientName
        var textViewDistrict=oneLinePatient.textViewDistrict

    }
}