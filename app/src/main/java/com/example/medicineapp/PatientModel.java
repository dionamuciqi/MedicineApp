package com.example.medicineapp;

public class PatientModel {
    private String fullname;
    private String diagnostic;
    private int id;
    private int userId;

    public PatientModel(String fullname, String diagnostic, int id, int userId) {
        this.fullname = fullname;
        this.diagnostic = diagnostic;
        this.id = id;
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
