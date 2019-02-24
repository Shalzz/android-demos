package com.example.shaleenjain.contactapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaleenjain.contactapp.R;
import com.example.shaleenjain.contactapp.utils.JSONCache;
import com.example.shaleenjain.contactapp.utils.SmsSender;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessageActivity extends AppCompatActivity {
    Button send;
    EditText msg;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message_page);

        send = (Button) findViewById(R.id.send);
        msg = (EditText)findViewById(R.id.message);
        txt = (TextView)findViewById(R.id.textView);

        final String name = getIntent().getStringExtra("name");
        final String phoneNo = getIntent().getStringExtra("phoneNo");
        txt.setText("To: "+ phoneNo);

        // get a random number
        Random rand = new Random();
        final String OTP = Integer.toString(rand.nextInt(1000000 - 100000) + 100000);
        String message = "Hi. Your OTP is: " + OTP;
        msg.setText(message);

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SmsSender.sendSMS(phoneNo, msg.getText().toString()).enqueue(
                        new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
                                    String currentDateandTime = sdf.format(new Date());

                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("name", name);
                                    jsonObject.put("OTP", OTP);
                                    jsonObject.put("Date", currentDateandTime);

                                    new JSONCache(SendMessageActivity.this).writeAppendToJSONFile(jsonObject);


                                    Log.d("SendMessageActivity","message sent");
                                    Log.d("SendMessageActivity", "response: " +
                                            response.body().string());

                                    Toast.makeText(SendMessageActivity.this,
                                            "SMS sent successfully", Toast.LENGTH_LONG)
                                            .show();
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                            }
                        }
                );

                Toast.makeText(SendMessageActivity.this,
                        "Error sending SMS", Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}
