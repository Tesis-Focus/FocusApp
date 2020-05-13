package com.example.focusappm;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MesFragment extends Fragment {

    public MesFragment() {
        // Required empty public constructor
    }

    MaterialCalendarView mCalendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_semana,container,false);
        mCalendarView = rootView.findViewById(R.id.calendarViewMes);
        mCalendarView.getSelectedDates();

        return inflater.inflate(R.layout.fragment_mes, container, false);
    }
}
