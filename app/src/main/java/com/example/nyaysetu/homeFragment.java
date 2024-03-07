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
import androidx.annotation.UiThread;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

public class homeFragment extends Fragment {
    FragmentHomeBinding homeBinding;

    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";
    TextToSpeech textToSpeech;
    String spokenText=null;
    String cont = "tell me in ";
    String userlang=null;


    String languages [] = {"English","Assamese", "Bangla", "Bodo", "Dogri", "Gujarati", "Hindi", "Kashmiri", "Kannada", "Konkani", "Maithili", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Tamil", "Telugu","Sanskrit" , "Santali", "Sindhi", "Urdu"};
    private static final int SPEECH_REQUEST_CODE = 0;

    public homeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false);
        TextPaint paint = homeBinding.greeting.getPaint();
//        Greeting section----------------------------
        spinnermethod();
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        if (hours >0 && hours<12){
            homeBinding.greeting.setText("Good Morning");
        } else if (hours>=12 && hours<17) {
            homeBinding.greeting.setText("Good afternoon");
        } else  {
            homeBinding.greeting.setText("Good evening");
        }
        float width=paint.measureText((String) homeBinding.greeting.getText());
        Shader textshader = new LinearGradient(0,0,width,homeBinding.greeting.getTextSize(),new int[]{
                Color.parseColor("#5E7FE7"),
                Color.parseColor("#B36DA7"),
                Color.parseColor("#D96570"),
        },null,Shader.TileMode.CLAMP);
        homeBinding.greeting.getPaint().setShader(textshader);
        homeBinding.mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textToSpeech.isSpeaking()){
                    textToSpeech.stop();
                }
            }
        });
        homeBinding.sendQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "btn clicked", Toast.LENGTH_SHORT).show();
                if (homeBinding.inputField1.getText()!=null){
                    if (tokenizer(homeBinding.inputField1.getText().toString())){
                        Toast.makeText(getContext(), "true", Toast.LENGTH_SHORT).show();
                        homeBinding.progressCircular.setVisibility(View.VISIBLE);
                        result(homeBinding.inputField1.getText().toString()+cont+userlang,getContext());
                    }
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
                speechRecognizer();
            }
        });
        return homeBinding.getRoot();
    }

    private void speechRecognizer(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true); // Prefer offline recognition
        startActivityForResult(intent,SPEECH_REQUEST_CODE);
    }

    /** @noinspection deprecation*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK){
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                spokenText = results.get(0).toLowerCase();
                Log.d("sss", "onActivityResult: "+spokenText);
                if (tokenizer(spokenText)){
                    homeBinding.progressCircular.setVisibility(View.VISIBLE);
                    homeBinding.inputField1.setText(spokenText);
                    result(spokenText+cont+userlang,getContext());
                }
            }
        }
    }
    /** @noinspection deprecation*/
    private void setresult(String userinput, String result){
        result = result.replace("*", "");
        String[] sentences = result.split("\\.|:");
        String formattedResult = String.join("<br><br>", sentences);
        String formattedText = "</b> " + formattedResult;
        Spanned formattedSpannedText = Html.fromHtml(formattedText);
        homeBinding.scrollView.setVisibility(View.VISIBLE);
        homeBinding.mute.setVisibility(View.VISIBLE);
        homeBinding.result.setText(formattedSpannedText);
        homeBinding.progressCircular.setVisibility(View.INVISIBLE);
        talkback(result);

    }
    private void talkback(String res){

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(new Locale("hi_","IN"));
                    textToSpeech.speak(res,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }
    public static boolean tokenizer(String prompt){
        boolean res = false;
        StringTokenizer stringTokenizer= new StringTokenizer(prompt);
        while(stringTokenizer.hasMoreTokens()){
            String word = stringTokenizer.nextToken();
            if (QueryPreprocessing.search(word)){
                res = true;
            }
        }
        return res;
    }
    public  void result(String userQuery, Context context){
        GenerativeModel gm  = new GenerativeModel("gemini-pro",apiKey);
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
                setresult(userQuery,promptResult[0]);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        }, context.getMainExecutor());
    }

    private void spinnermethod(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item,languages);
        arrayAdapter.setDropDownViewResource(com.google.android.material.R.layout.support_simple_spinner_dropdown_item);
        homeBinding.selectLanguage.setAdapter(arrayAdapter);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("Lang",Context.MODE_PRIVATE);
        String la = sharedPreferences.getString("l","");
        int index = getindex(la);
        userlang = languages[index];
        Log.d("kkk", "spinnermethod: "+userlang);
        homeBinding.selectLanguage.setSelection(index);
    }
    public int getindex(String lan){
        int index = 0;
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(lan)){
                index = i;
                return index;
            }
        }
        return index;
    }
}