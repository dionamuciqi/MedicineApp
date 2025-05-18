package com.example.medicineapp;

public class PatientModel {
    String fullname,diagnostic;

    int id;


    public PatientModel(String fullname, String diagnostic, int id) {
        this.fullname = fullname;
        this.diagnostic = diagnostic;
        this.id = id;
    }


    public String getfullname() {
        return fullname;
    }

    public void setfullname(String fullname) {
        this.fullname = fullname;
    }

    public String getdiagnostic() {
        return diagnostic;
    }

    public void setDescription(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}

