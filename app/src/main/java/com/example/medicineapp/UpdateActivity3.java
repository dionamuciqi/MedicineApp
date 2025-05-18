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

public class UpdateActivity3 extends AppCompatActivity {

    EditText updateTitle, updateDesc;
    Button updateBtn;
    int userId;  // variable to hold userId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update3);
        updateBtn = findViewById(R.id.updateBtn);
        updateTitle = findViewById(R.id.updatefullname);
        updateDesc = findViewById(R.id.updateDesc);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Get data from intent extras
        String fullname = getIntent().getStringExtra("fullname");
        String desc = getIntent().getStringExtra("diagnostic");
        int id = getIntent().getIntExtra("id", 0);

        // Get userId from intent extras (default -1 if missing)
        userId = getIntent().getIntExtra("userId", -1);

        // Set the received data to EditTexts
        updateTitle.setText(fullname);
        updateDesc.setText(desc);

        String sId = String.valueOf(id);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updateTitle.length() > 0 && updateDesc.length() > 0) {
                    MyDbHelper dbHelper = new MyDbHelper(UpdateActivity3.this);
                    dbHelper.updatePatient(updateTitle.getText().toString(), updateDesc.getText().toString(), sId);
                    Toast.makeText(UpdateActivity3.this, "The Data is Successfully Updated", Toast.LENGTH_SHORT).show();

                    // Create intent to go back to Home, pass userId as extra
                    Intent intent = new Intent(UpdateActivity3.this, Home.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                    finish(); // close this activity
                } else {
                    Toast.makeText(UpdateActivity3.this, "The Edit Box is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        // When back pressed, go to Home with userId passed
        Intent intent = new Intent(UpdateActivity3.this, Home.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
        super.onBackPressed();
    }
}
