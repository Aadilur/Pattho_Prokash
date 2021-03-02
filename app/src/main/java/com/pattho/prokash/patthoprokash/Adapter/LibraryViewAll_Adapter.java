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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Activity.BookStore.StoreBookDetails;
import com.pattho.prokash.patthoprokash.Activity.E_Library.LibraryBookDetails;
import com.pattho.prokash.patthoprokash.Model.AllBook_Model;
import com.pattho.prokash.patthoprokash.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryViewAll_Adapter extends RecyclerView.Adapter<LibraryViewAll_VH> implements Filterable {


    Context context;
    List<AllBook_Model> listData;
    DatabaseReference dir;
    List<AllBook_Model> listDataFull;
    long secCode;


    DatabaseReference databaseReference;

    FirebaseUser user;

    public LibraryViewAll_Adapter() {
    }

    public LibraryViewAll_Adapter(Context context, List<AllBook_Model> listData) {
        this.context = context;
        this.listData = listData;
        listDataFull = new ArrayList<>(listData);
    }

    @NonNull
    @Override
    public LibraryViewAll_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_library_allbooklist,parent,false);
        return new LibraryViewAll_VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull LibraryViewAll_VH holder, int position) {
        final AllBook_Model data = listData.get(position);

        if (data.getCover_img() != null && !data.getCover_img().equals("")) {
            Picasso.get().load(data.getCover_img()).into(holder.cover);
        } else Picasso.get().load(R.drawable.place_holder).into(holder.cover);

        holder.book.setText(data.getBook_name());
        holder.author.setText(data.getAuthor());





        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String category = TextUtils.join(",", data.getCategory());
                String genre = TextUtils.join(",", data.getGenre());
                String keyword = TextUtils.join(",", data.getKeyword());

                Intent intent = new Intent(context, LibraryBookDetails.class);
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

                    if (!data.getNew_price().equals("")) {
                        pPrice = Integer.parseInt(data.getNew_price());

                    } else {
                        if (!data.getPrice().equals("")) {
                            pPrice = Integer.parseInt(data.getPrice());
                        }
                    }


                    user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Toast.makeText(context, "Please Sign In to Continue", Toast.LENGTH_SHORT).show();
                    } else {

                        databaseReference = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(user.getUid()).child("books");
                        int finalPPrice = pPrice;

                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    secCode = System.currentTimeMillis();

                                    if (snapshot.hasChild(data.getId())) {

                                        Toast.makeText(context, "Book Already Added ! Please visit Your Cart.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        databaseReference = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(user.getUid()).child("books").child(data.getId());


                                        Map<String, Object> data0 = new HashMap<>();


                                        data0.put("uid",user.getUid());
                                        data0.put("status","Pending");
                                        data0.put("receiveDate","");
                                        data0.put("returnDate","");
                                        data0.put("borrowTime","");
                                        data0.put("code",secCode);


                                        Map<String, Object> data1 = new HashMap<>();
                                        data1.put("bid", data.getId());
                                        data1.put("quantity", quantity + "");
                                        data1.put("price", quantity * finalPPrice + "");
                                        data1.put("cover",data.getCover_img());
                                        data1.put("bName", data.getBook_name());
                                        data1.put("author", data.getAuthor());
                                        data1.put("time", ServerValue.TIMESTAMP);
                                        data1.put("uid", user.getUid());
                                        data1.put("code",secCode);

                                        databaseReference.setValue(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Toast.makeText(context, "Book Added Successfully.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    databaseReference = FirebaseDatabase.getInstance().getReference().child("LibraryCart").child(user.getUid());


                                    Map<String, Object> data0 = new HashMap<>();


                                    data0.put("uid",user.getUid());
                                    data0.put("status","Pending");
                                    data0.put("receiveDate","");
                                    data0.put("returnDate","");
                                    data0.put("borrowTime","");
                                    data0.put("code",secCode);
                                    databaseReference.setValue(data0).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(context, "Book Added Successfully.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                    Map<String, Object> data1 = new HashMap<>();
                                    data1.put("bid", data.getId());
                                    data1.put("quantity", quantity + "");
                                    data1.put("price", quantity * finalPPrice + "");
                                    data1.put("cover",data.getCover_img());
                                    data1.put("bName", data.getBook_name());
                                    data1.put("author", data.getAuthor());
                                    data1.put("time", ServerValue.TIMESTAMP);
                                    data1.put("uid", user.getUid());
                                    data1.put("code",secCode);

                                    databaseReference.child("books").child(data.getId()).setValue(data1).addOnCompleteListener(new OnCompleteListener<Void>() {
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


class LibraryViewAll_VH extends RecyclerView.ViewHolder{

    MaterialButton cartBtn;
    public ImageView cover;
    public TextView book, author;

    public LibraryViewAll_VH(@NonNull View itemView) {
        super(itemView);

        cover = itemView.findViewById(R.id.vh_coverImg1);
        book = itemView.findViewById(R.id.vh_bookName);
        author = itemView.findViewById(R.id.vh_authorName);
        cartBtn = itemView.findViewById(R.id.addCart);

    }
}
