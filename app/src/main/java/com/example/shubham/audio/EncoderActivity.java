package com.example.shubham.audio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;

public class EncoderActivity extends AppCompatActivity {

    EditText message;
    EditText key;

    Uri inputAudio;
    Uri outputAudioUri;
    String outputAudio;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "stegano";

    TextView label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoder);

        label = findViewById(R.id.label);

        outputAudio = Environment.getExternalStorageDirectory() + "/test.wav";
        outputAudioUri = Uri.fromFile(new File(outputAudio));
        label.setText(outputAudioUri.toString());
        Intent intent = getIntent();

        if (intent.getType() != null && intent.getType().contains("audio/")) {
            Bundle bundle = intent.getExtras();
            inputAudio =  (Uri) bundle.get(Intent.EXTRA_STREAM);
            Log.d(TAG, "" + inputAudio.toString());
        }

        if (inputAudio != null) {
            label.setText(inputAudio.toString());
        }

        Button shareButton = findViewById(R.id.share_button);
        Button importButton = findViewById(R.id.import_button);
        Button encodeButton = findViewById(R.id.encode);
        message = findViewById(R.id.message);
        key = findViewById(R.id.key);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outputAudioUri = Uri.fromFile(new File(outputAudio));
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, outputAudioUri);
                startActivity(Intent.createChooser(share, "Share Encrypted Sound File"));
            }
        });

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
                    Toast.makeText(EncoderActivity.this, "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LSBEncoderDecoder lsbEncoderDecoder = new LSBEncoderDecoder();
                Log.d(TAG, inputAudio.toString());
                Log.d(TAG, outputAudio);
                try {
                    InputStream ins = getContentResolver().openInputStream(inputAudio);
                    final String m  = message.getText().toString();
                    final  String k1 = key.getText().toString();
                    int k = Integer.parseInt(k1);
                    SharedPrefManager.getInstance(getApplicationContext()).storeMessage(m);
                    SharedPrefManager.getInstance(getApplicationContext()).storeKey(k);

                    lsbEncoderDecoder.Audioencrypt(m, ins,
                            new File(outputAudio),k);
                    label.setText("Encoding done");
                    Log.d("Done","Done Encryption");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
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