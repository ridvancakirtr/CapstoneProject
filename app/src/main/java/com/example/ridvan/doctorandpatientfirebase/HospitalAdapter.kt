package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.hospital_list.view.*
import java.util.ArrayList
    /**
     * Kendi Oluşturacağımız view Holderın Adını Yazdık -> DoctorsViewHolder
     */
class HospitalAdapter(allDoctors: ArrayList<Hospital>) : RecyclerView.Adapter<HospitalAdapter.DoctorsViewHolder>() {
    var doctorsAll=allDoctors
    override fun getItemCount(): Int {
        //Adapter iik çalıştığı zaman buraya bakar
        return doctorsAll.size
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int):DoctorsViewHolder? {
        //viewHolder burada oluşturulacak (inflater işlemi)
        var inflater= LayoutInflater.from(parent?.context)
        var oneLineDoctor=inflater.inflate(R.layout.hospital_list,parent,false)

        return DoctorsViewHolder(oneLineDoctor)
    }
    override fun onBindViewHolder(holder: DoctorsViewHolder?, position: Int) {
        //benim oluşturduğum sınıfın nesnesini geriğe dönecek

        holder?.textViewHospitalName?.text=doctorsAll.get(position).hospital_name
        holder?.textViewHospitalCity?.text=doctorsAll.get(position).hospital_city
    }

    class DoctorsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //içindeki elemanlara buradan erişeceğim
        var oneLineDoctor=itemView as CardView
        var textViewHospitalName=oneLineDoctor.textViewHospitalName
        var textViewHospitalCity=oneLineDoctor.textViewHospitalCity

    }
}