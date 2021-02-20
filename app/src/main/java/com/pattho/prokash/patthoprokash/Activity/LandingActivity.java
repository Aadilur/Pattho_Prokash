package com.pattho.prokash.patthoprokash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pattho.prokash.patthoprokash.Activity.BookStore.HomeStore;
import com.pattho.prokash.patthoprokash.R;

public class LandingActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

    }


    public void OnlineBookStore(View view) {
        Intent intent = new Intent(this, HomeStore.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
