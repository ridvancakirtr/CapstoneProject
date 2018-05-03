package com.example.ridvan.doctorandpatientfirebase

import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.doctor_list.view.*
import java.util.ArrayList
    /**
     * Kendi Oluşturacağımız view Holderın Adını Yazdık -> HospitalsViewHolder
     */
class DoctorAdapter(allDoctors: ArrayList<DoctorDataModel>) : RecyclerView.Adapter<DoctorAdapter.DoctorsViewHolder>() {
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
    override fun onBindViewHolder(holder: DoctorsViewHolder, position: Int) {
        //benim oluşturduğum sınıfın nesnesini geriğe dönecek
        holder?.textViewDoctorNameSurname?.text=doctorAll.get(position).doctor_name_surname
        holder?.textViewAreaExpertise?.text=doctorAll.get(position).doctor_area_expertise
        Picasso.with(holder.itemView.context).load(doctorAll.get(position).doctor_profile_pictures)
                .into(holder.itemView.doctorPictures)
        holder?.textViewHospitalName?.text=doctorAll.get(position).doctor_hospital_name

        holder.oneLineDoctor.setOnClickListener {
            Toast.makeText(holder.itemView.context,"Tıkladın : "+doctorAll.get(position).doctor_name_surname,Toast.LENGTH_SHORT).show()
            var intent=Intent(holder.itemView.context, EditDoctorActivity::class.java)
            intent.putExtra("userId",doctorAll.get(position).doctor_user_id!!)
            holder.itemView.context.startActivity(intent)
        }

    }

    class DoctorsViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        //içindeki elemanlara buradan erişeceğim
        var oneLineDoctor=itemView as CardView
        var textViewDoctorNameSurname=oneLineDoctor.textViewDoctorNameSurname
        var textViewAreaExpertise=oneLineDoctor.textViewAreaExpertise
        var textViewHospitalName=oneLineDoctor.startDate

    }
}