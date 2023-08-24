package com.example.fruitapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fruitapp.Activities.MainActivity;
import com.example.fruitapp.R;

public class LearnFragment extends Fragment {

    ImageView animal, vehicle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_learn, container, false);

        animal = view.findViewById(R.id.animal);
        vehicle = view.findViewById(R.id.vehicle);

        animal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragment(new LearnFragment2());
            }
        });


        vehicle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MainActivity) getActivity()).replaceFragment(new LearnFragment3());
        }
    });
        return view;
    }
}
