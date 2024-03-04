package com.example.nyaysetu;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.color.utilities.DynamicColor;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int SPEECH_REQUEST_CODE = 0;
    String spokenText="";
    String generated_response="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        speechRecognizer();
        QueryPreprocessing queryPreprocessing = new QueryPreprocessing();
        queryPreprocessing.checkTerm(getApplicationContext());
//        apply to all activities and fragments.....remainder
        DynamicColors.applyToActivitiesIfAvailable((Application) getApplicationContext());

       BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
       FrameLayout frameLayout =findViewById(R.id.frameLayout);

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

    private void speechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent,SPEECH_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==SPEECH_REQUEST_CODE && resultCode==RESULT_OK){
            List<String> result  = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result!=null && !result.isEmpty()){
                spokenText = result.get(0);
//                setting user said query on text box
                if(QueryPreprocessing.search(spokenText)){
                    generated_response=AiModel.result(spokenText);
                }
            }
        }
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