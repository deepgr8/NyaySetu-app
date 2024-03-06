package com.example.nyaysetu;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.nyaysetu.databinding.FragmentHomeBinding;
import com.google.android.material.color.DynamicColors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class homeFragment extends Fragment {

     FragmentHomeBinding homeBinding;

    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        DynamicColors.applyToActivitiesIfAvailable(getActivity().getApplication());
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        ArrayList<String> languages = new ArrayList<>();
        languages.addAll(Arrays.asList("English","Assamese", "Bangla", "Bodo", "Dogri", "Gujarati", "Hindi", "Kashmiri", "Kannada", "Konkani", "Maithili", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Tamil", "Telugu","Sanskrit" , "Santali", "Sindhi", "Urdu"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,languages);
        homeBinding.selectLanguage.setAdapter(arrayAdapter);
//        Greeting section----------------------------
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours >0 && hours<12){
            homeBinding.greeting.setText("Good Morning");
        } else if (hours>=12 && hours<17) {
            homeBinding.greeting.setText("Good afternoon");
        } else  {
            homeBinding.greeting.setText("Good evening");
        }
        return homeBinding.getRoot();
    }
}