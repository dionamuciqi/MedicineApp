package com.example.medicineapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        MyDbHelper dbHelper = new MyDbHelper(this);
        db = dbHelper.getWritableDatabase();

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, MainActivity.class);
            startActivity(intent);
        });

        Spinner genderSpinner = findViewById(R.id.gender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.gender,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        EditText fullName = findViewById(R.id.input_fullName);
        EditText userEmail = findViewById(R.id.input_userEmail);
        EditText mobile = findViewById(R.id.inputMobile);
        EditText address = findViewById(R.id.inputAddress);
        EditText password = findViewById(R.id.input_password);
        EditText confirmPassword = findViewById(R.id.confirm_password);
        Spinner gender = findViewById(R.id.gender);
        Button registerButton = findViewById(R.id.register_button);

        // Real-time password validation
        password.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = s.toString();
                if (!isValidPassword(pwd)) {
                    password.setError("Password must be 6-20 characters, include 1 digit, 1 uppercase, 1 lowercase, and 1 special character");
                } else {
                    password.setError(null);
                }
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        registerButton.setOnClickListener(v -> {
            String fullNameText = fullName.getText().toString().trim();
            String genderText = gender.getSelectedItem().toString();
            String userEmailText = userEmail.getText().toString().trim();
            String mobileText = mobile.getText().toString().trim();
            String addressText = address.getText().toString().trim();
            String passwordText = password.getText().toString();
            String confirmPasswordText = confirmPassword.getText().toString();

            if (fullNameText.isEmpty()) {
                fullName.setError("Full name is required");
                return;
            }

            if (userEmailText.isEmpty()) {
                userEmail.setError("Email is required");
                return;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailText).matches()) {
                userEmail.setError("Enter a valid email");
                return;
            }

            if (mobileText.isEmpty()) {
                mobile.setError("Mobile number is required");
                return;
            }

            if (addressText.isEmpty()) {
                address.setError("Address is required");
                return;
            }

            if (passwordText.isEmpty()) {
                password.setError("Password is required");
                return;
            } else if (!isValidPassword(passwordText)) {
                password.setError("Password must be 6-20 characters, include 1 digit, 1 uppercase, 1 lowercase, and 1 special character");
                return;
            }

            if (!passwordText.equals(confirmPasswordText)) {
                confirmPassword.setError("Passwords do not match");
                return;
            }

            ContentValues values = new ContentValues();
            values.put("fullName", fullNameText);
            values.put("gender", genderText);
            values.put("userEmail", userEmailText);
            values.put("mobile", mobileText);
            values.put("address", addressText);
            String hashedPassword = PasswordHasher.generateHash(passwordText);
            values.put("password", hashedPassword);

            long newRowId = db.insert("users", null, values);
            if (newRowId == -1) {
                Toast.makeText(this, "Error inserting data", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Password validation function
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{6,20}$";
        return password.matches(passwordPattern);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) {
            db.close();
        }
    }
}
