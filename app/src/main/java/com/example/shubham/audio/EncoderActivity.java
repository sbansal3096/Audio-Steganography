package com.example.shubham.audio;

import android.app.ProgressDialog;
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
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class EncoderActivity extends AppCompatActivity {

    EditText message;
    EditText key;

    Uri inputAudio;
    String outputAudio;

    private static final int FILE_SELECT_CODE = 0;
    private static final String TAG = "stegano";

    TextView label;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encoder);

        outputAudio = Environment.getExternalStorageDirectory() + "/test.wav";

        Intent intent = getIntent();

        if (intent.getType() != null && intent.getType().contains("audio/")) {
            Bundle bundle = intent.getExtras();
            inputAudio =  (Uri) bundle.get(Intent.EXTRA_STREAM);
            Log.d(TAG, "" + inputAudio.toString());
        }

        label = findViewById(R.id.label);
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
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("audio/*");
                share.putExtra(Intent.EXTRA_STREAM, inputAudio);
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
                    byte[] buffer = new byte[1024];
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    int numRead = 0;
                    while (numRead != -1) {
                        numRead = ins.read(buffer);
                        if (numRead > 0)
                            digest.update(buffer, 0, numRead);
                    }
                    byte[] md5Bytes = digest.digest();
                    final String md5Hash = lsbEncoderDecoder.convertHashToString(md5Bytes);
                    System.out.println("MD5 Hash of file is " + md5Hash);
                    final String m = message.getText().toString();
                    final String k1 = key.getText().toString();
                    int k = Integer.parseInt(k1);
                    SharedPrefManager.getInstance(getApplicationContext()).storeMessage(m);
                    SharedPrefManager.getInstance(getApplicationContext()).storeKey(k);

                    //lsbEncoderDecoder.Audioencrypt(m, ins,
                    //new File(outputAudio),22);

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("key", k1);
                    postParam.put("message", m);
                    postParam.put("hash",md5Hash);


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            EndPoints.URL_ENCRYPT, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, response.toString());
                                    //msgResponse.setText(response.toString());
                                    System.out.println("Here is the Message from Server"+response.toString());
                                    try {
                                        Log.d(TAG,response.getString("status"));
                                        Toast.makeText(getApplicationContext(),response.getString("status"),Toast.LENGTH_LONG);

                                        Toast.makeText(getApplicationContext(),"Encyption Done!!!",Toast.LENGTH_LONG);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    }) {
                        /**
                         * Passing some request headers
                         */
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Content-Type", "application/json; charset=utf-8");
                            return headers;
                        }


                    };

                    jsonObjReq.setTag(TAG);
                    // Adding request to request queue
                    queue.add(jsonObjReq);

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