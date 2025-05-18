package com.example.medicineapp;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class Patients extends AppCompatActivity {
    private SQLiteDatabase db;
    private MyDbHelper dbHelper;

    EditText edName,edDesc;

    Button button;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_patients);


        edName = findViewById(R.id.edName);
        edDesc = findViewById(R.id.edDesc);
        button = findViewById(R.id.addButton);
        dbHelper = new MyDbHelper(Patients.this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edName.length()>0&&edDesc.length()>0){

                    dbHelper.insertData(edName.getText().toString(),edDesc.getText().toString());
                    Toast.makeText(Patients.this,"The data Successfully Added",Toast.LENGTH_SHORT).show();
                    edDesc.setText("");
                    edName.setText("");
                    startActivity(new Intent(Patients.this,Home.class));

                }else {

                    Toast.makeText(Patients.this,"The Edit Box is empty",Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Patients.this,Home.class));
        super.onBackPressed();

    }


}