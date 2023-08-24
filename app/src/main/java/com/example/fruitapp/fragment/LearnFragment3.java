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

public class LearnFragment3 extends Fragment {

    private ViewPager viewPager;
    private PhotoAdapter photoAdapter;

    MediaPlayer vehicle_media;

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
        vehicle_media = MediaPlayer.create(getActivity(),R.raw.wheelchair);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                vehicle_media = MediaPlayer.create(getActivity(),getListPhoto().get(position).getIdRaw());
                tvanimal.setText(getListPhoto().get(position).getName());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vehicle_media.start();
            }
        });

        return view;
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo("Wheelchair",R.drawable.wheelchair,R.raw.wheelchair));
        list.add(new Photo("Van",R.drawable.van,R.raw.van));
        list.add(new Photo("Taxi",R.drawable.taxi,R.raw.taxi));
        list.add(new Photo("Police car",R.drawable.police,R.raw.policecar));
        list.add(new Photo("Ambulance",R.drawable.ambulance,R.raw.ambulance));
        list.add(new Photo("Bus",R.drawable.bus,R.raw.bus));
        list.add(new Photo("Scooter",R.drawable.scooter,R.raw.scooter));
        list.add(new Photo("Ship",R.drawable.ship,R.raw.ship));
        list.add(new Photo("Motor Cycle",R.drawable.motorcycle,R.raw.motorcycle));
        list.add(new Photo("Helicopter",R.drawable.helicopter,R.raw.helicopter));
        list.add(new Photo("Airplane",R.drawable.airplane,R.raw.airplane));
        list.add(new Photo("Train",R.drawable.train,R.raw.train));
        list.add(new Photo("Bicycle",R.drawable.bicycle,R.raw.bicycle));
        list.add(new Photo("Tractor",R.drawable.tractor,R.raw.tractor));
        list.add(new Photo("Truck",R.drawable.truck,R.raw.truck));
        list.add(new Photo("Ballon air",R.drawable.ballonair,R.raw.ballonair));
        list.add(new Photo("Subway",R.drawable.subway,R.raw.subway));
        list.add(new Photo("Fire engine",R.drawable.fireengine,R.raw.fireengine));
        list.add(new Photo("Boat",R.drawable.boat,R.raw.boat));

        return list;
    }
}
