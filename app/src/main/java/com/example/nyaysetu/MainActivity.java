package com.example.nyaysetu;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;


public class MainActivity extends AppCompatActivity {
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
                } else{
                    temp = new CategoryFragment();
                }
                getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout, temp)
                        .commit();

                return true;
            }
        });
    }
}
