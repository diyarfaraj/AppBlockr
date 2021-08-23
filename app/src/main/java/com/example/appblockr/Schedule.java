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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ca.antonious.materialdaypicker.MaterialDayPicker;

import static android.content.ContentValues.TAG;

public class Schedule extends AppCompatActivity {
    private Button fromTime, cancelScheduleBtn, confirmScheduleBtn,untilTime;
    MaterialTimePicker picker;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    MaterialDayPicker dayPicker;
    List<MaterialDayPicker.Weekday> selectedDays = new ArrayList<MaterialDayPicker.Weekday>();
    List<String> weekdaysListString = new ArrayList<>();
    List<String> getWeekdaysListString = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        fromTime = findViewById(R.id.fromTimeBtn);
        untilTime = findViewById(R.id.untilTimeBtn);
        cancelScheduleBtn = findViewById(R.id.cancelScheduleBtn);
        confirmScheduleBtn = findViewById(R.id.confirmScheduleBtn);
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
        cancelScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
        confirmScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
        dayPicker = findViewById(R.id.day_picker);

        getWeekdaysListString = SharedPrefUtil.getInstance(this).getDaysList();
        if(getWeekdaysListString != null) {
            if(getWeekdaysListString.size() > 1){
                getWeekdaysListString.forEach(day -> selectedDays.add(convertStringToDays(day)) );
            }
        }

        dayPicker.setSelectedDays(selectedDays);
        dayPicker.setDayPressedListener((weekday, isSelected) -> {
            weekdaysListString.clear();
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
        });
        selectedDays = dayPicker.getSelectedDays();

        String startHour = SharedPrefUtil.getInstance(this).getStartTimeHour();
        String startMinute = SharedPrefUtil.getInstance(this).getStartTimeMinute();
        String endHour = SharedPrefUtil.getInstance(this).getEndTimeHour();
        String endMinute = SharedPrefUtil.getInstance(this).getEndTimeMinute();
        if(startHour != null && startHour != ""){
            fromTime.setText(startHour+":"+startMinute);
        }
        if(endHour != null && endHour != ""){
            untilTime.setText(endHour+":"+endMinute);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private MaterialDayPicker.Weekday convertStringToDays(String day){
        MaterialDayPicker.Weekday currentDay = MaterialDayPicker.Weekday.MONDAY;
        List<MaterialDayPicker.Weekday> tempDays = new ArrayList<MaterialDayPicker.Weekday>();
        tempDays.add(MaterialDayPicker.Weekday.MONDAY);
        tempDays.add(MaterialDayPicker.Weekday.TUESDAY);
        tempDays.add(MaterialDayPicker.Weekday.WEDNESDAY);
        tempDays.add(MaterialDayPicker.Weekday.THURSDAY);
        tempDays.add(MaterialDayPicker.Weekday.FRIDAY);
        tempDays.add(MaterialDayPicker.Weekday.SATURDAY);
        tempDays.add(MaterialDayPicker.Weekday.SUNDAY);
        for (int i = 0; i < tempDays.size(); i++) {
            String wDay =tempDays.get(i).toString();
            if(wDay.equalsIgnoreCase(day)) {
                currentDay = tempDays.get(i);
            } else {
                //skip
            }
        }
        return currentDay;
    }

    private void cancelAlarm() {
        /*Intent intent = new Intent(this, ReceiverApplock.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0);
        if(alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);*/
        //TODO: empty all calender properties, and sharedprefs
    }

    private void confirmSchedule(){
        //todo
        SharedPrefUtil.getInstance(this).putBoolean("confirmSchedule", true);
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
                btn.setText(String.format("%02d", picker.getHour())+" : "+ String.format("%02d", picker.getMinute()));
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, picker.getHour());
                calendar.set(Calendar.MINUTE, picker.getMinute());
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);


                if(btn.getId() == R.id.fromTimeBtn) {
                    Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                    Integer minute = calendar.get(Calendar.MINUTE);
                    SharedPrefUtil.getInstance(v.getContext()).setStartTimeHour(String.format("%02d", hour));
                    SharedPrefUtil.getInstance(v.getContext()).setStartTimeMinute(String.format("%02d", minute));
                } else {
                    Integer hour = calendar.get(Calendar.HOUR_OF_DAY);
                    Integer minute = calendar.get(Calendar.MINUTE);
                    SharedPrefUtil.getInstance(v.getContext()).setEndTimeHour(String.format("%02d", hour));
                    SharedPrefUtil.getInstance(v.getContext()).setEndTimeMinute(String.format("%02d", minute));
                }



            }
        });
    }

}