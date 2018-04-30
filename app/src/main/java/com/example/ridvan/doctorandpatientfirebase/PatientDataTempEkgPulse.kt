package com.example.ridvan.doctorandpatientfirebase

class PatientDataTempEkgPulse {

    var temperature:String?=null
    var ekg:String?=null
    var pulse:String?=null
    var datadate:String?=null
    var id:String?=null

    constructor()

    constructor(temperature: String?, ekg: String?, pulse: String?, datadate: String?, id: String?) {
        this.temperature = temperature
        this.ekg = ekg
        this.pulse = pulse
        this.datadate = datadate
        this.id = id
    }


}