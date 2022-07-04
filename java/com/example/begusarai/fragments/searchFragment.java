package com.example.begusarai.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.begusarai.MainActivity;
import com.example.begusarai.R;
import com.example.begusarai.adapter.mallAdapter;
import com.example.begusarai.model.data;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class searchFragment extends Fragment {
    private TextView select;
    private EditText search;
    private RecyclerView recyclerView;
    private mallAdapter adapter;
    private List<data> list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        search=view.findViewById(R.id.search_text);
        select=view.findViewById(R.id.choose_type_text);

        recyclerView=view.findViewById(R.id.search_recycle_view);
        recyclerView.setHasFixedSize(true);
        list=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new mallAdapter(list,getContext());
        recyclerView.setAdapter(adapter);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu=new PopupMenu(getContext(),view);
                menu.inflate(R.menu.addchose);
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        select.setText(menuItem.getTitle().toString().toLowerCase());
                        addAllData(select.getText().toString());
                        return true;
                    }
                });
            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String type=select.getText().toString();
                if(search.getText().length()==0){

                    if(type.equals("choose type!!")){

                    }else{
                        addAllData(type);
                    }
                }else{
                    if(type.equals("choose type!!")){
                        Toast.makeText(getContext(), "first select Type", Toast.LENGTH_SHORT).show();

                    }else{
                        addDataOnTextChanged(type,charSequence.toString());
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void addDataOnTextChanged(String type,String letter) {
        FirebaseFirestore.getInstance().collection(type+"s")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    list.clear();
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        data selected=snapshot.toObject(data.class);
                        Log.d("name",selected.getName());
                        if(selected.getName().toLowerCase().contains(letter) ||
                           selected.getName().toUpperCase().contains(letter) ||
                        selected.getName().contains(letter)){
                            list.add(selected);
                        }

                    }
                    Collections.reverse(list);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private void addAllData(String type) {

            FirebaseFirestore.getInstance().collection(type+"s")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull  Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                list.clear();
                                for(QueryDocumentSnapshot snapshot: task.getResult()){
                                    data selected_type=snapshot.toObject(data.class);
                                    list.add(selected_type);
                                }
                                Collections.reverse(list);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
    }
}