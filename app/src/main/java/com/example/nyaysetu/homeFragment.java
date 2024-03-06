package com.example.nyaysetu;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
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
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nyaysetu.databinding.CustomSpinnerBinding;
import com.example.nyaysetu.databinding.FragmentHomeBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
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

    private static final int SPEECH_REQUEST_CODE = 0;

    public homeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeBinding = FragmentHomeBinding.inflate(inflater,container,false);
//        String[] languages  = {"English","Assamese", "Bangla", "Bodo", "Dogri", "Gujarati", "Hindi", "Kashmiri", "Kannada", "Konkani", "Maithili", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Tamil", "Telugu","Sanskrit" , "Santali", "Sindhi", "Urdu"};
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,languages);
//        homeBinding.selectLanguage.setAdapter(arrayAdapter);
        TextPaint paint = homeBinding.greeting.getPaint();
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
        float width=paint.measureText((String) homeBinding.greeting.getText());
        Shader textshader = new LinearGradient(0,0,width,homeBinding.greeting.getTextSize(),new int[]{
                Color.parseColor("#F97C3C"),
                Color.parseColor("#FDB54E"),
                Color.parseColor("#64B678"),
                Color.parseColor("#478AEA"),
                Color.parseColor("#8446CC"),
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
                        result(homeBinding.inputField1.getText().toString(),getContext());
                    }
                }
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] languages  = {"English","Assamese", "Bangla", "Bodo", "Dogri", "Gujarati", "Hindi", "Kashmiri", "Kannada", "Konkani", "Maithili", "Malayalam", "Manipuri", "Marathi", "Nepali", "Oriya", "Punjabi", "Tamil", "Telugu","Sanskrit" , "Santali", "Sindhi", "Urdu"};
        Spinner lan = view.findViewById(R.id.select_language);
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(getContext() ,languages);
        lan.setAdapter(customSpinnerAdapter);
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
                    homeBinding.inputField1.setText(spokenText);
                    result(spokenText,getContext());
                }
            }
        }
    }
    private void setresult(String userinput,String result){
        String formattedText = "<b>Q:</b> " + userinput + "<br><br>" +
                "<b>A:</b> "+result;
        Log.e("lan", "setresult: "+formattedText);
        homeBinding.scrollView.setVisibility(View.VISIBLE);
        homeBinding.mute.setVisibility(View.VISIBLE);
        homeBinding.result.setText(formattedText);
        talkback(result);

    }
    private void talkback(String res){

        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(new Locale("hi_IN"));
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

}