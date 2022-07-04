package com.example.begusarai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Toast;

import com.example.begusarai.R;
import com.example.begusarai.adapter.mallAdapter;
import com.example.begusarai.adapter.schoolAdapter;
import com.example.begusarai.model.data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class your_upload_activity extends AppCompatActivity {

    private List<data> list;
    private mallAdapter adapter;
    private RecyclerView recyclerView;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_upload);
        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        Intent intent=getIntent();
        type=intent.getStringExtra("selected");
        adapter=new mallAdapter(list,your_upload_activity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        additem();
    }

    private void additem() {
        FirebaseFirestore.getInstance().collection(type.toLowerCase()+"s")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        data type_selected=snapshot.toObject(data.class);
                        list.add(type_selected);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }
}
