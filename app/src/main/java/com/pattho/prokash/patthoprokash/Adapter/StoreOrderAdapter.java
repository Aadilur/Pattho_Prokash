package com.pattho.prokash.patthoprokash.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pattho.prokash.patthoprokash.Activity.BookStore.StoreOrderBookView;
import com.pattho.prokash.patthoprokash.Model.StoreOrderBooks_Model;
import com.pattho.prokash.patthoprokash.Model.StoreOrder_Model;
import com.pattho.prokash.patthoprokash.R;

import java.io.Serializable;
import java.util.List;

public class StoreOrderAdapter extends RecyclerView.Adapter<StoreOrder_VH>{
    Context context;
    List <StoreOrder_Model> storeOrderList;
    List <StoreOrderBooks_Model> storeOrderBooksList;

    public StoreOrderAdapter() {
    }

    public StoreOrderAdapter(Context context, List<StoreOrder_Model> storeOrderList) {
        this.context = context;
        this.storeOrderList = storeOrderList;
    }

    @NonNull
    @Override
    public StoreOrder_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_store_order,parent,false);
        return new StoreOrder_VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoreOrder_VH holder, int position) {

        final StoreOrder_Model data = storeOrderList.get(position);

        holder.time.setText(String.valueOf(data.getTime()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoreOrderBookView.class);
                intent.putExtra("txid", data.getTxid());
                intent.putExtra("uid", data.getUid());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return storeOrderList.size();
    }
}



class  StoreOrder_VH extends RecyclerView.ViewHolder{

    public TextView time;
    public StoreOrder_VH(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);

    }
}
