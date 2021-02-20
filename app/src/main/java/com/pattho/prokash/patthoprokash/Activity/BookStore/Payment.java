package com.pattho.prokash.patthoprokash.Activity.BookStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.pattho.prokash.patthoprokash.R;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {


    TextView Amount,account;
    RadioGroup radioGroup;
    RadioButton bkash,rocket;

    DatabaseReference databaseReference;

    TextInputEditText Txd, PaymentAccount, shippingAddress,phone;
    Button payBtn;
    int amount,num;
    String bid,paymentMethod="bKash";
    String author,book,cvr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("pendingOrder");
        initData();
        onClickData();

    }



    @SuppressLint("SetTextI18n")
    private void onClickData() {


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.bkash) {
                account.setText("bKash : 01725402592 (Personal)");
                paymentMethod = "bKash";
            } else {
                account.setText("Rocket : 017254025922 (Personal)");
                paymentMethod = "Rocket";
            }
        });

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txid,paymentAccount,shippingAdd,phoneNum;
                txid = Txd.getText().toString();
                paymentAccount = PaymentAccount.getText().toString();
                shippingAdd = shippingAddress.getText().toString();
                phoneNum = phone.getText().toString();
                if (txid.equals("") || paymentAccount.equals("") || shippingAdd.equals("") || phoneNum.equals("")){
                    Toast.makeText(Payment.this, "All Field Are Required", Toast.LENGTH_SHORT).show();
                }else {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final String uid = user.getUid();

                    Map<String,Object > data = new HashMap<>();
                    data.put("bid",bid);
                    data.put("time", ServerValue.TIMESTAMP);
                    data.put("Txid",txid);
                    data.put("paymentMethod",paymentMethod);
                    data.put("paymentAccount",paymentAccount);
                    data.put("shipping",shippingAdd);
                    data.put("contact",phoneNum);
                    data.put("amount",amount);
                    data.put("status","pending");
                    data.put("itemNum",num);
                    data.put("uid",uid);
                    data.put("bName",book);
                    data.put("aName",author);
                    data.put("cover",cvr);

                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.child(paymentMethod+":"+txid).exists()){

                                databaseReference.child(paymentMethod+":"+txid).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Payment.this, "Order Placed Successfully...", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(Payment.this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }else Toast.makeText(Payment.this, "Your Transaction ID Already Exist...", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }



    @SuppressLint("SetTextI18n")
    private void initData() {
        Amount = findViewById(R.id.ammount);

        radioGroup = findViewById(R.id.radioGroup);
        bkash = findViewById(R.id.bkash);
        rocket = findViewById(R.id.rocket);
        account = findViewById(R.id.account);

        Txd = findViewById(R.id.transaction_id);
        PaymentAccount = findViewById(R.id.clientBkash);
        shippingAddress = findViewById(R.id.user_address);
        phone = findViewById(R.id.phone);

        payBtn = findViewById(R.id.payBtn);


        Intent intent = getIntent();
        Amount.setText("Pay Taka : BDT "+intent.getIntExtra("amount",0)+"৳ /=");
        amount = intent.getIntExtra("amount",0);
        num = intent.getIntExtra("num",0);
        bid = intent.getStringExtra("bid");

        book = intent.getStringExtra("bName");
        author = intent.getStringExtra("aName");
        cvr = intent.getStringExtra("cover");
    }


    }
