package com.example.focusappm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SemanaFragment extends Fragment {


    public SemanaFragment() {
        // Required empty public constructor
    }

    WeekView mWeekView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_semana,container,false);
        mWeekView = (WeekView)rootView.findViewById(R.id.weekViewDispo);



        return rootView;

    }
}
