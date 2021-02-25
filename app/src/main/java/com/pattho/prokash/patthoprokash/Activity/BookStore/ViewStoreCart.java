package com.pattho.prokash.patthoprokash.Activity.BookStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Adapter.StoreCartAdapter;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.Model.StoreCart_Model;
import com.pattho.prokash.patthoprokash.R;

import java.util.ArrayList;
import java.util.List;

public class ViewStoreCart extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView totalPrice;
    Button payBtn;
    DatabaseReference reference,reference2;
    FirebaseUser user;

    List<StoreCart_Model> modelList;

    StoreCartAdapter adapter;

    AllBook_Model allBook_model;
    StoreCart_Model model;
    int totalBookPrice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store_cart);
        initData();

        RecyclerView.LayoutManager manager = new LinearLayoutManager(ViewStoreCart.this);
        recyclerView.setLayoutManager(manager);
        modelList = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();

        reference2 = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid()).child("books");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelList.clear();
                totalBookPrice = 0;
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                        model = dataSnapshot.getValue(StoreCart_Model.class);
                        assert model != null;
                        if (!model.getPrice().equals("")) {
                            totalBookPrice += Integer.parseInt((String) model.getPrice());
                        }
                        modelList.add(model);


                    }


                    adapter = new StoreCartAdapter(ViewStoreCart.this, modelList,totalBookPrice);
                    recyclerView.setAdapter(adapter);
                    reference2 = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid()).child("totalPrice");
                    reference2.setValue(totalBookPrice+"");
                    totalPrice.setText(String.valueOf("৳ "+totalBookPrice+".00/="));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewStoreCart.this,Payment.class);
                intent.putExtra("fromCart",true);
                intent.putExtra("amount",totalBookPrice);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        recyclerView = findViewById(R.id.recyclerview);
        totalPrice = findViewById(R.id.totalPrice);
        payBtn = findViewById(R.id.payNowBtn);

        allBook_model = new AllBook_Model();
        model = new StoreCart_Model();
    }
}