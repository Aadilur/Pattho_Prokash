package com.pattho.prokash.patthoprokash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.pattho.prokash.patthoprokash.Activity.BookStore.BookDetails;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class StoreViewAll_Adapter extends RecyclerView.Adapter<StoreViewAll_VH> implements Filterable {


    Context context;
    List<AllBook_Model> listData;
    DatabaseReference dir;
    List<AllBook_Model> listDataFull;

    public StoreViewAll_Adapter() {
    }

    public StoreViewAll_Adapter(Context context, List<AllBook_Model> listData) {
        this.context = context;
        this.listData = listData;
        listDataFull = new ArrayList<>(listData);
    }

    @NonNull
    @Override
    public StoreViewAll_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_allbooklist,parent,false);
        return new StoreViewAll_VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StoreViewAll_VH holder, int position) {
        final AllBook_Model data = listData.get(position);

        if (data.getCover_img() != null && !data.getCover_img().equals("")) {
            Picasso.get().load(data.getCover_img()).into(holder.cover);
        } else Picasso.get().load(R.drawable.place_holder).into(holder.cover);

        holder.book.setText(data.getBook_name());
        holder.author.setText(data.getAuthor());

        if (!data.getNew_price().equals("")){
            holder.price.setText(data.getNew_price() + " tk.");
            int newPrice = Integer.parseInt(data.getNew_price());
            int price = Integer.parseInt(data.getPrice());

            int discount = ((price-newPrice)*100)/price;
            holder.discount.setText("(" + discount + "% off)");
        }else holder.price.setText(data.getPrice()+ " tk.");



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = TextUtils.join(",", data.getCategory());
                String genre = TextUtils.join(",", data.getGenre());
                String keyword = TextUtils.join(",", data.getKeyword());

                Intent intent = new Intent(context, BookDetails.class);
                intent.putExtra("id", data.getId());
                intent.putExtra("cover",data.getCover_img());
                intent.putExtra("bName", data.getBook_name());
                intent.putExtra("author", data.getAuthor());
                intent.putExtra("price", data.getPrice());
                intent.putExtra("newPrice", data.getNew_price());
                intent.putExtra("page", data.getPage());
                intent.putExtra("condition", data.getCondition());
                intent.putExtra("description", data.getDescription());
                intent.putExtra("category", category);
                intent.putExtra("genre", genre);
                intent.putExtra("keyword", keyword);
                intent.putExtra("status", data.getStatus());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchTxt = constraint.toString().toLowerCase();
                List<AllBook_Model> tempList = new ArrayList<>();

                if (searchTxt.length() == 0){
                    tempList.addAll(listDataFull);

                }else {
                    for (AllBook_Model data: listDataFull){
                        if (data.getBook_name().toLowerCase().contains(constraint) ||
                                data.getId().toLowerCase().contains(constraint) ||
                                data.getGenre().toString().toLowerCase().contains(constraint) ||
                                data.getKeyword().toString().toLowerCase().contains(constraint))
                        {
                            tempList.add(data);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = tempList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listData.clear();
                listData.addAll((Collection<? extends AllBook_Model>) results.values);
                notifyDataSetChanged();

            }
        };

        return filter;
    }
}




class StoreViewAll_VH extends RecyclerView.ViewHolder{

    MaterialButton cartBtn;
    public ImageView cover;
    public TextView book, author, price, discount;

    public StoreViewAll_VH(@NonNull View itemView) {
        super(itemView);

        cover = itemView.findViewById(R.id.vh_coverImg1);
        book = itemView.findViewById(R.id.vh_bookName);
        author = itemView.findViewById(R.id.vh_authorName);
        price = itemView.findViewById(R.id.vh_price);
        discount = itemView.findViewById(R.id.vh_page);
        cartBtn = itemView.findViewById(R.id.addCart);

    }
}
