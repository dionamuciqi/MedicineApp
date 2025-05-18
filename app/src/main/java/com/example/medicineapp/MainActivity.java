package com.example.medicineapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button register = findViewById(R.id.button_register);
        register.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });

        Button forgot = findViewById(R.id.button_forgot_password);
        forgot.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        dbHelper = new MyDbHelper(this);
        db = dbHelper.getWritableDatabase();

        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(v -> {
            EditText email = findViewById(R.id.input_userEmail);
            String emailText = email.getText().toString();

            EditText password = findViewById(R.id.input_password);
            String passwordText = password.getText().toString();

            if (emailText.isEmpty() && passwordText.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Email And Password", Toast.LENGTH_SHORT).show();
                return;
            }

            String hashedPassword = PasswordHasher.generateHash(passwordText);

            String[] projection = {
                    "id",
                    "fullName",
                    "userEmail",
                    "password"
            };

            String selection = "userEmail = ? AND password = ?";
            String[] selectionArgs = {emailText, hashedPassword};

            Cursor cursor = db.query(
                    "users",
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));

                Toast.makeText(this, "Welcome " + fullName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);

                email.setText("");
                password.setText("");
            } else {
                Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                password.setText("");
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
