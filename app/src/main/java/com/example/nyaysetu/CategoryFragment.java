package com.example.nyaysetu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.nyaysetu.databinding.FragmentCategoryBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class CategoryFragment extends Fragment {
    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";
     FragmentCategoryBinding binding;
     String userl = null;

    String[] promptResult = {""};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("llm",Context.MODE_PRIVATE);
        userl = sharedPreferences.getString("ul","english");
        binding.criminalLawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String part = binding.crimeid.getText().toString();
                Intent intent = new Intent(getContext(), CategoryResultActivity.class);
                intent.putExtra("head",part);
                intent.putExtra("llm",userl);
                startActivity(intent);
            }

        });
        binding.civilLawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String part = binding.civilid.getText().toString();
                Intent intent = new Intent(getContext(), CategoryResultActivity.class);
                intent.putExtra("head",part);
                intent.putExtra("llm",userl);
                startActivity(intent);
            }
        });
        binding.familyLawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String part = binding.familyid.getText().toString();
                Intent intent = new Intent(getContext(), CategoryResultActivity.class);
                intent.putExtra("head",part);
                intent.putExtra("llm",userl);
                startActivity(intent);
            }
        });
        binding.companyLawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String part = binding.companyid.getText().toString();
                Intent intent = new Intent(getContext(), CategoryResultActivity.class);
                intent.putExtra("head",part);
                intent.putExtra("llm",userl);
                startActivity(intent);
            }
        });
        binding.labourLawBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String part = binding.labourid.getText().toString();
                Intent intent = new Intent(getContext(), CategoryResultActivity.class);
                intent.putExtra("head",part);
                intent.putExtra("llm",userl);
                startActivity(intent);
            }
        });
        binding.propertyLawbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String part = binding.propertyid.getText().toString();
                Intent intent = new Intent(getContext(), CategoryResultActivity.class);
                intent.putExtra("head",part);
                intent.putExtra("llm",userl);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

}