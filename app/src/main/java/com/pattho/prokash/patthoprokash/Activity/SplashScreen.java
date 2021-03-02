package com.pattho.prokash.patthoprokash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.UiModeManager;
import android.content.Intent;
import android.os.Bundle;

import com.pattho.prokash.patthoprokash.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        setContentView(R.layout.activity_splash_screen);

        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);

        Intent splash = new Intent(this,LandingActivity.class);
        Thread thread = new Thread(){
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    splash.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(splash);
                    finish();
                }
            }
        };
        thread.start();
    }
}