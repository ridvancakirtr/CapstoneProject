package com.example.ridvan.doctorandpatientfirebase

class HospitalDataModel{
    lateinit var hospital_name: String
    lateinit var hospital_city: String
    lateinit var hospital_id: String



    constructor(hospital_name: String, hospital_city: String, hospital_id: String) {
        this.hospital_name = hospital_name
        this.hospital_city = hospital_city
        this.hospital_id = hospital_id
    }

    constructor()
}