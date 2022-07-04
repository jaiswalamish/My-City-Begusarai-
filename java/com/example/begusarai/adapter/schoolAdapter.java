package com.example.begusarai.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.begusarai.R;
import com.example.begusarai.model.data;
import com.squareup.picasso.Picasso;

import java.util.List;

public class schoolAdapter extends RecyclerView.Adapter<schoolAdapter.holder> {
    private List<data> list;
    private Context mContext;

    public schoolAdapter(List<data> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.schools,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        data school=list.get(position);
        Picasso.get().load(school.getImageUrl()).into(holder.pic);
        holder.description.setText(school.getDescription());
        holder.location.setText(school.getAddress());
        holder.name.setText(school.getName());
        holder.phone.setText(school.getPhone());
        holder.email.setText(school.getEmail());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(school.getLink()));
                    mContext.startActivity(myIntent);
                } catch (Exception e) {
                    Toast.makeText(mContext, "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(school.getLink()));
                    mContext.startActivity(myIntent);
                } catch (Exception e) {
                    Toast.makeText(mContext, "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView location;
        private TextView description;
        private TextView phone;
        private ImageView pic;
        private TextView email;

        public holder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.school_name);
            location=itemView.findViewById(R.id.school_location);
            description=itemView.findViewById(R.id.school_description);
            phone=itemView.findViewById(R.id.school_number);
            pic=itemView.findViewById(R.id.school_pic);
            email=itemView.findViewById(R.id.school_email);
        }
    }
}