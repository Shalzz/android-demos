package com.example.shaleenjain.contactapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.shaleenjain.contactapp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class ContactDetailsActivity extends AppCompatActivity {
    TextView contactno, name;
    Button sendMessageButton;
    String nameText;
    JSONObject obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);


        name = (TextView) findViewById(R.id.name);
        contactno = (TextView) findViewById(R.id.contact);
        sendMessageButton = (Button)findViewById(R.id.sendMessageButton);

        //get intent i.e. contact data
        String contact = getIntent().getStringExtra("contact");
        try {
            obj = (JSONObject) new JSONTokener(contact).nextValue();
            nameText = obj.getString("firstName")+" "+obj.getString("lastName");


            name.setText("Name : "+ nameText);
            contactno.setText("Contact No. : "+ obj.getString("mobileno"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactDetailsActivity.this, SendMessageActivity.class);
                try {
                    i.putExtra("name",nameText);
                    i.putExtra("phoneNo",obj.getString("mobileno"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {

        //reload main activity to reload Older Message Data
        Intent i = new Intent(ContactDetailsActivity.this, MainActivity.class);
        //finish();
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        super.onBackPressed();
    }
}
