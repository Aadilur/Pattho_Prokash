package com.pattho.prokash.patthoprokash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.pattho.prokash.patthoprokash.R;

public class SignIn extends AppCompatActivity {


    EditText Phone;
    Button SignIn;


    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();


        initData();
        OnClickData();
    }

    private void OnClickData() {

            SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String num = "+880" + Phone.getText().toString();
                    System.out.println(num.length());
                    if (num.length() == 14) {






                    Intent intent = new Intent(getApplicationContext(), VerifyPhone.class);
                    intent.putExtra("SignIn_PhoneNumber",num );
                    startActivity(intent);
                    }else Toast.makeText(SignIn.this, "Please Check Your Number.", Toast.LENGTH_SHORT).show();
                }
            });


    }

    private void initData() {
        Phone = findViewById(R.id.SignIn_emailEditTextField);
        SignIn = findViewById(R.id.SignIn_signInBtn);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}