package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.hospital_list.view.*
import java.util.ArrayList
    /**
     * Kendi Oluşturacağımız view Holderın Adını Yazdık -> HospitalsViewHolder
     */
class HospitalAdapter(allHospitals: ArrayList<HospitalDataModel>) : RecyclerView.Adapter<HospitalAdapter.HospitalsViewHolder>() {
    var hospitalAll=allHospitals
    override fun getItemCount(): Int {
        //Adapter iik çalıştığı zaman buraya bakar
        return hospitalAll.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalsViewHolder {
        //viewHolder burada oluşturulacak (inflater işlemi)
        var inflater= LayoutInflater.from(parent?.context)
        var oneLineHospital=inflater.inflate(R.layout.hospital_list,parent,false)

        return HospitalsViewHolder(oneLineHospital)
    }
    override fun onBindViewHolder(holder: HospitalsViewHolder, position: Int) {
        //benim oluşturduğum sınıfın nesnesini geriğe dönecek

        holder?.textViewHospitalName?.text=hospitalAll.get(position).hospital_name
        holder?.textViewHospitalCity?.text=hospitalAll.get(position).hospital_city
    }

    class HospitalsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //içindeki elemanlara buradan erişeceğim
        var oneLineHospital=itemView as CardView
        var textViewHospitalName=oneLineHospital.dataDate
        var textViewHospitalCity=oneLineHospital.textViewHospitalCity

    }
}