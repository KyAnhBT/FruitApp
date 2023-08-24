package com.example.fruitapp.fragment;

import static android.app.Activity.RESULT_OK;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fruitapp.Activities.Register;
import com.example.fruitapp.Adapters.PostAdapter;
import com.example.fruitapp.Models.Post;
import com.example.fruitapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {
    FirebaseAuth mAuth;
    FirebaseUser currenUser;
    Dialog popAddPost;
    ImageView popupUserImage, popupPosImage, popupAddBtn;
    TextView popupTitle,popupDesription;
    ProgressBar popupClickProgress;
    private static final int PReqCode = 2;
    private static final int REQUESTCODE = 2;
    private Uri pickerImgUri = null;

    RecyclerView recyclerView ;
    PostAdapter postAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<Post> postList;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);
        recyclerView = view.findViewById(R.id.postRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        firebaseDatabase  = FirebaseDatabase.getInstance("https://blogapp-72454-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference("Posts");
        mAuth = FirebaseAuth.getInstance();
        currenUser = mAuth.getCurrentUser();


        iniPopup();
        setupPopupImageClick();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });
        return view;
    }

    @Override
    public void  onStart(){
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot psnap : snapshot.getChildren()){
                    Post post = psnap.getValue(Post.class);
                    postList.add(post) ;
                }
                postAdapter = new PostAdapter(getActivity(),postList);
                recyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupPopupImageClick() {
        popupPosImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndRequestForPermission();
            }
        });
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(getActivity(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        }else {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUESTCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESTCODE && data != null){
            pickerImgUri = data.getData();
            popupPosImage.setImageURI(pickerImgUri);
        }
    }

    private void iniPopup() {
        popAddPost = new Dialog(getActivity());
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        popupUserImage = popAddPost.findViewById(R.id.popup_user_image);
        popupPosImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDesription = popAddPost.findViewById(R.id.popup_decription);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);


        Glide.with(getActivity()).load(currenUser.getPhotoUrl()).into(popupUserImage);

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupClickProgress.setVisibility(View.VISIBLE);
                popupAddBtn.setVisibility(View.INVISIBLE);

                if (!popupTitle.getText().toString().isEmpty() && !popupDesription.getText().toString().isEmpty() && pickerImgUri != null){
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    StorageReference imageFilePath = storageReference.child(pickerImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickerImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String ImageDownloadLink = uri.toString();
                                    Post post = new Post(popupTitle.getText().toString(),
                                            popupDesription.getText().toString(),
                                            ImageDownloadLink,
                                            currenUser.getUid(),
                                            currenUser.getPhotoUrl().toString()
                                            );
                                    addPost(post);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }


                    });
                }else{
                    showMessage("Please verify all input fields and choose Post Image");
                    popupClickProgress.setVisibility(View.INVISIBLE);
                    popupAddBtn.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://blogapp-72454-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myref = database.getReference("Posts").push();

        String key = myref.getKey();
        post.setPostKey(key);

        myref.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message,Toast.LENGTH_LONG).show();
    }
}
