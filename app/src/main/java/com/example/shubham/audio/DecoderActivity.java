package com.example.shubham.audio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DecoderActivity extends AppCompatActivity {

    Uri inputAudio;
    private static final String TAG = "decoderAC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);

        Intent intent = getIntent();
        Log.d(TAG, "Here");

        if (intent.getType().contains("audio/")) {
            Log.d(TAG, "Here again");
            inputAudio = intent.getData();
//            Log.d(TAG, inputAudio.toString());
        }
//        Button encoder_main = findViewById(R.id.encoder_main);
//        Button decoder_main = findViewById(R.id.decoder_main);

    }

}