package com.example.ridvan.doctorandpatientfirebase

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.all_district_list.view.*

class DistrictAdapter(allDistrict: ArrayList<District>) : RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder>() {
    var allDİstricts=allDistrict;
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DistrictViewHolder {
        var inflater=LayoutInflater.from(parent?.context)
        var oneLineDistrict=inflater.inflate(R.layout.all_district_list,parent,false)
        return DistrictViewHolder(oneLineDistrict)
    }

    override fun getItemCount(): Int {
        return allDİstricts.size
    }

    override fun onBindViewHolder(holder: DistrictViewHolder?, position: Int) {
        holder?.districtName?.text=allDİstricts[position].district
    }


    class DistrictViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var oneLineDistrict=itemView as CardView
        var districtName=oneLineDistrict.districtName
    }
}