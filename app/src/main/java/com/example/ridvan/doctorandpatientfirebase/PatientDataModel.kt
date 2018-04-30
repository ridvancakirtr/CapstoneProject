package com.example.ridvan.doctorandpatientfirebase

import com.google.firebase.database.Exclude
import java.util.*


class PatientDataModel {
    var patient_name_surname:String?=null
    var patient_profile_picture:String?=null
    var patient_gender:String?=null
    var district:String?=null
    var adress:String?=null
    var patient_mobile_phone:String?=null
    var patient_email:String?=null
    var patient_password:String?=null
    var level : String? = null
    var patient_user_id: String? = null
    var doctor_user_id: String? = null

    constructor()

    constructor(patient_name_surname: String?, patient_profile_picture: String?, patient_gender: String?, district: String?, adress: String?, patient_mobile_phone: String?, patient_email: String?, patient_password: String?, level: String?, patient_user_id: String?) {
        this.patient_name_surname = patient_name_surname
        this.patient_profile_picture = patient_profile_picture
        this.patient_gender = patient_gender
        this.district = district
        this.adress = adress
        this.patient_mobile_phone = patient_mobile_phone
        this.patient_email = patient_email
        this.patient_password = patient_password
        this.level = level
        this.patient_user_id = patient_user_id
    }


    constructor(patient_name_surname: String?, patient_profile_picture: String?, district: String?, patient_mobile_phone: String?, patient_email: String?, patient_password: String?, patient_user_id: String?, doctor_user_id: String?) {
        this.patient_name_surname = patient_name_surname
        this.patient_profile_picture = patient_profile_picture
        this.district = district
        this.patient_mobile_phone = patient_mobile_phone
        this.patient_email = patient_email
        this.patient_password = patient_password
        this.patient_user_id = patient_user_id
        this.doctor_user_id= doctor_user_id
    }

    constructor(patient_name_surname: String?, patient_profile_picture: String?, district: String?, patient_mobile_phone: String?, patient_user_id: String?, doctor_user_id: String?) {
        this.patient_name_surname = patient_name_surname
        this.patient_profile_picture = patient_profile_picture
        this.district = district
        this.patient_mobile_phone = patient_mobile_phone
        this.patient_user_id = patient_user_id
        this.doctor_user_id = doctor_user_id
    }


}

