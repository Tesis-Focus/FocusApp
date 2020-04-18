package com.example.focusappm;

import android.graphics.RectF;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SemanaFragment extends AppCompatActivity
        implements EventClickListener<Event>, MonthChangeListener<Event>,
        EventLongPressListener<Event>, EmptyViewLongPressListener{




    WeekView mWeekView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_semana,container,false);
        mWeekView = (WeekView)rootView.findViewById(R.id.weekView3);



        return rootView;

    }
}
