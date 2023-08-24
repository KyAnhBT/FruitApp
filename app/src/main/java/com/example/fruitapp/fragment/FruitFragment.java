package com.example.fruitapp.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.fruitapp.R;
import com.example.fruitapp.ml.Model;
import com.google.android.material.navigation.NavigationView;
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

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class FruitFragment extends Fragment {
    Button camera, gallery, wiki,btsc,bt1,bt2,down,share;
    ImageView imageView,i1,i2,i3,imgNut,imgd;
    TextView result,tt,tfact,fact,ttre,tvInf,tvFact,cre,tvsc;
    VideoView vd;
    int imageSize = 180 ;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser currentUser;
    MediaController mediaController;
    LinearLayout ln1;
    Spinner spinner;

    ScrollView sc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fruit, container, false);

        camera = view.findViewById(R.id.button);
        gallery = view.findViewById(R.id.button2);
        result = view.findViewById(R.id.result);
        sc = view.findViewById(R.id.sc);

        ttre = view.findViewById(R.id.classified);
        imageView = view.findViewById(R.id.imageView);
        cre = view.findViewById(R.id.cre);
        i1 = view.findViewById(R.id.i1);
        i2 = view.findViewById(R.id.i2);
        i3 = view.findViewById(R.id.i3);
        tvInf = view.findViewById(R.id.tvInf);
        tvFact = view.findViewById(R.id.tvFact);
        imgNut = view.findViewById(R.id.imgNut);
        imgd = view.findViewById(R.id.imgd);
        wiki = view.findViewById(R.id.wiki);
        btsc = view.findViewById(R.id.btsc);
        vd = view.findViewById(R.id.vd);
        ln1 = view.findViewById(R.id.ln1);
        spinner = view.findViewById(R.id.spinner);
        tvsc = view.findViewById(R.id.tvsc);
        bt1 = view.findViewById(R.id.bt1);
        bt2 = view.findViewById(R.id.bt2);
        down = view.findViewById(R.id.down);
        share = view.findViewById(R.id.share);



        btsc.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        tvsc.setVisibility(View.INVISIBLE);
        bt1.setVisibility(View.INVISIBLE);
        bt2.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);



        //spinner.setOnItemSelectedListener();

        List<String> categories = new ArrayList<String>();
        categories.add("Apple");
        categories.add("Avocado");
        categories.add("Banana");
        categories.add("Coconut");
        categories.add("Cherry");
        categories.add("Dragon Fruit");
        categories.add("Durian");
        categories.add("Grape");
        categories.add("Guava");
        categories.add("Kiwi");
        categories.add("Lime");
        categories.add("Longan");
        categories.add("Lychee");
        categories.add("Mango");
        categories.add("Mangosteen");
        categories.add("Orange");
        categories.add("Papaya");
        categories.add("Passion fruit");
        categories.add("Pineapple");
        categories.add("Pomelo");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        mediaController = new MediaController(getActivity());

        NavigationView navigationView = getActivity().findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView point = headerView.findViewById(R.id.point);
        TextView tvRank = headerView.findViewById(R.id.tvRank);
        ImageView imgRank = headerView.findViewById(R.id.imgRank);
        firebaseDatabase  = FirebaseDatabase.getInstance("https://blogapp-72454-default-rtdb.asia-southeast1.firebasedatabase.app/");
        databaseReference = firebaseDatabase.getReference().child("clickCounts");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int clickCount2 = snapshot.getValue(Integer.class);
                    point.setText(""+clickCount2);
                    if (clickCount2>=30) {
                        imgRank.setImageResource(R.drawable.rank3);
                        tvRank.setText("Legendary");
                        btsc.setVisibility(View.VISIBLE);
                    }else if(clickCount2<30 && clickCount2 >=15){
                        imgRank.setImageResource(R.drawable.rank2);
                        tvRank.setText("Pro");
                    }else {
                        imgRank.setImageResource(R.drawable.rank1);
                        tvRank.setText("Newbie");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                tvsc.setVisibility(View.VISIBLE);
                bt1.setVisibility(View.VISIBLE);
                bt2.setVisibility(View.VISIBLE);
                btsc.setVisibility(View.INVISIBLE);
                ln1.setVisibility(View.INVISIBLE);
                camera.setVisibility(View.INVISIBLE);
                gallery.setVisibility(View.INVISIBLE);
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item=String.valueOf(spinner.getSelectedItem());
                result.setText(item);
                sc.setVisibility(View.VISIBLE);
                img();
                des();
                nutri();
                dd();
                vd();
                fact();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result.setVisibility(View.INVISIBLE);
                sc.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                tvsc.setVisibility(View.INVISIBLE);
                bt1.setVisibility(View.INVISIBLE);
                bt2.setVisibility(View.INVISIBLE);
                btsc.setVisibility(View.VISIBLE);
                ln1.setVisibility(View.VISIBLE);
                camera.setVisibility(View.VISIBLE);
                gallery.setVisibility(View.VISIBLE);
            }
        });




        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(ContextCompat.checkSelfPermission(getActivity(),android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 3);
                }else{
                    ActivityCompat.requestPermissions(getActivity(),new String[]{android.Manifest.permission.CAMERA}, 100);
                }

            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(cameraIntent, 1);


            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImageToOtherApps();
            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());

                imageView.setDrawingCacheEnabled(false);
                String savedImageURL = MediaStore.Images.Media.insertImage(
                        getActivity().getContentResolver(),
                        bitmap,
                        result.getText().toString(),
                        "Ảnh lưu từ fruit app"

                );

                if (savedImageURL != null) {
                    // Hiển thị thông báo cho người dùng
                    Toast.makeText(getActivity(), "Lưu ảnh thành công!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final MediaPlayer apple = MediaPlayer.create(getActivity(),R.raw.apple);
        final MediaPlayer avocado = MediaPlayer.create(getActivity(),R.raw.avocado);
        final MediaPlayer banana = MediaPlayer.create(getActivity(),R.raw.banana);
        final MediaPlayer coconut = MediaPlayer.create(getActivity(),R.raw.coconut);
        final MediaPlayer cherry = MediaPlayer.create(getActivity(),R.raw.cherry);
        final MediaPlayer dragonfruit = MediaPlayer.create(getActivity(),R.raw.dragonfruit);
        final MediaPlayer durian = MediaPlayer.create(getActivity(),R.raw.durian);
        final MediaPlayer grape = MediaPlayer.create(getActivity(),R.raw.grape);
        final MediaPlayer guava = MediaPlayer.create(getActivity(),R.raw.guava);
        final MediaPlayer kiwi = MediaPlayer.create(getActivity(),R.raw.kiwi);
        final MediaPlayer lime = MediaPlayer.create(getActivity(),R.raw.lime);
        final MediaPlayer longan = MediaPlayer.create(getActivity(),R.raw.longan);
        final MediaPlayer lychee = MediaPlayer.create(getActivity(),R.raw.lychee);
        final MediaPlayer mango = MediaPlayer.create(getActivity(),R.raw.mango);
        final MediaPlayer mangosteen = MediaPlayer.create(getActivity(),R.raw.mangosteen);
        final MediaPlayer orange = MediaPlayer.create(getActivity(),R.raw.ogrange);
        final MediaPlayer papaya = MediaPlayer.create(getActivity(),R.raw.papaya);
        final MediaPlayer passionfruit = MediaPlayer.create(getActivity(),R.raw.passionfruit);
        final MediaPlayer pineapple = MediaPlayer.create(getActivity(),R.raw.pineapple);
        final MediaPlayer pomelo = MediaPlayer.create(getActivity(),R.raw.pomelo);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(result.getText().toString()) {
                    case "Passion fruit":
                        passionfruit.start();
                        break;
                    case "Apple":
                        apple.start();
                        break;
                    case "Avocado":
                        avocado.start();
                        break;
                    case "Banana":
                        banana.start();
                        break;
                    case "Coconut":
                        coconut.start();
                        break;
                    case "Cherry":
                        cherry.start();
                        break;
                    case "Dragon Fruit":
                        dragonfruit.start();
                        break;
                    case "Durian":
                        durian.start();
                        break;
                    case "Grape":
                        grape.start();
                        break;
                    case "Guava":
                        guava.start();
                        break;
                    case "Kiwi":
                        kiwi.start();
                        break;
                    case "Lime":
                        lime.start();
                        break;
                    case "Longan":
                        longan.start();
                        break;
                    case "Lychee":
                        lychee.start();
                        break;
                    case "Mango":
                        mango.start();
                        break;
                    case "Mangosteen":
                        mangosteen.start();
                        break;
                    case "Orange":
                        orange.start();
                        break;
                    case "Papaya":
                        papaya.start();
                        break;
                    case "Pineapple":
                        pineapple.start();
                        break;
                    case "Pomelo":
                        pomelo.start();
                        break;
                    default:
                }
            }
        });
        wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String websiteUrl = "";

                switch(result.getText().toString()) {
                    case "Passion fruit":
                        websiteUrl = "https://en.wikipedia.org/wiki/Passion_fruit_(fruit)";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Apple":
                        websiteUrl = "https://en.wikipedia.org/wiki/Apple";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Avocado":
                        websiteUrl = "https://en.wikipedia.org/wiki/Avocado";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Banana":
                        websiteUrl = "https://en.wikipedia.org/wiki/Banana";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Coconut":
                        websiteUrl = "https://en.wikipedia.org/wiki/Coconut";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Cherry":
                        websiteUrl = "https://en.wikipedia.org/wiki/Cherry";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Dragon Fruit":
                        websiteUrl = "https://en.wikipedia.org/wiki/Pitaya";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Durian":
                        websiteUrl = "https://en.wikipedia.org/wiki/Durian";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Grape":
                        websiteUrl = "https://en.wikipedia.org/wiki/Grape";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Guava":
                        websiteUrl = "https://en.wikipedia.org/wiki/Guava";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Kiwi":
                        websiteUrl = "https://en.wikipedia.org/wiki/Kiwi";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Lime":
                        websiteUrl = "https://en.wikipedia.org/wiki/Lime_(fruit)";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Longan":
                        websiteUrl = "https://en.wikipedia.org/wiki/Longan";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Lychee":
                        websiteUrl = "https://en.wikipedia.org/wiki/Lychee";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Mango":
                        websiteUrl = "https://en.wikipedia.org/wiki/Mango";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Mangosteen":
                        websiteUrl = "https://en.wikipedia.org/wiki/Mangosteen";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Orange":
                        websiteUrl = "https://en.wikipedia.org/wiki/Orange_(fruit)";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Papaya":
                        websiteUrl = "https://en.wikipedia.org/wiki/Papaya";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Pineapple":
                        websiteUrl = "https://en.wikipedia.org/wiki/Pineapple";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    case "Pomelo":
                        websiteUrl = "https://en.wikipedia.org/wiki/Pomelo";
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl)));
                        break;
                    default:
                }
            }
        });



        return view;
    }




    private void des(){
        switch(result.getText().toString()) {
            case "Banana":
                tvInf.setText(R.string.band);
                break;
            case "Dragon Fruit":
                tvInf.setText(R.string.drad);
                break;
            case "Passion fruit":
                tvInf.setText(R.string.pasd);
            break;
            case "Apple":
                tvInf.setText(R.string.appd);
                break;
            case "Avocado":
                tvInf.setText(R.string.avod);
                break;
            case "Coconut":
                tvInf.setText(R.string.cocd);
                break;
            case "Cherry":
                tvInf.setText(R.string.ched);
                break;

            case "Durian":
                tvInf.setText(R.string.durd);
                break;
            case "Grape":
                tvInf.setText(R.string.grad);
                break;
            case "Guava":
                tvInf.setText(R.string.guad);
                break;
            case "Kiwi":
                tvInf.setText(R.string.kiwd);
                break;
            case "Lime":
                tvInf.setText(R.string.limd);
                break;
            case "Longan":
                tvInf.setText(R.string.lond);
                break;
            case "Lychee":
                tvInf.setText(R.string.lycd);
                break;
            case "Mango":
                tvInf.setText(R.string.mand);
                break;
            case "Mangosteen":
                tvInf.setText(R.string.masd);
                break;
            case "Orange":
                tvInf.setText(R.string.orad);
                break;
            case "Papaya":
                tvInf.setText(R.string.papd);
                break;
            case "Pineapple":
                tvInf.setText(R.string.pind);
                break;
            case "Pomelo":
                tvInf.setText(R.string.pomd);
                break;
            default:
                ;

        }
    }
    private void img(){
        switch(result.getText().toString()) {
            case "Banana":
                i1.setImageResource(R.drawable.ban1);
                i2.setImageResource(R.drawable.ban2);
                i3.setImageResource(R.drawable.ban3);
                break;
            case "Dragon Fruit":
                i1.setImageResource(R.drawable.dra1);
                i2.setImageResource(R.drawable.dra2);
                i3.setImageResource(R.drawable.dra3);
                break;
            case "Passion fruit":
                i1.setImageResource(R.drawable.pas1);
                i2.setImageResource(R.drawable.pas2);
                i3.setImageResource(R.drawable.pas3);
                break;
            case "Apple":
                i1.setImageResource(R.drawable.app1);
                i2.setImageResource(R.drawable.app2);
                i3.setImageResource(R.drawable.app3);
                break;
            case "Avocado":
                i1.setImageResource(R.drawable.avo1);
                i2.setImageResource(R.drawable.avo2);
                i3.setImageResource(R.drawable.avo3);
                break;
            case "Coconut":
                i1.setImageResource(R.drawable.coc1);
                i2.setImageResource(R.drawable.coc2);
                i3.setImageResource(R.drawable.coc3);
                break;
            case "Cherry":
                i1.setImageResource(R.drawable.che1);
                i2.setImageResource(R.drawable.che2);
                i3.setImageResource(R.drawable.che3);
                break;
            case "Durian":
                i1.setImageResource(R.drawable.dur1);
                i2.setImageResource(R.drawable.dur2);
                i3.setImageResource(R.drawable.dur3);
                break;
            case "Grape":
                i1.setImageResource(R.drawable.gra1);
                i2.setImageResource(R.drawable.gra2);
                i3.setImageResource(R.drawable.gra3);
                break;
            case "Guava":
                i1.setImageResource(R.drawable.gua1);
                i2.setImageResource(R.drawable.gua2);
                i3.setImageResource(R.drawable.gua3);
                break;
            case "Kiwi":
                i1.setImageResource(R.drawable.kiw1);
                i2.setImageResource(R.drawable.kiw2);
                i3.setImageResource(R.drawable.kiw3);
                break;
            case "Lime":
                i1.setImageResource(R.drawable.lim1);
                i2.setImageResource(R.drawable.lim2);
                i3.setImageResource(R.drawable.lim3);
                break;
            case "Longan":
                i1.setImageResource(R.drawable.lon1);
                i2.setImageResource(R.drawable.lon2);
                i3.setImageResource(R.drawable.lon3);
                break;
            case "Lychee":
                i1.setImageResource(R.drawable.lyc1);
                i2.setImageResource(R.drawable.lyc2);
                i3.setImageResource(R.drawable.lyc3);
                break;
            case "Mango":
                i1.setImageResource(R.drawable.man1);
                i2.setImageResource(R.drawable.man2);
                i3.setImageResource(R.drawable.man3);
                break;
            case "Mangosteen":
                i1.setImageResource(R.drawable.mas1);
                i2.setImageResource(R.drawable.mas2);
                i3.setImageResource(R.drawable.mas3);
                break;
            case "Orange":
                i1.setImageResource(R.drawable.ora1);
                i2.setImageResource(R.drawable.ora2);
                i3.setImageResource(R.drawable.ora3);
                break;
            case "Papaya":
                i1.setImageResource(R.drawable.pap1);
                i2.setImageResource(R.drawable.pap2);
                i3.setImageResource(R.drawable.pap3);
                break;
            case "Pineapple":
                i1.setImageResource(R.drawable.pin1);
                i2.setImageResource(R.drawable.pin2);
                i3.setImageResource(R.drawable.pin3);
                break;
            case "Pomelo":
                i1.setImageResource(R.drawable.pom1);
                i2.setImageResource(R.drawable.pom2);
                i3.setImageResource(R.drawable.pom3);
                break;
            default:
                ;
        }
    }
    private void fact(){
        switch(result.getText().toString()) {
            case "Banana":
                tvFact.setText(R.string.banf);
                break;
            case "Dragon Fruit":
                tvFact.setText(R.string.draf);
                break;
            case "Passion fruit":
                tvFact.setText(R.string.pasf);
                break;
            case "Apple":
                tvFact.setText(R.string.appf);
                break;
            case "Avocado":
                tvFact.setText(R.string.avof);
                break;

            case "Coconut":
                tvFact.setText(R.string.cocf);
                break;
            case "Cherry":
                tvFact.setText(R.string.chef);
                break;

            case "Durian":
                tvFact.setText(R.string.durf);
                break;
            case "Grape":
                tvFact.setText(R.string.graf);
                break;
            case "Guava":
                tvFact.setText(R.string.guaf);
                break;
            case "Kiwi":
                tvFact.setText(R.string.kiwf);
                break;
            case "Lime":
                tvFact.setText(R.string.limf);
                break;
            case "Longan":
                tvFact.setText(R.string.lonf);
                break;
            case "Lychee":
                tvFact.setText(R.string.lycf);
                break;
            case "Mango":
                tvFact.setText(R.string.manf);
                break;
            case "Mangosteen":
                tvFact.setText(R.string.masf);
                break;
            case "Orange":
                tvFact.setText(R.string.oraf);
                break;
            case "Papaya":
                tvFact.setText(R.string.papf);
                break;
            case "Pineapple":
                tvFact.setText(R.string.pinf);
                break;
            case "Pomelo":
                tvFact.setText(R.string.pomf);
                break;
            default:
                ;
        }
    }

    private void nutri(){
        switch(result.getText().toString()) {
            case "Banana":
                imgNut.setImageResource(R.drawable.bann);
                break;
            case "Dragon Fruit":
                imgNut.setImageResource(R.drawable.dran);
                break;
            case "Passion fruit":
                imgNut.setImageResource(R.drawable.pasn);
                break;
            case "Apple":
                imgNut.setImageResource(R.drawable.appn);
                break;
            case "Avocado":
                imgNut.setImageResource(R.drawable.avon);
                break;

            case "Coconut":
                imgNut.setImageResource(R.drawable.cocn);
                break;
            case "Cherry":
                imgNut.setImageResource(R.drawable.chen);
                break;

            case "Durian":
                imgNut.setImageResource(R.drawable.durn);
                break;
            case "Grape":
                imgNut.setImageResource(R.drawable.gran);
                break;
            case "Guava":
                imgNut.setImageResource(R.drawable.guan);
                break;
            case "Kiwi":
                imgNut.setImageResource(R.drawable.kiwn);
                break;
            case "Lime":
                imgNut.setImageResource(R.drawable.limn);
                break;
            case "Longan":
                imgNut.setImageResource(R.drawable.lonn);
                break;
            case "Lychee":
                imgNut.setImageResource(R.drawable.lycn);
                break;
            case "Mango":
                imgNut.setImageResource(R.drawable.mann);
                break;
            case "Mangosteen":
                imgNut.setImageResource(R.drawable.masn);
                break;
            case "Orange":
                imgNut.setImageResource(R.drawable.oran);
                break;
            case "Papaya":
                imgNut.setImageResource(R.drawable.papn);
                break;
            case "Pineapple":
                imgNut.setImageResource(R.drawable.pinn);
                break;
            case "Pomelo":
                imgNut.setImageResource(R.drawable.pomn);
                break;
            default:
                ;
        }
    }

    private void dd(){
        switch(result.getText().toString()) {
            case "Banana":
                imgd.setImageResource(R.drawable.banb);
                break;
            case "Dragon Fruit":
                imgd.setImageResource(R.drawable.drab);
                break;
            case "Passion fruit":
                imgd.setImageResource(R.drawable.paskaka);
                break;
            case "Apple":
                imgd.setImageResource(R.drawable.appb);
                break;
            case "Avocado":
                imgd.setImageResource(R.drawable.avob);
                break;

            case "Coconut":
                imgd.setImageResource(R.drawable.cocb);
                break;
            case "Cherry":
                imgd.setImageResource(R.drawable.cheb);
                break;

            case "Durian":
                imgd.setImageResource(R.drawable.durb);
                break;
            case "Grape":
                imgd.setImageResource(R.drawable.grab);
                break;
            case "Guava":
                imgd.setImageResource(R.drawable.guab);
                break;
            case "Kiwi":
                imgd.setImageResource(R.drawable.kiwb);;
                break;
            case "Lime":
                imgd.setImageResource(R.drawable.limb);
                break;
            case "Longan":
                imgd.setImageResource(R.drawable.lonb);
                break;
            case "Lychee":
                imgd.setImageResource(R.drawable.lycb);
                break;
            case "Mango":
                imgd.setImageResource(R.drawable.manb);
                break;
            case "Mangosteen":
                imgd.setImageResource(R.drawable.masb);
                break;
            case "Orange":
                imgd.setImageResource(R.drawable.orab);
                break;
            case "Papaya":
                imgd.setImageResource(R.drawable.papb);
                break;
            case "Pineapple":
                imgd.setImageResource(R.drawable.pinb);
                break;
            case "Pomelo":
                imgd.setImageResource(R.drawable.pomb);
                break;
            default:
                ;
        }
    }

    private void vd(){
        String videoUrl="";
        switch(result.getText().toString()) {
            case "Banana":
                 videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fban.mp4?alt=media&token=0cda1226-2bd9-4c3a-a54e-b6f5ac8e9cf2";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);

                break;
            case "Dragon Fruit":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fdra.mp4?alt=media&token=42ee8145-9f4c-4597-891c-4a8e5eb04a71";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Passion fruit":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fpas.mp4?alt=media&token=60847a91-52ad-476d-a7f1-0c05f09caecd";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Apple":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fapple.mp4?alt=media&token=80b121c8-0008-4fc0-ae5e-0dcb71e03363";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Avocado":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Favo.mp4?alt=media&token=922b38bb-9bcb-4b3d-98b5-b7de639db155";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;

            case "Coconut":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fcoc.mp4?alt=media&token=302da7ae-7f5b-4e14-b507-14cd2dd36dfc";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Cherry":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fche.mp4?alt=media&token=308d90cf-dac5-4957-be3b-9478578eed20";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;

            case "Durian":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fdur.mp4?alt=media&token=6b7f6331-3502-4d93-844f-6713413c73db";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Grape":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fgra.mp4?alt=media&token=99ccc739-a3ec-4b34-a1e2-e93ff6005429";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Guava":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fgua.mp4?alt=media&token=b12bb1b3-6d56-433b-aac9-02aee27919dd";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Kiwi":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fkiw.mp4?alt=media&token=1f88ddf1-1286-4d4b-bd35-69929c2e05d2";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Lime":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Flim.mp4?alt=media&token=9631e882-70f9-49f5-9823-db0d81df1c1a";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Longan":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Flon.mp4?alt=media&token=79c05795-2b2b-4d6d-9bef-faa44945d6cc";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Lychee":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Flyc.mp4?alt=media&token=7e23d361-4d9f-46a5-a4db-7f898c6c9942";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Mango":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fman.mp4?alt=media&token=34a54e69-5538-40d4-a000-7f595db8daf9";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Mangosteen":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fmas.mp4?alt=media&token=53608b69-d3db-417e-82de-3fa08669fb39";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Orange":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fora.mp4?alt=media&token=72d5e17c-b0db-4ecd-b471-9b76f101ae44";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Papaya":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fpap.mp4?alt=media&token=24ad1cbd-342c-485e-aee8-041d0122be6d";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Pineapple":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fpin.mp4?alt=media&token=08a39ca6-0594-4b22-a7ce-81e1e169bece";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            case "Pomelo":
                videoUrl = "https://firebasestorage.googleapis.com/v0/b/blogapp-72454.appspot.com/o/fruit_cooking%2Fpom.mp4?alt=media&token=7d927d1b-b5a4-48fd-a352-8796615d290f";
                vd.setVideoURI(Uri.parse(videoUrl));
                vd.setMediaController(mediaController);
                mediaController.setAnchorView(vd);
                break;
            default:
                ;
        }
    }
    /*private void fact(){
        switch(result.getText().toString()) {
            case "Passion fruit":
                fact.setText(R.string.passionfruit);
                break;
            case "Apple":
                fact.setText(R.string.apple);
                break;
            case "Avocado":
                fact.setText(R.string.avocado);
                break;
            case "Banana":
                fact.setText(R.string.banana);
                break;
            case "Coconut":
                fact.setText(R.string.coconut);
                break;
            case "Cherry":
                fact.setText(R.string.cherry);
                break;
            case "Dragon Fruit":
                fact.setText(R.string.dragonfruit);
                break;
            case "Durian":
                fact.setText(R.string.durian);
                break;
            case "Grape":
                fact.setText(R.string.grape);
                break;
            case "Guava":
                fact.setText(R.string.guava);
                break;
            case "Kiwi":
                fact.setText(R.string.kiwi);
                break;
            case "Lime":
                fact.setText(R.string.lime);
                break;
            case "Longan":
                fact.setText(R.string.longan);
                break;
            case "Lychee":
                fact.setText(R.string.lychee);
                break;
            case "Mango":
                fact.setText(R.string.mango);
                break;
            case "Mangosteen":
                fact.setText(R.string.mangosteen);
                break;
            case "Orange":
                fact.setText(R.string.orange);
                break;
            case "Papaya":
                fact.setText(R.string.papaya);
                break;
            case "Pineapple":
                fact.setText(R.string.pineapple);
                break;
            case "Pomelo":
                fact.setText(R.string.pomelo);
                break;
            default:
                fact.setText("");
        }
    }*/

    private void shareImageToOtherApps() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        shareImageandText(bitmap);
        /*imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap selectedImageBitmap = imageView.getDrawingCache();

        if (selectedImageBitmap != null) {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg");

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "title");
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            Uri uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values);


            OutputStream outstream;
            try {
                outstream = getActivity().getContentResolver().openOutputStream(uri);
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                outstream.close();
            } catch (Exception e) {
                System.err.println(e.toString());
            }



            share.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(share, "Share Image"));
            }

        imageView.destroyDrawingCache();
        imageView.setDrawingCacheEnabled(false);*/
        }

    private void shareImageandText(Bitmap bitmap) {
        Uri uri = getmageToShare(bitmap);
        Intent intent = new Intent(Intent.ACTION_SEND);

        // putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        // adding text to share
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");

        // Add subject Here
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");

        // setting type to image
        intent.setType("image/png");

        // calling startactivity() to share
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    // Retrieving the url to share
    private Uri getmageToShare(Bitmap bitmap) {
        File imagefolder = new File(getActivity().getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "shared_image.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(getActivity(), "com.anni.shareimage.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    public void classifyImage(Bitmap image){
        imageView.setVisibility(View.VISIBLE);
        btsc.setVisibility(View.INVISIBLE);
        ln1.setVisibility(View.INVISIBLE);
        try {
            Model model = Model.newInstance(getActivity().getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 180, 180, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());
            int pixel = 0;
            for(int i = 0; i < imageSize; i ++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 1));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 1));
                }
            }


            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Model.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            Log.d("haha", String.valueOf(confidences.length));
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            int twoPos = 0;
            int threePos = 0;
            float maxConfidence = 0;
            for (int i =0; i < confidences.length; i++){
                if (confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            twoPos = findSecondLargestIndex(confidences);
            threePos = findThirdLargestIndex(confidences);

            Log.d("kaka1", String.valueOf(threePos));
            String[] classes = {"Apple","Avocado","Banana","Cherry","Coconut","Dragon Fruit","Durian","Grape","Guava","Kiwi","Lime","Longan","Lychee","Mango","Mangosteen","Orange","Papaya","Passion fruit","Pineapple","Pomelo"};
            result.setText(classes[maxPos]);
            cre.setText(classes[twoPos] +"(2nd) "+ classes[threePos] +"(3rd)");
            //fact();

            img();
            des();
            nutri();
            dd();
            vd();
            fact();
            result.setVisibility(View.VISIBLE);
            ttre.setVisibility(View.VISIBLE);
            sc.setVisibility(View.VISIBLE);




            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        // Lấy giá trị hiện tại từ Firebase và thêm vào số lần nhấn mới
        databaseReference.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            int clickCount = 0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int previousCount = snapshot.getValue(Integer.class);
                    clickCount += previousCount ;
                }
                // Cập nhật giá trị clickCount lên Firebase
                databaseReference.child(currentUser.getUid()).setValue(clickCount+1);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 3){
                Bitmap image = (Bitmap) data.getExtras().get("data");
                int dimension = Math.min(image.getWidth(),image.getHeight());
                image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                imageView.setImageBitmap(image);

                image = Bitmap.createScaledBitmap(image, imageSize , imageSize, false);
                classifyImage(image);
            }else{
                Uri dat = data.getData();
                Bitmap image = null;
                try {
                    image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), dat);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(image);
                image = Bitmap.createScaledBitmap(image, imageSize , imageSize, false);
                classifyImage(image);
            }
        }

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference folderRef  = storageRef.child("alternative_images"); // Đổi tên tệp ảnh

        String timestamp = String.valueOf(System.currentTimeMillis());
        StorageReference imageRef = folderRef.child(result.getText().toString() + "_" + timestamp);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] k = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(k);
        uploadTask.addOnFailureListener(exception -> {
            Toast.makeText(getActivity(), "Lỗi lưu ảnh lên server!", Toast.LENGTH_SHORT).show();
            // Xử lý nếu có lỗi xảy ra trong quá trình tải lên
        }).addOnSuccessListener(taskSnapshot -> {

        });
        super.onActivityResult(requestCode, resultCode, data);
    }
    public int findSecondLargestIndex(float[] array) {


        int largestIndex = 0;
        int secondLargestIndex = -1;

        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largestIndex]) {
                secondLargestIndex = largestIndex;
                largestIndex = i;
            } else if (secondLargestIndex == -1 || array[i] > array[secondLargestIndex]) {
                secondLargestIndex = i;
            }
        }

        return secondLargestIndex;
    }
    public int findThirdLargestIndex(float[] array) {

        int largestIndex = 0;
        int secondLargestIndex = -1;
        int thirdLargestIndex = -1;

        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largestIndex]) {
                thirdLargestIndex = secondLargestIndex;
                secondLargestIndex = largestIndex;
                largestIndex = i;
            } else if (secondLargestIndex == -1 || array[i] > array[secondLargestIndex]) {
                thirdLargestIndex = secondLargestIndex;
                secondLargestIndex = i;
            } else if (thirdLargestIndex == -1 || array[i] > array[thirdLargestIndex]) {
                thirdLargestIndex = i;
            }
        }

        return thirdLargestIndex;
    }

}
