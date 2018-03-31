package com.example.ridvan.doctorandpatientfirebase

class AdminDataModel {
    var admin_name_surname:String?=null
    var admin_phone:String?=null
    var admin_user_id: String? = null
    var level : String? = null



    constructor()

    constructor(admin_name_surname: String?, admin_phone: String?, admin_user_id: String?, level: String?) {
        this.admin_name_surname = admin_name_surname
        this.admin_phone = admin_phone
        this.admin_user_id = admin_user_id
        this.level = level
    }
}
