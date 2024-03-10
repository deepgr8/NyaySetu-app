package com.example.nyaysetu;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nyaysetu.databinding.FragmentAdvocateBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.utilities.DynamicColor;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    MaterialTextView greeting;

    Executor executor = newSingleThreadExecutor();
    Spinner select_language;
    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";
    TextToSpeech textToSpeech;
    private static final int MIC_PERMISSION_REQUEST_CODE = 100;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},MIC_PERMISSION_REQUEST_CODE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new homeFragment()).addToBackStack(null).commit();
//        apply to all activities and fragments.....remainder
        QueryPreprocessing.checkTerm(this);
        DynamicColors.applyToActivitiesIfAvailable((Application) getApplicationContext());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
       BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //        Language selection section----------------
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment temp;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.advocate){
                    temp = new AdvocateFragment();
                } else if (itemId==R.id.home) {
                    temp = new homeFragment();
                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (textToSpeech.isSpeaking()){
                                textToSpeech.stop();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "continue", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else{
                    temp = new CategoryFragment();
                    textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int i) {
                            if (textToSpeech.isSpeaking()){
                                textToSpeech.stop();
                            }
                            else{
                                Log.d("s", "onInit: "+"continue");
                            }
                        }
                    });
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,temp).addToBackStack(null).commit();
                return true;
            }
        });
    }
}
