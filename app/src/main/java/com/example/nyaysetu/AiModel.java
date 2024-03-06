package com.example.nyaysetu;

import android.content.Context;
import android.util.Log;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


public class AiModel {
    private static final String apiKey = "AIzaSyBanBCnl7DjDbZ8MeSFN9rv290bEZ1qMSM";

    public static void result(String userQuery,Context context){
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
                PreferenceHelper.saveStringInfo(context,"result",result.getText());
                Log.d("res", "onSuccess: "+promptResult[0]);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();

            }
        }, context.getMainExecutor());
    }
}
