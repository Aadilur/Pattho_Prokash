package com.pattho.prokash.patthoprokash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pattho.prokash.patthoprokash.Model.StoreOrderBooks_Model;
import com.pattho.prokash.patthoprokash.R;

import java.util.List;

public class StoreOrderBookAdapter extends RecyclerView.Adapter<StoreOrderBook_VH>{

    Context context;
    List<StoreOrderBooks_Model> storeOrderBooks_modelList;
    String  txid;
    String uid;

    public StoreOrderBookAdapter(Context context, List<StoreOrderBooks_Model> storeOrderBooks_modelList, String txid, String uid) {
        this.context = context;
        this.storeOrderBooks_modelList = storeOrderBooks_modelList;
        this.txid = txid;
        this.uid = uid;
    }



    public StoreOrderBookAdapter() {
    }

    public StoreOrderBookAdapter(Context context, List<StoreOrderBooks_Model> storeOrderBooks_modelList) {
        this.context = context;
        this.storeOrderBooks_modelList = storeOrderBooks_modelList;
    }

    @NonNull
    @Override
    public StoreOrderBook_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_store_order_book,parent,false);
        return new StoreOrderBook_VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StoreOrderBook_VH holder, int position) {
        final StoreOrderBooks_Model data = storeOrderBooks_modelList.get(position);
        if ( txid.equals(data.getTxid()) && uid.equals(data.getUid())){

            holder.bid.setText(data.getBid()+"");
        }


    }

    @Override
    public int getItemCount() {
        return storeOrderBooks_modelList.size();
    }
}



class StoreOrderBook_VH extends RecyclerView.ViewHolder{

    public TextView bid;
    public StoreOrderBook_VH(@NonNull View itemView) {
        super(itemView);
        bid = itemView.findViewById(R.id.bid);
    }
}
