package com.example.fruitapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText edtPass, edtEmail;
    private ProgressBar pgBar;

    private TextView tvRegister;
    private Button btLogin;

    private FirebaseAuth mAuth;

    private Intent Home;
    ImageView imgAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imgAvatar = findViewById(R.id.imgAvatar);
        edtPass = findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);
        pgBar = findViewById(R.id.pgBar2);
        btLogin = findViewById(R.id.btLogin);
        tvRegister = findViewById(R.id.tvRegister);

        mAuth = FirebaseAuth.getInstance();
        Home = new Intent(this, MainActivity.class);

        pgBar.setVisibility(View.INVISIBLE);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                startActivity(i);
                finish();
            }
        });


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pgBar.setVisibility(View.VISIBLE);
                btLogin.setVisibility(View.INVISIBLE);

                final  String mail = edtEmail.getText().toString();
                final  String pass = edtPass.getText().toString();

                if (mail.isEmpty() || pass.isEmpty()){
                    showMessage("Please Verify All Field");
                    btLogin.setVisibility(View.VISIBLE);
                    pgBar.setVisibility(View.INVISIBLE);
                }else {
                    signIn(mail,pass);
                }
            }
        });
    }

    private void signIn(String mail, String pass) {
        mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    pgBar.setVisibility(View.INVISIBLE);
                    btLogin.setVisibility(View.VISIBLE);
                    updateUI();
                }else {
                    showMessage(task.getException().getMessage());
                    btLogin.setVisibility(View.VISIBLE);
                    pgBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        startActivity(Home);
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }
    @Override
    protected  void onStart() {

        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null){
            updateUI();
        }
    }
}