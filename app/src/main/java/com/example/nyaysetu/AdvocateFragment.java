package com.example.nyaysetu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nyaysetu.databinding.FragmentAdvocateBinding;

import java.util.ArrayList;


public class AdvocateFragment extends Fragment {

    FragmentAdvocateBinding binding;


    public AdvocateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdvocateBinding.inflate(inflater,container,false);
        ArrayList<AdvocateModel> list = new ArrayList<>();
        list.add(new AdvocateModel("Name : swati" , "Exp : 5",5));
        list.add(new AdvocateModel("Name : damini" , "Exp : 5" ,4));
        list.add(new AdvocateModel("Name : deepu" , "Exp : 5" , 1));
        list.add(new AdvocateModel("Name : aryan" , "Exp : 5",4));
        list.add(new AdvocateModel("Name : Chakri" , "Exp : 5",4));
        binding.recycleOne.setLayoutManager(new LinearLayoutManager(getContext()));
        AdvocateAdapter advocateAdapter1 = new AdvocateAdapter(list, getContext());
        binding.recycleOne.setAdapter(advocateAdapter1);
        return binding.getRoot();
    }
}