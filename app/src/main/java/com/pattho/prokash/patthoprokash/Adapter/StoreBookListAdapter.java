package com.pattho.prokash.patthoprokash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.pattho.prokash.patthoprokash.Activity.BookStore.StoreBookDetails;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreBookListAdapter extends RecyclerView.Adapter<StoreBookList_VH> {

    Context context;
    List<AllBook_Model> listData;
    FirebaseUser user;
    DatabaseReference dir;
    long count = 0;


    DatabaseReference databaseReference;
    StorageReference storageReference;

    public StoreBookListAdapter(Context context, List<AllBook_Model> listData, DatabaseReference dir) {
        this.context = context;
        this.listData = listData;
        this.dir = dir;
    }

    public StoreBookListAdapter(Context context, List<AllBook_Model> data) {
        this.context = context;
        this.listData = data;
    }

    public StoreBookListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public StoreBookList_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_store_allbooklist,parent,false);
        return new StoreBookList_VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StoreBookList_VH holder, int position) {

        final AllBook_Model data = listData.get(position);

        if (data.getCover_img() != null && !data.getCover_img().equals("")) {
            Picasso.get().load(data.getCover_img()).into(holder.cover);
        } else Picasso.get().load(R.drawable.place_holder).into(holder.cover);

        holder.book.setText(data.getBook_name());
        holder.author.setText(data.getAuthor());


        if (!data.getNew_price().equals("")) {
            holder.price.setText(data.getNew_price() + " tk.");
            int newPrice = Integer.parseInt(data.getNew_price());
            int price = Integer.parseInt(data.getPrice());

            int discount = ((price - newPrice) * 100) / price;
            holder.discount.setText("(" + discount + "% off)");
        } else {
            holder.price.setText(data.getPrice() + " tk.");

        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = TextUtils.join(",", data.getCategory());
                String genre = TextUtils.join(",", data.getGenre());
                String keyword = TextUtils.join(",", data.getKeyword());

                Intent intent = new Intent(context, StoreBookDetails.class);
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

        holder.cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                if (firebaseUser != null) {

                int quantity = 1;
                int pPrice = 0;

                if (!data.getNew_price().equals("")){
                    pPrice = Integer.parseInt(data.getNew_price());

                }else {
                    if (!data.getPrice().equals("")) {
                        pPrice = Integer.parseInt(data.getPrice());
                    }
                }



                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user ==null){
                    Toast.makeText(context, "Please Sign In to Continue", Toast.LENGTH_SHORT).show();
                }else {

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid()).child("books");
                    int finalPPrice = pPrice;
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {

                                if (snapshot.hasChild(data.getId())) {

                                    Toast.makeText(context, "Book Already Added ! Please visit Your Cart.", Toast.LENGTH_SHORT).show();
                                } else {
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid()).child("books").child(data.getId());
                                    Map<String, Object> data1 = new HashMap<>();
                                    data1.put("bid", data.getId());
                                    data1.put("quantity", quantity+"");
                                    data1.put("price", quantity * finalPPrice+"");

                                    data1.put("cover",data.getCover_img());
                                    data1.put("bName", data.getBook_name());
                                    data1.put("author", data.getAuthor());
                                    data1.put("uid", user.getUid());

                                    databaseReference.setValue(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(context, "Book Added Successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }

                            else {
                                databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid()).child("books").child(data.getId());
                                Map<String, Object> data1 = new HashMap<>();
                                data1.put("bid", data.getId());
                                data1.put("quantity", quantity+"");
                                data1.put("price", quantity * finalPPrice+"");

                                data1.put("cover",data.getCover_img());
                                data1.put("bName", data.getBook_name());
                                data1.put("author", data.getAuthor());
                                data1.put("uid", user.getUid());

                                databaseReference.setValue(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            Toast.makeText(context, "Book Added Successfully.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });





                }

                }else {
                    Toast.makeText(context, "Please Sign In.", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }


    @Override
    public int getItemCount() {
        return Math.min(listData.size(), 6);
    }
}



class StoreBookList_VH extends RecyclerView.ViewHolder {



    public MaterialCardView cardView;
    MaterialButton cartBtn;
    public ImageView cover;
    public TextView book, author, price, discount;

    public StoreBookList_VH(@NonNull View itemView) {
        super(itemView);

        cover = itemView.findViewById(R.id.vh_coverImg1);
        book = itemView.findViewById(R.id.vh_bookName);
        author = itemView.findViewById(R.id.vh_authorName);
        price = itemView.findViewById(R.id.vh_price);
        discount = itemView.findViewById(R.id.vh_discount);
        cartBtn = itemView.findViewById(R.id.addCart);


    }
}
