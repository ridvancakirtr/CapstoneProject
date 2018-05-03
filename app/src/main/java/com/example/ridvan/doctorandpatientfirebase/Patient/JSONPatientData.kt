package com.example.ridvan.doctorandpatientfirebase.Patient

class JSONPatientData {
    var patient_id: String? =null
    var start_date: String? =null
    var end_date: String? =null
    var session_id: String? =null

    constructor(patient_id: String?, start_date: String?, end_date: String?, session_id: String?) {
        this.patient_id = patient_id
        this.start_date = start_date
        this.end_date = end_date
        this.session_id = session_id
    }
}