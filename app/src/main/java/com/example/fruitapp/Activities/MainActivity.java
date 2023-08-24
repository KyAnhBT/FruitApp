package com.example.fruitapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.service.controls.templates.ThumbnailTemplate;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fruitapp.R;
import com.example.fruitapp.fragment.BlogFragment;
import com.example.fruitapp.fragment.FruitFragment;
import com.example.fruitapp.fragment.GameFragment;
import com.example.fruitapp.fragment.LearnFragment;
import com.example.fruitapp.ml.Model;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private  static final  int FRAGMENT_FRUIT = 0;
    private  static final  int FRAGMENT_LEARN = 1;
    private  static final  int FRAGMENT_BLOG = 2;
    private  static final  int FRAGMENT_GAME = 3;

    FirebaseUser cUser;
    FirebaseAuth mAuth;

    private int mCurrentFragment = FRAGMENT_FRUIT;


    private DrawerLayout mDrawerLayout;

    int imageSize = 180 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();

        replaceFragment(new FruitFragment());
        getSupportActionBar().setTitle("Fruit Classification");
        navigationView.getMenu().findItem(R.id.nav_fruit).setChecked(true);

        updateNavHeader();

            }

       @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_fruit){
            if(mCurrentFragment != FRAGMENT_FRUIT){
                replaceFragment(new FruitFragment());
                mCurrentFragment = FRAGMENT_FRUIT;
                getSupportActionBar().setTitle("Fruit Classification");
            }
        } else if(id == R.id.nav_learn){
            if(mCurrentFragment != FRAGMENT_LEARN){
                replaceFragment(new LearnFragment());
                mCurrentFragment = FRAGMENT_LEARN;
                getSupportActionBar().setTitle("Learning");
            }
        } else if(id == R.id.nav_game){
            if(mCurrentFragment != FRAGMENT_GAME) {
                replaceFragment(new GameFragment());
                mCurrentFragment = FRAGMENT_GAME;
                getSupportActionBar().setTitle("Game");
            }
        } else if(id == R.id.nav_blog){
            if(mCurrentFragment != FRAGMENT_BLOG) {
                replaceFragment(new BlogFragment());
                mCurrentFragment = FRAGMENT_BLOG;
                getSupportActionBar().setTitle("Blog");
            }
        } else if(id == R.id.nav_logout){
            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, Login.class);
            startActivity(i);
            finish();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame,fragment);
        transaction.commit();
    }
    public void updateNavHeader (){
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        TextView navUsermail = headerView.findViewById(R.id.nav_usermail);
        ImageView navUserphoto = headerView.findViewById(R.id.nav_userphoto);
        Button btRank = headerView.findViewById(R.id.btRank);

        btRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Rank.class);
                startActivity(i);
            }
        });

        navUsermail.setText(cUser.getEmail());
        navUsername.setText(cUser.getDisplayName());
        Glide.with(this).load(cUser.getPhotoUrl()).into(navUserphoto);
    }
}