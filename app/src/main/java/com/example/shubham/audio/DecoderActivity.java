package com.example.shubham.audio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.Random;

public class DecoderActivity extends AppCompatActivity {

    Uri inputAudio;
    private static final String TAG = "decoderAC";
    private static final int FILE_SELECT_CODE = 0;

    TextView label;
    TextView message;
    EditText key;

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
        key = findViewById(R.id.key);

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
                    String k1 = key.getText().toString();
                    int k = Integer.parseInt(k1);
                    String msg = lsbEncoderDecoder.Audiodecrypt(ins,k);
//                    if(k == SharedPrefManager.getInstance(getApplicationContext()).getKey())
//                        message.setText(SharedPrefManager.getInstance(getApplicationContext()).getMessage());
//                    else
//                        message.setText(RandomShiz(SharedPrefManager.getInstance(getApplicationContext()).getMessage().length()));
                    message.setText(msg);
                    Log.d(TAG, "done");
                    Log.d(TAG,"a"+ SharedPrefManager.getInstance(getApplicationContext()).getMessage());
                    Log.d(TAG, "done");

                } catch (Exception ex) {
                    Log.d(TAG, "hello");
                    ex.printStackTrace();
                }
            }

        });
    }

    private String RandomShiz(int l)
    {
        int leftLimit = 10; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = l;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    inputAudio = data.getData();
                    Log.d(TAG, "File Uri: " + inputAudio.toString());
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}