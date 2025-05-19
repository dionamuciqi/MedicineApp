package com.example.medicineapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import javax.mail.MessagingException;

public class VerifyActivity extends AppCompatActivity {
    private EditText otpET;
    private TextView resendBtn;
    private boolean resendEnable = false;
    private int resendTime = 60;
    private String email;
    private String generatedOtp;
    private int userId;  // Added userId field
    private MailSender mailSender = new MailSender();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        otpET = findViewById(R.id.otpET);
        resendBtn = findViewById(R.id.resendTW);
        final TextView otpEmail = findViewById(R.id.otpEmail);
        final Button verifyBtn = findViewById(R.id.verifyBtn);

        // Get email, OTP, and userId from Intent extras
        email = getIntent().getStringExtra("email");
        otpEmail.setText(email);

        generatedOtp = getIntent().getStringExtra("otp");
        userId = getIntent().getIntExtra("userId", -1);

        verifyBtn.setOnClickListener(v -> {
            String enteredOtp = otpET.getText().toString();
            verifyOtp(enteredOtp);
        });

        startCountDownTimer();

        resendBtn.setOnClickListener(v -> {
            if (resendEnable) {
                resendCode();
            }
        });
    }

    private void verifyOtp(String enteredOtp) {
        if (enteredOtp.equals(generatedOtp)) {
            Toast.makeText(VerifyActivity.this, "OTP Verified Successfully!", Toast.LENGTH_SHORT).show();

            // Navigate to Home activity and pass userId
            Intent intent = new Intent(VerifyActivity.this, Home.class);
            intent.putExtra("userId", userId);
            startActivity(intent);

            finish(); // Close VerifyActivity so user can't go back here
        } else {
            Toast.makeText(VerifyActivity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCountDownTimer() {
        resendEnable = false;

        new CountDownTimer(resendTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resendBtn.setText("Resend Code (" + (millisUntilFinished / 1000) + ")");
            }

            @Override
            public void onFinish() {
                resendEnable = true;
                resendBtn.setText("Resend Code");
            }
        }.start();
    }

    private void resendCode() {
        generatedOtp = generateOtp();
        new Thread(() -> {
            try {
                mailSender.sendOtpEmail(email, generatedOtp);
                runOnUiThread(() -> {
                    Toast.makeText(this, "New OTP sent to " + email, Toast.LENGTH_SHORT).show();
                    startCountDownTimer();
                });
            } catch (MessagingException e) {
                runOnUiThread(() -> Toast.makeText(this, "Failed to send OTP", Toast.LENGTH_SHORT).show());
                Log.e("VerifyActivity", "Error sending OTP", e);
            }
        }).start();
    }

    // Generate a random 6-digit OTP
    private String generateOtp() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }
}
