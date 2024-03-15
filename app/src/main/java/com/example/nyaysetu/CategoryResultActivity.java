package com.example.nyaysetu;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nyaysetu.databinding.ActivityCategoryResultBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.android.material.textview.MaterialTextView;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Objects;

public class CategoryResultActivity extends AppCompatActivity {
    ActivityCategoryResultBinding binding;
    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";
    TextView title;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_result);
        title = findViewById(R.id.heading);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Bundle bundle = getIntent().getExtras();
        title.setVisibility(View.GONE);
        if (bundle!=null){
            String tit = bundle.getString("head");
            String llm = bundle.getString("llm");
            catResult(tit,llm);
        }
    }
    public void catResult(String part,String userl){
        String que = "Explain "+part+" in detail with example in"+userl+" language according to indian penal court";
        GenerativeModel gm = new GenerativeModel("gemini-pro", apiKey);
        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder()
                .addText(que)
                .build();
        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);
        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                String ans = result.getText();
                setResult(ans);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        }, this.getMainExecutor());

    }

    private void setResult(String ans) {
        ans = ans.replace("*", "");
        String[] sentences = ans.split("\\.|:");
        String formattedResult = String.join("<br><br>", sentences);
        String formattedText = "</b> " + formattedResult;
        Spanned formattedSpannedText = Html.fromHtml(formattedText);
        title.setVisibility(View.VISIBLE);
        title.setText(formattedSpannedText);
        progressBar.setVisibility(View.GONE);
    }

}