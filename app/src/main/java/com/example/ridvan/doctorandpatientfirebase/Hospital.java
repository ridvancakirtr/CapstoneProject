package com.example.ridvan.doctorandpatientfirebase;

public class Hospital {
    String hospital_name;
    String hospital_city;

    public Hospital() {
    }

    public Hospital(String hospital_name, String hospital_city) {
        this.hospital_name = hospital_name;
        this.hospital_city = hospital_city;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_city() {
        return hospital_city;
    }

    public void setHospital_city(String hospital_city) {
        this.hospital_city = hospital_city;
    }
}
