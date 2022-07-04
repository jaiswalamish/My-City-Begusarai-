package com.example.begusarai.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.begusarai.R;
import com.example.begusarai.activity.bookStoreActivity;
import com.example.begusarai.activity.busActivity;
import com.example.begusarai.activity.doctorActivity;
import com.example.begusarai.activity.hotelActivity;
import com.example.begusarai.activity.mallActivity;
import com.example.begusarai.activity.schoolActivity;
import com.example.begusarai.activity.shopActivity;
import com.example.begusarai.activity.tourActivity;
import com.google.firebase.auth.FirebaseAuth;


public class homeFragment extends Fragment {

    private ImageView doctor;
    private ImageView shops;
    private ImageView hotels;
    private ImageView malls;
    private ImageView schools;
    private ImageView booksStore;
    private ImageView busStand;
    private ImageView tour;
    private ScrollView scrollView;
    private LinearLayout layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        doctor=view.findViewById(R.id.doctor);
        shops=view.findViewById(R.id.shopping);
        hotels=view.findViewById(R.id.hotel);
        malls=view.findViewById(R.id.mall);
        schools=view.findViewById(R.id.schools);
        booksStore=view.findViewById(R.id.book);
        busStand=view.findViewById(R.id.bus);
        tour=view.findViewById(R.id.tour);


        malls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), mallActivity.class));
            }
        });
        schools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), schoolActivity.class));
            }
        });
        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), doctorActivity.class));
            }
        });
        shops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), shopActivity.class));
            }
        });
        hotels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), hotelActivity.class));
            }
        });
        booksStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), bookStoreActivity.class));
            }
        });
        busStand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), busActivity.class));
            }
        });
        tour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), tourActivity.class));
            }
        });


        return view;
    }
}