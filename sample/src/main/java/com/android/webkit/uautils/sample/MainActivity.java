package com.android.webkit.uautils.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.webkit.uautils.WebViewSettingsUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String ua = WebViewSettingsUtils.getUserAgent();
        Log.d("UA", ua);
    }
}
