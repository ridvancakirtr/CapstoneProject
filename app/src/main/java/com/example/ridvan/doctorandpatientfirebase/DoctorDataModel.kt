package com.example.ridvan.doctorandpatientfirebase

class DoctorDataModel {

    var doctor_name_surname : String? = null
    var doctor_gender : String? = null
    var doctor_area_expertise : String? = null
    var doctor_hospital_name : String? = null
    var doctor_office_phone : String? = null
    var doctor_mobile_phone : String? = null
    var doctor_email:String?=null
    var doctor_password:String?=null
    var level : String? = null
    var doctor_user_id: String? = null


    constructor()

    constructor(doctor_name_surname: String?, doctor_gender: String?, doctor_area_expertise: String?, doctor_hospital_name: String?, doctor_office_phone: String?, doctor_mobile_phone: String?, doctor_email: String?, doctor_password: String?, level: String?, doctor_user_id: String?) {
        this.doctor_name_surname = doctor_name_surname
        this.doctor_gender = doctor_gender
        this.doctor_area_expertise = doctor_area_expertise
        this.doctor_hospital_name = doctor_hospital_name
        this.doctor_office_phone = doctor_office_phone
        this.doctor_mobile_phone = doctor_mobile_phone
        this.doctor_email = doctor_email
        this.doctor_password = doctor_password
        this.level = level
        this.doctor_user_id = doctor_user_id
    }

    constructor(doctor_name_surname: String?, doctor_area_expertise: String?, doctor_office_phone: String?) {
        this.doctor_name_surname = doctor_name_surname
        this.doctor_area_expertise = doctor_area_expertise
        this.doctor_office_phone = doctor_office_phone
    }

}