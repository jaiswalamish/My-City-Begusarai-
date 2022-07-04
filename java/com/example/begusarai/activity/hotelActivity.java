package com.example.begusarai.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.begusarai.R;
import com.example.begusarai.adapter.mallAdapter;
import com.example.begusarai.adapter.schoolAdapter;
import com.example.begusarai.fragments.homeFragment;
import com.example.begusarai.model.data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class hotelActivity extends AppCompatActivity {
    private List<data> list;
    private schoolAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView close;

    private CollectionReference db= FirebaseFirestore.getInstance().collection("hotels");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        adapter=new schoolAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(hotelActivity.this, homeFragment.class));
                finish();
            }
        });
        additem();
    }

    private void additem() {
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshots:task.getResult()){
                        data doctor=snapshots.toObject(data.class);
                        list.add(doctor);
                    }
                    Collections.reverse(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}