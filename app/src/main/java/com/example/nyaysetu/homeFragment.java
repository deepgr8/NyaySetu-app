package com.example.nyaysetu;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.nyaysetu.databinding.FragmentHomeBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class homeFragment extends Fragment {
    FragmentHomeBinding homeBinding;
    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";
    TextToSpeech textToSpeech;
    private DatabaseReference databaseReference;
    private List<String> recentQueries;
    String spokenText = null;
    String cont = "explain me in ";
    String userlang = null;


    String languages[] = {"English", "Assamese", "Bangla", "Gujarati", "Hindi", "Kannada", "Malayalam", "Marathi", "Nepali", "Oriya", "Punjabi", "Tamil", "Telugu"};
    private static final int SPEECH_REQUEST_CODE = 0;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        TextPaint paint = homeBinding.greeting.getPaint();
        recentQueries = new ArrayList<>();
        checkForQueries();
        setRetainInstance(true);
        homeBinding.scrollView.setVisibility(View.INVISIBLE);
        homeBinding.mute.setOnClickListener(view -> {
                if (textToSpeech.isSpeaking() && textToSpeech!=null) { // Optional check
                    textToSpeech.stop();
                }
                else{
                Toast.makeText(getContext(), "continue", Toast.LENGTH_SHORT).show();
            }
        });
//        Greeting section----------------------------
        spinnermethod();
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours > 0 && hours < 12) {
            homeBinding.greeting.setText(getString(R.string.good_morning));
        } else if (hours >= 12 && hours < 17) {
            homeBinding.greeting.setText(getString(R.string.good_afternoon));
        } else {
            homeBinding.greeting.setText(getString(R.string.good_evening));
        }
        float width = paint.measureText((String) homeBinding.greeting.getText());
        Shader textshader = new LinearGradient(0, 0, width, homeBinding.greeting.getTextSize(), new int[]{
                Color.parseColor("#5E7FE7"),
                Color.parseColor("#B36DA7"),
                Color.parseColor("#D96570"),
        }, null, Shader.TileMode.CLAMP);
        homeBinding.greeting.getPaint().setShader(textshader);
        homeBinding.sendQuery.setOnClickListener(view -> {
            if (homeBinding.inputField1.getText() != null) {
                if (tokenizer(homeBinding.inputField1.getText().toString())) {
                    homeBinding.recentitemsContainer.setVisibility(View.INVISIBLE);
                    homeBinding.progressCircular.setVisibility(View.VISIBLE);
                    result(homeBinding.inputField1.getText().toString() + cont + userlang,getContext());
                } else {
                    homeBinding.progressCircular.setVisibility(View.INVISIBLE);
                    failedToken();
                }
            }
        });
        // Todo: spinner item selected item:

        homeBinding.selectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            private int previousSelection = -1; // Initialize with an invalid index

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != previousSelection) { // Check for actual change
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Lang", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("l", homeBinding.selectLanguage.getSelectedItem().toString());
                    editor.apply();
                    previousSelection = i; // Update for future checks
                    spinnermethod();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        homeBinding.voiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speechReco();
            }
        });
        return homeBinding.getRoot();
    }


    private void speechReco() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    //     @noinspection deprecation
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                spokenText = results.get(0).toLowerCase();
                Log.d("sss", "onActivityResult: " + spokenText);
                if (tokenizer(spokenText)) {
                    homeBinding.recentitemsContainer.setVisibility(View.INVISIBLE);
                    homeBinding.progressCircular.setVisibility(View.VISIBLE);
                    homeBinding.inputField1.setText(spokenText);
                    result(spokenText + cont + userlang, getContext());
                } else {
                    homeBinding.progressCircular.setVisibility(View.INVISIBLE);
                    failedToken();
                }
            }
        }
    }
    private void checkForQueries() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String email = currentUser.getEmail();
        String username = email.replace("@gmail.com", "");
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(username)
                    .child("recents");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> userInputList = new ArrayList<>();
                        for (DataSnapshot querySnapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> queryMap = (Map<String, Object>) querySnapshot.getValue();
                            if (queryMap != null && queryMap.containsKey("userInput")) {
                                String userInput = (String) queryMap.get("userInput");
                                String newInput = userInput.replace(cont+userlang," ");
                                userInputList.add(newInput);
                            }
                        }
                        updateUi(userInputList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }

    }

    //     @noinspection deprecation
    private void setresult(String userinput, String result) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("d", result);
        editor.apply();
        editor.commit();
        result = result.replace("*", "");
        String[] sentences = result.split("\\.|:");
        String formattedResult = String.join("<br><br>", sentences);
        String formattedText = "</b> " + formattedResult;
        Spanned formattedSpannedText = Html.fromHtml(formattedText);
        homeBinding.scrollView.setVisibility(View.VISIBLE);
        homeBinding.mute.setVisibility(View.VISIBLE);
        homeBinding.result.setText(formattedSpannedText);
        homeBinding.progressCircular.setVisibility(View.INVISIBLE);
        storeRecent(userinput,result);
        talkback(result);

    }

    private void storeRecent(String userinput, String result) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String email = firebaseAuth.getCurrentUser().getEmail();
        String username = email.replace("@gmail.com", "");
        if (username!=null){
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(username).child("recents");
            String queryKey = databaseReference.push().getKey();
            Map<String, Object> recentMap = new HashMap<>();
            recentMap.put("userInput", userinput);
            recentMap.put("result", result);
            databaseReference.child(queryKey).setValue(recentMap);
        }
    }

    private void updateUi(List<String> recentQueries) {
        if (recentQueries.size() == 1) {
            homeBinding.container3.setVisibility(View.INVISIBLE);
            homeBinding.container2.setVisibility(View.INVISIBLE);
            homeBinding.recent1.setText(recentQueries.get(recentQueries.size()-1));

        }
        if (recentQueries.size() == 2) {
            homeBinding.container3.setVisibility(View.INVISIBLE);
            homeBinding.recent1.setText(recentQueries.get(recentQueries.size()-1));
            homeBinding.recent2.setText(recentQueries.get(recentQueries.size()- 2));
        }
        if (recentQueries.size() >= 3) {
            homeBinding.recent1.setText(recentQueries.get(recentQueries.size()-1));
            homeBinding.recent2.setText(recentQueries.get(recentQueries.size()- 2));
            homeBinding.recent3.setText(recentQueries.get(recentQueries.size()-3));
        }

    }


    private void talkback(String res) {
        String setup = "";
        String[] langCode = {"en_IN", "as_IN", "bn_IN", "gu_IN", "hi_IN", "kn_IN", "ml_IN", "mr_IN", "ne_IN", "or_IN", "pa_IN", "ta_IN", "te_IN"};
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(userlang)) {
                setup = langCode[i];
            }
        }
        String finalSetup = setup;
        try {
            //             @noinspection deprecation
            textToSpeech = new TextToSpeech(getContext(), i -> {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setPitch(-0.5f);
                    textToSpeech.setEngineByPackageName("com.google.android.tts");
                    textToSpeech.setLanguage(new Locale(finalSetup));
                    textToSpeech.speak(res, TextToSpeech.QUEUE_ADD, null);
                }
            });
        } catch (ExceptionInInitializerError error) {
            Toast.makeText(getContext(), "Try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean tokenizer(String prompt) {
        boolean res = false;
        StringTokenizer stringTokenizer = new StringTokenizer(prompt);
        while (stringTokenizer.hasMoreTokens()) {
            String word = stringTokenizer.nextToken();
            if (QueryPreprocessing.search(word)) {
                res = true;
            }
        }
        return res;
    }

    public void result(String userQuery, Context context) {
        GenerativeModel gm = new GenerativeModel("gemini-pro", apiKey);
        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder()
                .addText(userQuery)
                .build();
        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);
        final String[] promptResult = {""};
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                promptResult[0] = result.getText();
                setresult(userQuery, promptResult[0]);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        }, context.getMainExecutor());
    }

    private void spinnermethod() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, languages);
        arrayAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        homeBinding.selectLanguage.setAdapter(arrayAdapter);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Lang", Context.MODE_PRIVATE);
        String la = sharedPreferences.getString("l", "");
        int index = getindex(la);
        userlang = languages[index];
        Log.d("kkk", "spinnermethod: " + userlang);
        homeBinding.selectLanguage.setSelection(index);
    }

    public int getindex(String lan) {
        int index = 0;
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(lan)) {
                index = i;
                return index;
            }
        }
        return index;
    }

    public void failedToken() {
        homeBinding.inputField.setHint(R.string.message);
        if (userlang.equals(languages[0])) {
            textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    textToSpeech.setLanguage(new Locale("en_IN"));
                    textToSpeech.speak("I'm sorry, I'm designed only for legal queries", TextToSpeech.QUEUE_FLUSH, null);
                }
            });
        } else {
            textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    textToSpeech.setLanguage(new Locale("hi_IN"));
                    textToSpeech.speak("मुझे क्षमा करे मैं सिर्फ क़ानूनी जवाब देने के लिए बनाई गई हूँ|", TextToSpeech.QUEUE_FLUSH, null);
                }
            });
        }
    }
    public void showdataa(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String email = currentUser.getEmail();
        String username = email.replace("@gmail.com", "");
        if (currentUser != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(username)
                    .child("recents");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        List<String> userResultList = new ArrayList<>();
                        for (DataSnapshot querySnapshot : dataSnapshot.getChildren()) {
                            Map<String, Object> queryMap = (Map<String, Object>) querySnapshot.getValue();
                            if (queryMap != null && queryMap.containsKey("result")) {
                                String userInput = (String) queryMap.get("result");
                                userResultList.add(userInput);
                            }
                        }
                        updateUimain(userResultList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }
    public void updateUimain(List<String> userInputList) {
        if (homeBinding != null && homeBinding.scrollView != null) {
            homeBinding.scrollView.setVisibility(View.VISIBLE);
            homeBinding.result.setText(userInputList.get(0));
        }
    }
}