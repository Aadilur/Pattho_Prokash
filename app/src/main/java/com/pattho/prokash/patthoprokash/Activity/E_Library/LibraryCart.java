package com.pattho.prokash.patthoprokash.Activity.E_Library;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Activity.BookStore.Payment;
import com.pattho.prokash.patthoprokash.Adapter.LibraryCartAdapter;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.Model.StoreCart_Model;
import com.pattho.prokash.patthoprokash.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryCart extends AppCompatActivity {

    RecyclerView recyclerView;
    Button orderNow;
    DatabaseReference reference,reference2;
    FirebaseUser user;

    List<StoreCart_Model> modelList;

    LibraryCartAdapter adapter;

    AllBook_Model allBook_model;
    StoreCart_Model model;
    int totalBookPrice;

    String name, address, phone2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_cart);

        initData();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(LibraryCart.this);
        recyclerView.setLayoutManager(manager);
        modelList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null) {
            DatabaseReference databaseReference22 = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            databaseReference22.addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        name = snapshot.child("name").getValue().toString() + "";
                        address = snapshot.child("address").getValue().toString() + "";

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(user.getUid());
                        reference.child("totalPrice").setValue(totalBookPrice + "");
                        reference.child("orderDate").setValue(ServerValue.TIMESTAMP);
                        reference.child("phone").setValue(user.getPhoneNumber());
                        reference.child("address").setValue(address);
                        reference.child("name").setValue(name);


                        reference2 = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(user.getUid()).child("books");

                        reference2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                modelList.clear();
                                totalBookPrice = 0;
                                if (snapshot.exists()) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        model = dataSnapshot.getValue(StoreCart_Model.class);
                                        assert model != null;
                                        if (!model.getPrice().equals("")) {
                                            totalBookPrice += Integer.parseInt((String) model.getPrice());
                                        }
                                        modelList.add(model);


                                    }


                                    adapter = new LibraryCartAdapter(LibraryCart.this, modelList);
                                    recyclerView.setAdapter(adapter);


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        if (modelList.size()==0){totalBookPrice = 0;}

        orderNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modelList.size()>0) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("LibraryOrders");
                    DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(user.getUid());

                    reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()){
                                reference.push().setValue(snapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                            Toast.makeText(LibraryCart.this, "Order Placed.", Toast.LENGTH_SHORT).show();
                                                reference2.removeValue();

                                                Intent intent = new Intent(LibraryCart.this,LibraryHome.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();

                                        }else {
                                            Toast.makeText(LibraryCart.this, "Something Went Wrong!", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }else {
                    Toast.makeText(LibraryCart.this, "Please Add Item.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initData() {
        recyclerView = findViewById(R.id.recyclerview);
        orderNow = findViewById(R.id.payNowBtn);

        allBook_model = new AllBook_Model();
        model = new StoreCart_Model();
    }
}