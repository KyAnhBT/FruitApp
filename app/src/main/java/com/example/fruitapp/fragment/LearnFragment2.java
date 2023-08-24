package com.example.fruitapp.fragment;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fruitapp.Models.Photo;
import com.example.fruitapp.Adapters.PhotoAdapter;
import com.example.fruitapp.R;

import java.util.ArrayList;
import java.util.List;

public class LearnFragment2 extends Fragment {

    private ViewPager viewPager;
    private PhotoAdapter photoAdapter;

    MediaPlayer animal_media;

    ImageView play;

    TextView tvanimal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_learn_2, container, false);

        viewPager = view.findViewById(R.id.viewpager);
        tvanimal = view.findViewById(R.id.tvanimal);
        play = view.findViewById(R.id.play);

        photoAdapter = new PhotoAdapter(getActivity(), getListPhoto());
        viewPager.setAdapter(photoAdapter);
        tvanimal.setText(getListPhoto().get(viewPager.getCurrentItem()).getName());
        animal_media = MediaPlayer.create(getActivity(),R.raw.dog);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                animal_media = MediaPlayer.create(getActivity(),getListPhoto().get(position).getIdRaw());
                tvanimal.setText(getListPhoto().get(position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animal_media.start();
            }
        });

        return view;
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo("Dog",R.drawable.dog,R.raw.dog));
        list.add(new Photo("Cat",R.drawable.cat,R.raw.cat));
        list.add(new Photo("Chicken",R.drawable.chicken,R.raw.chicken));
        list.add(new Photo("Duck",R.drawable.duck,R.raw.duck));
        list.add(new Photo("Cow",R.drawable.cow,R.raw.cow));
        list.add(new Photo("Bird",R.drawable.bird,R.raw.bird));
        list.add(new Photo("Fish",R.drawable.fish,R.raw.fish));
        list.add(new Photo("Butterfly",R.drawable.butterfly,R.raw.butterfly));
        //list.add(new Photo("Rabit",R.drawable.rabit,R.raw.rabit));
        list.add(new Photo("Bear",R.drawable.bear,R.raw.bear));
       // list.add(new Photo("Snake",R.drawable.snake,R.raw.snake));
        list.add(new Photo("Bee",R.drawable.bee,R.raw.bee));
        list.add(new Photo("Frog",R.drawable.fog,R.raw.frog));
        //list.add(new Photo("Pig",R.drawable.pig,R.raw.pig));
        list.add(new Photo("Hippo",R.drawable.hippo,R.raw.hippo));
        list.add(new Photo("Giraffe",R.drawable.giraffe,R.raw.giraffe));
        list.add(new Photo("Crocodile",R.drawable.crocodile,R.raw.crocodile));
        //list.add(new Photo("Tiger",R.drawable.tiger,R.raw.tiger));
        list.add(new Photo("Fox",R.drawable.fox,R.raw.fox));
        list.add(new Photo("Horse",R.drawable.horse,R.raw.horse));

        return list;
    }
}
