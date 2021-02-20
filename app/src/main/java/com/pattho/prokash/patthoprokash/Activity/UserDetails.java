package com.pattho.prokash.patthoprokash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pattho.prokash.patthoprokash.R;

public class UserDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
    }

    public void editPro(View view) {
        startActivity(new Intent(this,UpdateUserDetails.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}