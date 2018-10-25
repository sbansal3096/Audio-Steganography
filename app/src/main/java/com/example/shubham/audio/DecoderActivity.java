package com.example.shubham.audio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class DecoderActivity extends AppCompatActivity {

    Uri inputAudio;
    private static final String TAG = "decoderAC";
    private static final int FILE_SELECT_CODE = 0;

    TextView label;
    TextView message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decoder);

        Intent intent = getIntent();

        if (intent.getType() != null && intent.getType().contains("audio/")) {
            Bundle bundle = intent.getExtras();
            inputAudio =  (Uri) bundle.get(Intent.EXTRA_STREAM);
            Log.d(TAG, "" + inputAudio.toString());
        }

        label = findViewById(R.id.label);
        message = findViewById(R.id.message);
        if (inputAudio != null) {
            String path=inputAudio.getPath();
            String fileName= path.substring(path.lastIndexOf('/')+1);
            label.setText(path);
        }

        Button importButton = findViewById(R.id.import_button);
        Button decodeButton = findViewById(R.id.decode);

        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(DecoderActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                LSBEncoderDecoder lsbEncoderDecoder = new LSBEncoderDecoder();
                try {
                    InputStream ins = getContentResolver().openInputStream(inputAudio);
                    String msg = lsbEncoderDecoder.Audiodecrypt(ins,22);
                    message.setText(msg);
                    Log.d(TAG, "done");
                    Log.d(TAG,"a"+ msg);
                    Log.d(TAG, "done");

                } catch (Exception ex) {
                    Log.d(TAG, "hello");
                    ex.printStackTrace();
                }
            }
        });
    }

}