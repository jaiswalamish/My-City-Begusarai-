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
import com.example.begusarai.adapter.schoolAdapter;
import com.example.begusarai.fragments.homeFragment;
import com.example.begusarai.model.data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class schoolActivity extends AppCompatActivity {

    private List<data> list;
    private schoolAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school);
        recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new schoolAdapter(list,this);
        recyclerView.setAdapter(adapter);
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(schoolActivity.this, homeFragment.class));
                finish();
            }
        });
        addSchools();
    }

    private void addSchools() {
        FirebaseFirestore.getInstance().collection("schools").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshots:task.getResult()){
                        data mall=snapshots.toObject(data.class);
                        list.add(mall);
                    }
                    Collections.reverse(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}