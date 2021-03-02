package com.pattho.prokash.patthoprokash.Activity.BookStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Adapter.StoreOrderAdapter;
import com.pattho.prokash.patthoprokash.Model.StoreOrderBooks_Model;
import com.pattho.prokash.patthoprokash.Model.StoreOrder_Model;
import com.pattho.prokash.patthoprokash.R;

import java.util.ArrayList;
import java.util.List;

public class StoreOrderView extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;

    List<StoreOrder_Model> storeOrderList;
    public static List<StoreOrderBooks_Model> storeOrderBooksList;
    StoreOrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_view);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        storeOrderList = new ArrayList<>();
        storeOrderBooksList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference().child("pendingOrder");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    storeOrderList.clear();
                    storeOrderBooksList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        StoreOrder_Model model = dataSnapshot.getValue(StoreOrder_Model.class);

                        storeOrderList.add(model);


                        for (DataSnapshot dataSnapshot1 :dataSnapshot.child("books").getChildren() ){

                            StoreOrderBooks_Model model1 = dataSnapshot1.getValue(StoreOrderBooks_Model.class);
                            System.out.println(model1.getBid());
                            storeOrderBooksList.add(model1);

                        }

                    }

                    adapter = new StoreOrderAdapter(StoreOrderView.this,storeOrderList);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,StoreHome.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}