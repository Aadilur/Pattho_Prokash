package com.pattho.prokash.patthoprokash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetails extends AppCompatActivity {
    CircleImageView profileImage;
    TextView name,address,phone,email,uid;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        initData();
    }

    @SuppressLint("SetTextI18n")
    private void initData() {
        profileImage = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        uid = findViewById(R.id.uid);


        user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        this.uid.setText("User ID : "+uid+"");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    name.setText("Name : " + snapshot.child("name").getValue().toString() + "");
                    address.setText("Address : " + snapshot.child("address").getValue().toString() + "");
                    email.setText("Email : " + snapshot.child("email").getValue().toString() + "");
                    phone.setText("Phone : " + user.getPhoneNumber());
                    String url = snapshot.child("pic").getValue().toString() + "";
                    if (!url.equals("")){
                        Picasso.get().load(url).into(profileImage);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void editPro(View view) {
        startActivity(new Intent(this,UpdateUserDetails.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}