package com.example.begusarai.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.begusarai.R;
import com.example.begusarai.adapter.mallAdapter;
import com.example.begusarai.fragments.homeFragment;
import com.example.begusarai.model.data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class mallActivity extends AppCompatActivity {

    private List<data> list;
    private mallAdapter adapter;
    private RecyclerView recyclerView;
    private ImageView close;

    private CollectionReference db=FirebaseFirestore.getInstance().collection("malls");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall);
        recyclerView=findViewById(R.id.list_item);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        adapter=new mallAdapter(list,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        close=findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(mallActivity.this, homeFragment.class));
                finish();
            }
        });
        additem();
    }

    private void additem() {
        db.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
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