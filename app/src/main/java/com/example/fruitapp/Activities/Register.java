package com.example.fruitapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {

    ImageView imgAvatar;
    private TextView tvLogin;
    static  int PReqCode = 1;
    static  int REQUESTCODE = 1;
    Uri pickerImgUri ;

    private EditText edtName, edtPass, edtEmail, edtConfirm;
    private ProgressBar pgBar;
    private Button btRegister;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        imgAvatar = findViewById(R.id.imgAvatar);
        edtName = findViewById(R.id.edtName);
        edtPass = findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtConfirm = findViewById(R.id.edtConfirm);
        pgBar = findViewById(R.id.pgBar);
        btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tvLogin);

        pgBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });


        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btRegister.setVisibility(View.INVISIBLE);
                pgBar.setVisibility(View.VISIBLE);
                final String email = edtEmail.getText().toString();
                final String pass = edtPass.getText().toString();
                final String confirm = edtConfirm.getText().toString();
                final String name = edtName.getText().toString();

                if(email.isEmpty() || name.isEmpty() || pass.isEmpty() || !confirm.equals(pass)){

                    showMessage("Please Verify all fields");
                    btRegister.setVisibility(View.VISIBLE);
                    pgBar.setVisibility(View.INVISIBLE);

                }else {
                    CreateUser(email,name,pass);
                }

            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission();
                }else {
                    openGallery();
                }

            }
        });
    }

    private void CreateUser(String email, String name, String pass) {

        mAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            showMessage("Account create");
                            updateUserInfo(name, pickerImgUri, mAuth.getCurrentUser());
                        }else {
                            showMessage("account creation failed" + task.getException().getMessage());
                            btRegister.setVisibility(View.VISIBLE);
                            pgBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });



    }

    private void updateUserInfo(String name, Uri pickerImgUri, FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photo");
        StorageReference imageFilePath = mStorage.child(pickerImgUri.getLastPathSegment());
        imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate= new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            showMessage("Register Complete");
                                            updateUI();
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    private void updateUI() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class) ;
        startActivity(intent);
        finish();
    }

    private void showMessage(String m) {
        Toast.makeText(getApplicationContext(), m, Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUESTCODE);
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(Register.this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(Register.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(Register.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else {
            openGallery();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            pickerImgUri = data.getData();
            imgAvatar.setImageURI(pickerImgUri);
        }
    }
}