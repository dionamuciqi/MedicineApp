package com.example.medicineapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String fullName = intent.getStringExtra("fullName");
        Toast.makeText(context, "Alarm activated! Welcome " + fullName, Toast.LENGTH_LONG).show();


    }
}
