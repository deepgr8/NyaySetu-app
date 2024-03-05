package com.example.nyaysetu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.utilities.DynamicColor;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MaterialTextView greeting;
    Spinner select_language;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        apply to all activities and fragments.....remainder
        DynamicColors.applyToActivitiesIfAvailable((Application) getApplicationContext());

       BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
       FrameLayout frameLayout =findViewById(R.id.frameLayout);
        //        Language selection section----------------
        select_language = findViewById(R.id.select_language);
        ArrayList<String> languages = new ArrayList<>();
        languages.addAll(Arrays.asList("English","Assamese", "Bangla", "Bodo", "Dogri", "Gujarati", "Hindi", "Kashmiri", "Kannada", "Konkani", "Maithili", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Tamil", "Telugu","Sanskrit" , "Santali", "Sindhi", "Urdu"));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,languages);
        select_language.setAdapter(arrayAdapter);
//        Greeting section----------------------------
        greeting = findViewById(R.id.greeting);
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours >0 && hours<12){
            greeting.setText("Good Morning");
        } else if (hours>=12 && hours<17) {
            greeting.setText("Good afternoon");
        } else if (hours>=17 && hours <20) {
            greeting.setText("Good evening");
        }

//     .....................................................................

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.advocate){
                    loadFragment(new AdvocateFragment() , false);
                } else  {
                    loadFragment(new CategoryFragment() , false);
                }

                return true;
            }
        });
    }

   private void loadFragment(Fragment fragment , boolean isAppInitialized){
       FragmentManager fragmentManager = getSupportFragmentManager();
       FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
       if (isAppInitialized){
           fragmentTransaction.add(R.id.frameLayout , fragment);
       }else {
           fragmentTransaction.replace(R.id.frameLayout , fragment);
       }
       fragmentTransaction.commit();
   }

}