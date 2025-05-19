package com.example.medicineapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private MyDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Load and apply animations to UI elements
        ImageView loginImage = findViewById(R.id.loginImage);
        ImageView shapeImage = findViewById(R.id.shape);

        Animation fadeInOnce = AnimationUtils.loadAnimation(this, R.anim.fade_in_once);
        Animation rotateForever = AnimationUtils.loadAnimation(this, R.anim.rotate_shape);

        loginImage.startAnimation(fadeInOnce);
        shapeImage.startAnimation(rotateForever);

        // Buttons for register and forgot password
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

        // Initialize database
        dbHelper = new MyDbHelper(this);
        db = dbHelper.getWritableDatabase();

        // Login button logic
        Button login = findViewById(R.id.button_login);
        login.setOnClickListener(v -> {
            EditText email = findViewById(R.id.input_userEmail);
            EditText password = findViewById(R.id.input_password);

            String emailText = email.getText().toString();
            String passwordText = password.getText().toString();

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(this, "Please enter your email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hash the password
            String hashedPassword = PasswordHasher.generateHash(passwordText);

            // Query user by email and hashed password
            String[] projection = {"id", "fullName", "userEmail", "password"};
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
                int userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));

                Toast.makeText(this, "Welcome " + fullName, Toast.LENGTH_SHORT).show();

                // Generate OTP
                String generatedOtp = generateOtp();

                // Send OTP to email using MailSender
                new Thread(() -> {
                    try {
                        MailSender mailSender = new MailSender();
                        mailSender.sendOtpEmail(emailText, generatedOtp);

                        // Switch to VerifyActivity with email and OTP
                        runOnUiThread(() -> {
                            Intent intent = new Intent(MainActivity.this, VerifyActivity.class);
                            intent.putExtra("email", emailText);
                            intent.putExtra("otp", generatedOtp);
                            intent.putExtra("userId", userId); // Optional: pass userId if needed
                            startActivity(intent);
                        });

                    } catch (Exception e) {
                        runOnUiThread(() ->
                                Toast.makeText(MainActivity.this, "Failed to send OTP email", Toast.LENGTH_SHORT).show());
                        e.printStackTrace();
                    }
                }).start();

                // Clear input fields
                email.setText("");
                password.setText("");
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                password.setText("");
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        });
    }

    // Generate a random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
