package com.example.medicineapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medicine.db";
    private static final int DATABASE_VERSION = 2;

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fullName TEXT," +
                "gender TEXT," +
                "userEmail TEXT UNIQUE," +
                "mobile TEXT," +
                "address TEXT," +
                "password TEXT)");

        db.execSQL("CREATE TABLE patients(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER," +
                "fullname TEXT," +
                "diagnostic TEXT," +
                "FOREIGN KEY(userId) REFERENCES users(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS patients");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    // User CRUD
    public long insertUser(String fullName, String gender, String email, String mobile, String address, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullName", fullName);
        values.put("gender", gender);
        values.put("userEmail", email);
        values.put("mobile", mobile);
        values.put("address", address);
        values.put("password", password);
        return db.insert("users", null, values);
    }

    public Cursor getUserByEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users WHERE userEmail=? AND password=?", new String[]{email, password});
    }

    // Patients CRUD
    public long insertPatient(String fullname, String diagnostic, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("diagnostic", diagnostic);
        values.put("userId", userId);
        return db.insert("patients", null, values);
    }

    public Cursor getPatientsByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM patients WHERE userId=? ORDER BY id DESC", new String[]{String.valueOf(userId)});
    }

    public void deletePatient(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("patients", "id=?", new String[]{id});
    }

    public int updatePatient(String fullname, String diagnostic, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fullname", fullname);
        values.put("diagnostic", diagnostic);
        return db.update("patients", values, "id=?", new String[]{id});
    }
}
