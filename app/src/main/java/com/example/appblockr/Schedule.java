package com.example.appblockr;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appblockr.broadcast.ReceiverApplock;
import com.example.appblockr.shared.SharedPrefUtil;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class Schedule extends AppCompatActivity {
    private Button fromTime, cancelAlarm, untilTime;
    MaterialTimePicker picker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    MaterialDayPicker dayPicker;
    List<MaterialDayPicker.Weekday> selectedDays;
    List<String> weekdaysListString = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        fromTime = findViewById(R.id.fromTimeBtn);
        untilTime = findViewById(R.id.untilTimeBtn);
        cancelAlarm = findViewById(R.id.cancelAlarmBtn);
        //selectedStartTime = findViewById(R.id.selectedTime);
        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(fromTime);
            }
        });
        untilTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(untilTime);
            }
        });
        cancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        dayPicker = findViewById(R.id.day_picker);

        dayPicker.setDayPressedListener((weekday, isSelected) -> {
            if(isSelected) {
                selectedDays.add(weekday);
            } else {
                selectedDays.remove(weekday);
            }
            selectedDays.forEach(day -> weekdaysListString.add(day.toString()) );
            Set<String> daySet = new HashSet<>(weekdaysListString);
            weekdaysListString.clear();
            weekdaysListString.addAll(daySet);
            SharedPrefUtil.getInstance(this).setDaysList(weekdaysListString);
            Log.d("TAG", "onCreaterrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr: " + selectedDays);
        });

        selectedDays = dayPicker.getSelectedDays();
        final String test = "";
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

    private void showTimePicker(Button btn) {
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
                btn.setText(picker.getHour()+" : "+ String.format("%02d", picker.getMinute()));
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
            }
        });
    }

}