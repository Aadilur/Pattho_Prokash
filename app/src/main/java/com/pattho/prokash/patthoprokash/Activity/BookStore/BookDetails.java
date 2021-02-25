package com.pattho.prokash.patthoprokash.Activity.BookStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.pattho.prokash.patthoprokash.Activity.PreviewBooks;
import com.pattho.prokash.patthoprokash.R;
import com.yinglan.shadowimageview.ShadowImageView;

import java.util.HashMap;
import java.util.Map;

public class BookDetails extends AppCompatActivity {
    String phone = "+8801688901225";

    ShadowImageView cover;
    Button plus,minus,buy,cart;
    TextView bName, aName,status, price, count, details;
    int num = 1, totalPrice;
    String bid,author,book,cvr;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart");

        initData();
        onClick();

        Intent intent = getIntent();

        Glide.with(this)
                .asBitmap()
                .load(intent.getStringExtra("cover"))
                .into(new CustomTarget<Bitmap>(640,800) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        cover.setImageBitmap(resource);
                        System.out.println(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });

    }

    private void onClick() {
        minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (num <= 1){
                    minus.setClickable(false);
                }else {

                    minus.setClickable(true);
                    num-=1;

                    Intent intent = getIntent();
                    int times = 0;
                    try {

                        if (intent.getStringExtra("newPrice").equals("") ){
                            times = Integer.parseInt(intent.getStringExtra("price"));
                        }else times = Integer.parseInt(intent.getStringExtra("newPrice"));

                    }catch (Exception ignored){}
                    price.setText("৳ " + times*num + ".00/=");
                    totalPrice = times*num;

                    count.setText(num+"");
                    plus.setClickable(true);
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (num >= 10){
                    plus.setClickable(false);
                }else {


                    plus.setClickable(true);
                    num+=1;

                    Intent intent = getIntent();
                    int times = 0;
                    try {
                        if (intent.getStringExtra("newPrice").equals("") ){
                            times = Integer.parseInt(intent.getStringExtra("price"));
                        }else times = Integer.parseInt(intent.getStringExtra("newPrice"));
                    }catch (Exception ignored){}
                    price.setText("BDT " + times*num + "৳/=");
                    totalPrice = times*num;

                    count.setText(num+"");
                    minus.setClickable(true);
                }
            }
        });


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (firebaseUser != null){
                        Intent intent = new Intent(BookDetails.this,Payment.class);
                        intent.putExtra("amount", totalPrice);
                        intent.putExtra("bid",bid);
                        intent.putExtra("num",num);

                        intent.putExtra("bName",book);
                        intent.putExtra("aName",author);
                        intent.putExtra("cover",cvr);

                        startActivity(intent);
                    }else {
                        Toast.makeText(BookDetails.this, "Please SignIn and Verify Your Email...", Toast.LENGTH_SHORT).show();

                    }


            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!firebaseUser.getPhoneNumber().equals(null) || firebaseUser.isEmailVerified()){

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String uid = user.getUid();

                        Map<String,Object > data = new HashMap<>();
                        data.put("bid",bid);
                        data.put("time", ServerValue.TIMESTAMP);
                        data.put("Txid","");
                        data.put("paymentMethod","");
                        data.put("paymentAccount","");
                        data.put("shipping","");
                        data.put("contact","");
                        data.put("amount", totalPrice);
                        data.put("status","pending");
                        data.put("itemNum",num);
                        data.put("uid",uid);
                        data.put("bName",book);
                        data.put("aName",author);
                        data.put("cover",cvr);

                        databaseReference.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(BookDetails.this, "Order Placed Successfully.", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(BookDetails.this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }else {
                        Toast.makeText(BookDetails.this, "Please SignIn and Verify Your Email.", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ignored){}
            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void initData() {
        cover = findViewById(R.id.cover);
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        buy = findViewById(R.id.deleteBtn);
        cart = findViewById(R.id.button3);
        bName = findViewById(R.id.bName);
        aName = findViewById(R.id.aName);
        price = findViewById(R.id.price);
        status = findViewById(R.id.textView7);
        count = findViewById(R.id.quantity);
        details = findViewById(R.id.details);

        Intent intent = getIntent();

        book = intent.getStringExtra("bName");
        author = intent.getStringExtra("author");
        cvr = intent.getStringExtra("cover");

        bName.setText(intent.getStringExtra("bName")+"");
        aName.setText(intent.getStringExtra("author")+"");





        if (intent.getStringExtra("newPrice").equals("") ){
            price.setText("BDT " + intent.getStringExtra("price") + "৳/=");
            try {
                totalPrice = Integer.parseInt(intent.getStringExtra("price"));
            }catch (Exception ignored){}
        }else {
            price.setText("BDT " + intent.getStringExtra("newPrice") + "৳/=");
            try {
                totalPrice = Integer.parseInt(intent.getStringExtra("newPrice"));
            }catch (Exception ignored){}
        }

        details.setText(intent.getStringExtra("description")+"");
        bid = intent.getStringExtra("id");

        int sts = intent.getIntExtra("status",1);
        if (sts == 1){
            status.setText("Status : Available");
            status.setTextColor(Color.parseColor("#0AD025"));
        }else {
            status.setText("Status : Unavailable");
            status.setTextColor(Color.parseColor("#FF5733"));
        }

    }


    public  void toPhone(View view){
        try {

//            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
//            startActivity(intent);

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            startActivity(intent);

        }catch (Exception ignored){

        }
    }

    public void toMessenger(View view) {

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("fb-messenger://user-thread/103807988159973"));
//        i.setPackage("com.facebook.orca");



        try {
            startActivity(i);
        } catch (Exception ex) {
            Toast.makeText(this, "Please Install Facebook Messenger",    Toast.LENGTH_LONG).show();
        }
    }

    public void toWhatapp(View view) {

        try {
            Intent in = new Intent(Intent.ACTION_VIEW);
            in.setData(Uri.parse("https://api.whatsapp.com/send?phone=8801688901225&text=Hello%20*Pattho Prokash*, I want to buy "+num+" copy/s of *"+book+"*"));
            in.setPackage("com.whatsapp");
            startActivity(in);
        } catch (Exception e) {
            Toast.makeText(this, "Whatsapp Not Installed", Toast.LENGTH_LONG).show();
        }

    }


    public void previewBookOnclick(View view) {

        Intent intent = new Intent(getApplicationContext(), PreviewBooks.class);
        intent.putExtra("bookId1",bid);
        startActivity(intent);

    }
}