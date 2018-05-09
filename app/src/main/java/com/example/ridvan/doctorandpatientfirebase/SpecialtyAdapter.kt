package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.all_district_list.view.*


class SpecialtyAdapter(allSpecialty: ArrayList<SpecialtyBranches>) : RecyclerView.Adapter<SpecialtyAdapter.SpecialtyAdapterViewHolder>() {
    var allSpecialtys=allSpecialty
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SpecialtyAdapterViewHolder {
        var inflater= LayoutInflater.from(parent?.context)
        var oneLineDistrict=inflater.inflate(R.layout.all_district_list,parent,false)
        return SpecialtyAdapterViewHolder(oneLineDistrict)
    }

    override fun getItemCount(): Int {
        return allSpecialtys.size
    }

    override fun onBindViewHolder(holder: SpecialtyAdapterViewHolder?, position: Int) {
        holder?.SpecialtyName?.text=allSpecialtys[position].SpecialtyBranches
    }

    class SpecialtyAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var oneLineSpecialty=itemView as CardView
        var SpecialtyName=oneLineSpecialty.districtName
    }
}