package com.example.nyaysetu;

import androidx.core.content.ContextCompat;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


public class AiModel {

    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";

    static String result(String userQuery){
        GenerativeModel gm  = new GenerativeModel("gemini-pro",apiKey);
        GenerativeModelFutures modelFutures = GenerativeModelFutures.from(gm);
        Content content = new Content.Builder()
                .addText(userQuery)
                .build();
        ListenableFuture<GenerateContentResponse> response = modelFutures.generateContent(content);
        final String[] promptResult = {""};
        response.addListener(()->{
            try {
                GenerateContentResponse response1 = response.get();
                promptResult[0] += response1.getText();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, ContextCompat.getMainExecutor(null));
        return promptResult[0];
    }
}
