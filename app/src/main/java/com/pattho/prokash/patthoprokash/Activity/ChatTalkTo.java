package com.pattho.prokash.patthoprokash.Activity;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.pattho.prokash.patthoprokash.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ChatTalkTo extends AppCompatActivity {






    private WebView web;
//    final String url = "https://www.devapp.info/chat.html#max-widget";
    final String url = "https://est.devapp.info#max-widget";


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_talk_to);


        web = findViewById(R.id.webView);
        WebSettings ws = web.getSettings();
        ws.setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(url);



    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()){
            web.goBack();
        }else super.onBackPressed();
    }







}