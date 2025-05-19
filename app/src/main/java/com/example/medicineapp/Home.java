package com.example.medicineapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    FloatingActionButton floatingId;
    RecyclerView recyclerView;
    ArrayList<PatientModel> arrayList = new ArrayList<>();
    private MyDbHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        floatingId = findViewById(R.id.floatingId);
        recyclerView = findViewById(R.id.recyclerView);
        dbHelper = new MyDbHelper(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Merr userId nga intent
        userId = getIntent().getIntExtra("userId", -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: userId is missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Merr pacientët vetëm për userId-në në fjalë
        Cursor cursor = dbHelper.getPatientsByUserId(userId);
        while (cursor.moveToNext()) {
            arrayList.add(new PatientModel(
                    cursor.getString(cursor.getColumnIndexOrThrow("fullname")),
                    cursor.getString(cursor.getColumnIndexOrThrow("diagnostic")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    userId
            ));
        }
        cursor.close();

        // Krijo adapterin me userId
        PatientAdepter adapter = new PatientAdepter(this, arrayList, userId);
        recyclerView.setAdapter(adapter);

        floatingId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Patients.class);
                intent.putExtra("userId", userId);  // kalojmë userId tek Patients
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
