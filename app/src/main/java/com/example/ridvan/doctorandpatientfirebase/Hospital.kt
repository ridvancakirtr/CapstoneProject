package com.example.ridvan.doctorandpatientfirebase

class Hospital {
    lateinit var hospital_name: String
    lateinit var hospital_city: String

    constructor() {}

    constructor(hospital_name: String, hospital_city: String) {
        this.hospital_name = hospital_name
        this.hospital_city = hospital_city
    }
}
