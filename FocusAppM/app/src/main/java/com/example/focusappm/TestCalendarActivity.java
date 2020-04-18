package com.example.focusappm;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TestCalendarActivity extends AppCompatActivity {

    WeekView mWeekView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_calendar_activity);
        mWeekView = findViewById(R.id.weekView3);

        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {
                Log.i("calendar", "onEventClick: ");
            }
        });

        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                List<Tarea> tareas = new ArrayList<>();
                Log.i("calendar", "onMonthChange: ");
                return tareas;
            }
        });

        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
                Log.i("calendar", "onEventLongPress: ");
            }
        });
    }
}
