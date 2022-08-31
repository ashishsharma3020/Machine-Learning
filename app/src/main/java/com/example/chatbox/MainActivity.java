package com.example.chatbox;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.cloud.sdk.core.http.HttpConfigOptions;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.http.ServiceCallback;
import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;
import com.ibm.watson.tone_analyzer.v3.model.ToneScore;

public class MainActivity extends AppCompatActivity {

    ToneAnalysis toneAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IamAuthenticator authenticator = new IamAuthenticator("2_w0vbrNXIK0YTFieGzk7Ayc8kjxfd_v4AHrIcBBlp5P");
                ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", authenticator);
                toneAnalyzer.setServiceUrl("https://api.au-syd.tone-analyzer.watson.cloud.ibm.com/instances/42019e98-60a1-48a6-9367-058b1d3e9ce6");

                EditText et = (EditText) findViewById(R.id.et);
                String text = et.getText().toString();

                ToneOptions toneOptions = new ToneOptions.Builder()
                        .text(text).sentences(false)
                        .build();
                toneAnalyzer.tone(toneOptions).enqueue(new ServiceCallback<ToneAnalysis>() {
                    @Override
                    public void onResponse(Response<ToneAnalysis> response) {
                        toneAnalysis = response.getResult();
                        //Log.d("TAG",toneAnalysis.toString());
                        String str = "";
                        for (ToneScore toneScore : toneAnalysis.getDocumentTone().getTones()) {
                            str = str + toneScore.getToneName() + " ";
                            // Log.d("TAG",toneScore.getToneName());
                        }
                        TextView t = (TextView) findViewById(R.id.textView);
                        t.setText(str);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("TAG", e.getMessage());
                        Log.d("TAG", "error ");
                    }
                });


            }
        });

    }
}