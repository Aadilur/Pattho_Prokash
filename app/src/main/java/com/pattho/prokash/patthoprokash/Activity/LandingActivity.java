package com.pattho.prokash.patthoprokash.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.pattho.prokash.patthoprokash.Activity.BookStore.StoreHome;
import com.pattho.prokash.patthoprokash.Activity.E_Library.LibraryHome;
import com.pattho.prokash.patthoprokash.R;

public class LandingActivity extends AppCompatActivity  {

MaterialToolbar materialToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        materialToolbar = findViewById(R.id.materialToolbar);
        materialToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(materialToolbar);
        getSupportActionBar().setTitle("পাঠ্য প্রকাশ");

    }


    public void OnlineBookStore(View view) {
        Intent intent = new Intent(this, StoreHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void OnlineBookLibrary(View view) {
        Intent intent = new Intent(this, LibraryHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
