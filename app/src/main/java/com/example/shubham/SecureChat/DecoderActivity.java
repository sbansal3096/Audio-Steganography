package com.example.shubham.SecureChat;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;



public class DecoderActivity extends AppCompatActivity {

    Uri inputAudio;
    private static final String TAG = "decoderAC";
    private static final int FILE_SELECT_CODE = 0;

    TextView label;
    TextView message;
    EditText key;

    String m;

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
            label.setText("File Imported");
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
                    label.setText("File Imported");
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
                    byte[] buffer = new byte[1024];
                    MessageDigest digest = MessageDigest.getInstance("MD5");
                    int numRead = 0;
                    while (numRead != -1) {
                        numRead = ins.read(buffer);
                        if (numRead > 0)
                            digest.update(buffer, 0, numRead);
                    }
                    byte [] md5Bytes = digest.digest();
                    final String md5Hash=lsbEncoderDecoder.convertHashToString(md5Bytes);
                    System.out.println("MD5 Hash of file is "+ md5Hash);
                    final String k1 = key.getText().toString();
                    int k = Integer.parseInt(k1);
                    //String msg = lsbEncoderDecoder.Audiodecrypt(ins,k);

                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                    Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("key", k1);
                    postParam.put("hash",md5Hash);


                    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                            EndPoints.URL_DECRYPT, new JSONObject(postParam),
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d(TAG, response.toString());
                                    try {
                                        m = response.getString("result");
                                        System.out.println(m);
                                        if(m.equals("error"))
                                            message.setText(RandomShiz(15));
                                        else
                                            message.setText(m);
                                        Toast.makeText(getApplicationContext(),"Decoding Done!!",Toast.LENGTH_LONG);

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
                    label.setText("File Decoded");
                    //System.out.println(m);
                    //message.setText(m);
                    //Log.d(TAG,m);
                    /*
                    if(k == SharedPrefManager.getInstance(getApplicationContext()).getKey())
                        message.setText(SharedPrefManager.getInstance(getApplicationContext()).getMessage());
                    else
                        message.setText(RandomShiz(SharedPrefManager.getInstance(getApplicationContext()).getMessage().length()));
                    Log.d(TAG, "done");
                    Log.d(TAG,"a"+ SharedPrefManager.getInstance(getApplicationContext()).getMessage());
                    Log.d(TAG, "done");
                    */
                } catch (Exception ex) {
                    Log.d(TAG, "hello");
                    ex.printStackTrace();
                }
            }

        });
    }

    private String RandomShiz(int l)
    {
        int leftLimit = 65; // letter 'a'
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