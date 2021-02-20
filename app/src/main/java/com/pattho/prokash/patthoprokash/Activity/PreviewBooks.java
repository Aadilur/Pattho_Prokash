package com.pattho.prokash.patthoprokash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Activity.BookStore.HomeStore;
import com.pattho.prokash.patthoprokash.Adapter.StoreSliderAdapter;
import com.pattho.prokash.patthoprokash.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class PreviewBooks extends AppCompatActivity {

    SliderView sliderView;
    StoreSliderAdapter sliderAdapter;
    List<String> sliderList;
    DatabaseReference sliderRef;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_books);

        sliderView = findViewById(R.id.imageSlider);

        Intent intent = getIntent();
        data = intent.getStringExtra("bookId1");

        sliderAdapter = new StoreSliderAdapter(this);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setAutoCycle(false);
        sliderView.startAutoCycle();

        sliderRef = FirebaseDatabase.getInstance().getReference().child("Books").child(data).child("preview_img");
        sliderList = new ArrayList<>();

        sliderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    sliderList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        sliderList.add(dataSnapshot.getValue().toString());
                    }

                    if (sliderList.size() == 0) {
                        sliderView.setVisibility(View.GONE);
                    } else sliderView.setVisibility(View.VISIBLE);

                    sliderAdapter = new StoreSliderAdapter(PreviewBooks.this, sliderList);
                    sliderView.setSliderAdapter(sliderAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}