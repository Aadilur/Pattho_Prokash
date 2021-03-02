package com.pattho.prokash.patthoprokash.Activity.BookStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.pattho.prokash.patthoprokash.Adapter.StoreOrderBookAdapter;
import com.pattho.prokash.patthoprokash.Model.StoreOrderBooks_Model;
import com.pattho.prokash.patthoprokash.R;

import java.util.ArrayList;
import java.util.List;

public class StoreOrderBookView extends AppCompatActivity {
    RecyclerView recyclerView;

    List<StoreOrderBooks_Model> storeOrderBooksList2;
    StoreOrderBookAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order_book_view);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String txid = intent.getStringExtra("txid");
        String uid = intent.getStringExtra("uid");
        storeOrderBooksList2 = new ArrayList<>();

        for (StoreOrderBooks_Model l : StoreOrderView.storeOrderBooksList){
            if (l.getTxid().equals(txid) && l.getUid().equals(uid)){
                storeOrderBooksList2.add(l);
            }
            System.out.println(storeOrderBooksList2.size()+"----------------");
        }


        adapter = new StoreOrderBookAdapter(StoreOrderBookView.this, storeOrderBooksList2,txid,uid);
        recyclerView.setAdapter(adapter);

    }
}