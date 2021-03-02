package com.pattho.prokash.patthoprokash.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pattho.prokash.patthoprokash.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserDetails extends AppCompatActivity {

    EditText Fname, Address, Email, Pass;
    Button updateInfoBtn;
    String fname, phone, address, email, url;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    Button imgChose;
    CircleImageView img1;
    Context context = this;
    String s="s";

    StorageReference storageReference;


    Uri profilePicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_details);
        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        s = intent.getStringExtra("1");
        initData();
        OnClickData();
    }


    private void OnClickData() {


        imgChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(UpdateUserDetails.this);
            }
        });

        updateInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadData();
            }


        });

    }

    private void uploadData() {

        fname = Fname.getText().toString() + "";

        address = Address.getText().toString() + "";
        email = Email.getText().toString() + "";


        FirebaseUser firebaseUser = auth.getCurrentUser();
        assert firebaseUser != null;
        String userId = firebaseUser.getUid();
        System.out.println(userId);
        phone = firebaseUser.getPhoneNumber() + "";

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);



        HashMap<String, Object> data = new HashMap<>();
        data.put("name", fname);
        data.put("phone", phone);
        data.put("address", address);
        data.put("email", email);
        data.put("pic", "");
        data.put("id", userId);

        databaseReference.updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(context, "Update Success...!", Toast.LENGTH_SHORT).show();
                    if (s!=null) {
                        if (s.equals("1")) {
                            Intent intent = new Intent(UpdateUserDetails.this, LandingActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(UpdateUserDetails.this, "You can't sign up with this Email or Password.......", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateUserDetails.this, "Error... " + e, Toast.LENGTH_SHORT).show();
            }
        });


        if (profilePicUri != null) {

            Uri imageData = profilePicUri;
            Bitmap fullSizeImage = null;
            try {
                fullSizeImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = Bitmap.createScaledBitmap(fullSizeImage, 400, 400, false);
            File reducedFile = null;
            try {
                reducedFile = getBitmapFile(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference imageName = storageReference.child("UserImage").child(userId).child("img.jpg");
            imageName.putFile(Uri.fromFile(reducedFile)).addOnSuccessListener(taskSnapshot ->
                    imageName.getDownloadUrl().addOnSuccessListener(uri -> {
                        String img_link = uri + "";
                        databaseReference.child("pic").setValue(img_link).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateUserDetails.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }));

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            profilePicUri = result.getUri();
            img1.setImageURI(profilePicUri);
        }
    }


    private File getBitmapFile(Bitmap reducedImageBitmap) throws IOException {
        File file = new File(context.getCacheDir(), "temp55566511.jpeg");
        file.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        reducedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapdata);
        fos.flush();
        fos.close();
        return file;
    }

    private void initData() {
        Fname = findViewById(R.id.name);
        Address = findViewById(R.id.address);
        Email = findViewById(R.id.phone);
        imgChose = findViewById(R.id.choseProfileImgBtn);
        img1 = findViewById(R.id.profile_image);

        updateInfoBtn = findViewById(R.id.SignUp_signUpBtn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Fname.setText(snapshot.child("name").getValue().toString() + "");
                    Address.setText(snapshot.child("address").getValue().toString() + "");
                    Email.setText(snapshot.child("email").getValue().toString() + "");
                    url = snapshot.child("pic").getValue().toString() + "";
                }else uploadData();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (s != null) {
            if (s.equals("1")) {
                Intent intent = new Intent(this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}