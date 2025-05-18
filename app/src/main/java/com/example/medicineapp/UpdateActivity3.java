package com.example.medicineapp;

import android.content.Intent;
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

public class UpdateActivity3 extends AppCompatActivity {


    EditText updateTitle,updateDesc;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update3);
        updateBtn = findViewById(R.id.updateBtn);
        updateTitle = findViewById(R.id.updatefullname);
        updateDesc = findViewById(R.id.updateDesc);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        String fullname = getIntent().getStringExtra("fullname");
        String desc = getIntent().getStringExtra("diagnostic");
        int id = getIntent().getIntExtra("id",0);
        updateTitle.setText(fullname);
        updateDesc.setText(desc);

        String sId = String.valueOf(id);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateTitle.length()>0&&updateDesc.length()>0){
                    MyDbHelper dbHelper = new MyDbHelper(UpdateActivity3.this);
                    dbHelper.updateData(updateTitle.getText().toString(),updateDesc.getText().toString(),sId);
                    Toast.makeText(UpdateActivity3.this,"The Data is Successfully Added",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateActivity3.this,Home.class));

                }else {
                    Toast.makeText(UpdateActivity3.this,"The Edit Box is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateActivity3.this,MainActivity.class));
        super.onBackPressed();

    }
}