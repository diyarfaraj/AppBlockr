package com.example.appblockr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appblockr.broadcast.ReceiverApplock;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.Calendar;

public class Schedule extends AppCompatActivity {
    private Button setAlarm, cancelAlarm, selectTime;
    TextView selectedTime;
    MaterialTimePicker picker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        selectTime = findViewById(R.id.selectTimeBtn);
        setAlarm = findViewById(R.id.setAlarmBtn);
        cancelAlarm = findViewById(R.id.cancelAlarmBtn);
        selectedTime = findViewById(R.id.selectedTime);

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }
    private void cancelAlarm() {
        Intent intent = new Intent(this, ReceiverApplock.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
    }

    private void setAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, ScreenBlocker.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);
    }

    private void showTimePicker() {
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select time")
                .build();
        picker.show(getSupportFragmentManager(), "appblockr");
        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(picker.getHour() > 12){
                    selectTime.setText(String.format("%02d",picker.getHour()-12) + " : " +String.format("%02d", picker.getMinute()+ " PM" ));
                } else {*/
                selectedTime.setText(picker.getHour()+" : "+ picker.getMinute());
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }

        });
    }

}