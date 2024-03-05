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
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.utilities.DynamicColor;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AdvocateAdapter advocateAdapter;

    private RecyclerView recyclerView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        apply to all activities and fragments.....remainder
        DynamicColors.applyToActivitiesIfAvailable((Application) getApplicationContext());

       BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
       FrameLayout frameLayout =findViewById(R.id.frameLayout);
//     .....................................................................
        recyclerView = findViewById(R.id.recyclerView);
        List<AdvocateModel> list = new ArrayList<>();
        list.add(new AdvocateModel("swati" , 5));
        list.add(new AdvocateModel("damini" , 5));
        list.add(new AdvocateModel("deepu" , 5));
        list.add(new AdvocateModel("aryan" , 5));
        AdvocateAdapter advocateAdapter1 = new AdvocateAdapter((ArrayList<AdvocateModel>) list, getApplicationContext());
        recyclerView.setAdapter(advocateAdapter1);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
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