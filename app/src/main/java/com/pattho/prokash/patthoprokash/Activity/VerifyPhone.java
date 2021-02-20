package com.pattho.prokash.patthoprokash.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pattho.prokash.patthoprokash.R;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhone extends AppCompatActivity {

    EditText code;
    Button Verify;


    FirebaseAuth auth;
    String systemCode;
    String phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        auth = FirebaseAuth.getInstance();
        initData();
        Intent intent = getIntent();
        phoneNo = intent.getStringExtra("SignIn_PhoneNumber");

        SendOTP(phoneNo);


        OnClickData();
    }

    private void SendOTP(String phoneNo) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNo)       // Phone number to verify
                        .setTimeout(100L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
                    systemCode = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhone.this, "Error: "+e, Toast.LENGTH_LONG).show();

        }
    };



    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(VerifyPhone.this,UpdateUserDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else Toast.makeText(VerifyPhone.this, "Error: "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verifyCode(String otp_code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(systemCode, otp_code);
        signInWithCredential(credential);


    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(VerifyPhone.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(VerifyPhone.this,UpdateUserDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }else Toast.makeText(VerifyPhone.this, "Error: "+ Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void OnClickData() {

        Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = code.getText().toString();
                if (otp.isEmpty()){
                    code.setError("Please Enter Verification Code.");
                }else verifyCode(otp);
            }
        });
    }

    private void initData() {
        code = findViewById(R.id.SignIn_emailEditTextField);
        Verify = findViewById(R.id.SignIn_signInBtn);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}