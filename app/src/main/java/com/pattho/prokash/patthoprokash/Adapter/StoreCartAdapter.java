package com.pattho.prokash.patthoprokash.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.Model.StoreCart_Model;
import com.pattho.prokash.patthoprokash.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StoreCartAdapter extends RecyclerView.Adapter<StoreCart_VH> {

    Context context;
    List<StoreCart_Model> storeCart_models;
    int totalBookPrice = 0;

    public StoreCartAdapter() {
    }

    public StoreCartAdapter(Context context, List<StoreCart_Model> storeCart_models, int totalBookPrice) {
        this.context = context;
        this.storeCart_models = storeCart_models;
        this.totalBookPrice = totalBookPrice;
    }




    public StoreCartAdapter(Context context, List<StoreCart_Model> storeCart_models) {
        this.context = context;
        this.storeCart_models = storeCart_models;
    }

    @NonNull
    @Override
    public StoreCart_VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_store_cart_view, parent, false);
        return new StoreCart_VH(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull StoreCart_VH holder, int position) {
        final StoreCart_Model data = storeCart_models.get(position);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (!data.getCover().equals("")) {
            Picasso.get().load(data.getCover()).into(holder.cover);
        }
        holder.price.setText("৳ "+data.getPrice().toString()+".00/=");

        holder.bName.setText(data.getbName());
        holder.aName.setText(data.getAuthor());
        holder.quantity.setText(data.getQuantity());


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert user != null;

                System.out.println("---------------------"+storeCart_models.size());

                if (storeCart_models.size() ==1){
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart");
                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                notifyDataSetChanged();
                                notifyItemRemoved(position);
                            }
                        }
                    });

                }else {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart").child(user.getUid()).child("books").child(data.getBid());
                    reference.removeValue();
                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        reference2.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
//                                if (error != null) {
//                                    System.out.println("Copy failed");
//                                } else {

//                                    System.out.println("Success");
//
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });

            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert user != null;
                plusOnclick(user.getUid(), data, holder);
            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                assert user != null;
                minusOnclick(user.getUid(), data, holder);
            }
        });


    }

    private void minusOnclick(String uid, StoreCart_Model data, StoreCart_VH holder) {
        int quantity = Integer.parseInt(data.getQuantity());
        int price = Integer.parseInt((String) data.getPrice())/quantity;
        if (quantity <= 1) {
            holder.minus.setClickable(false);
        } else {
            holder.minus.setClickable(true);
            quantity-=1;


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("books").child(data.getBid());

            price*=quantity;
            int finalQuantity = quantity;
            int finalPrice = price;
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() &&snapshot.hasChild("bid")){

                        reference.child("quantity").setValue(String.valueOf(finalQuantity));
                        reference.child("price").setValue(String.valueOf(finalPrice));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void plusOnclick(String uid, StoreCart_Model data, StoreCart_VH holder) {
        int quantity = Integer.parseInt(data.getQuantity());
        int price = Integer.parseInt((String) data.getPrice())/quantity;
        if (quantity >= 10) {
            holder.plus.setClickable(false);
        } else {
            holder.plus.setClickable(true);
            quantity+=1;
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart").child(uid).child("books").child(data.getBid());

            price*=quantity;

            int finalQuantity = quantity;
            int finalPrice = price;
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() &&snapshot.hasChild("bid")){

                        reference.child("quantity").setValue(String.valueOf(finalQuantity));
                        reference.child("price").setValue(String.valueOf(finalPrice));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return storeCart_models.size();
    }
}


class StoreCart_VH extends RecyclerView.ViewHolder {

    ImageView cover;
    TextView aName, bName, price, quantity;
    Button plus, minus, deleteBtn;

    public StoreCart_VH(@NonNull View itemView) {
        super(itemView);
        cover = itemView.findViewById(R.id.cover);
        aName = itemView.findViewById(R.id.aName);
        bName = itemView.findViewById(R.id.bName);
        price = itemView.findViewById(R.id.price);
        quantity = itemView.findViewById(R.id.quantity);
        plus = itemView.findViewById(R.id.plus);
        minus = itemView.findViewById(R.id.minus);
        deleteBtn = itemView.findViewById(R.id.deleteBtn);
    }
}
