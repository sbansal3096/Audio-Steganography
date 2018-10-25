package com.example.shubham.audio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText message;
    EditText key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shareButton = (Button) findViewById(R.id.share_button);
        Button importButton = (Button) findViewById(R.id.import_button);
        Button encodeButton = (Button) findViewById(R.id.encode);
    }
}