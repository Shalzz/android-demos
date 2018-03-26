/*
 * Copyright (c) 2017. Shaleen Jain
 */
package com.shaleenjain.ola.play.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shaleenjain.ola.play.R;

/**
 * Placeholder activity for features that are not implemented in this sample, but
 * are in the navigation drawer.
 */
public class PortfolioActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeholder);
        initializeToolbar();

        TextView t2 = (TextView) findViewById(R.id.text_name);
        t2.setMovementMethod(LinkMovementMethod.getInstance());


        TextView t3 = (TextView) findViewById(R.id.text2_name);
        t3.setMovementMethod(LinkMovementMethod.getInstance());


        TextView t1 = (TextView) findViewById(R.id.text3_name);
        t1.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView v = findViewById(R.id.view3_hex_color);

        Glide.with(this)
                .load("https://lh3.googleusercontent.com/gWybApiUlrw8YVknewflxjzIDEEzZPjzAacb8LsSlSa8d18XTaMFC-5UIK61g8P5stEI=w300")
                .into(v);
    }

}
