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

public class mallAdapter extends RecyclerView.Adapter<mallAdapter.holder> {

    private List<data> list;
    private Context mContext;

    public mallAdapter(List<data> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.mall, parent, false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        data mall = list.get(position);
        Picasso.get().load(mall.getImageUrl()).into(holder.mall_pic);

        holder.phone.setText(mall.getPhone());
        holder.description.setText(mall.getDescription());
        holder.mall_name.setText(mall.getName());
        holder.location.setText(mall.getAddress());
        holder.email.setText(mall.getEmail());
        holder.mall_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mall.getLink()));
                    mContext.startActivity(myIntent);
                } catch (Exception e) {
                    Toast.makeText(mContext, "No application can handle this request."
                            + " Please install a webbrowser",  Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        holder.mall_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mall.getLink()));
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

    public class holder extends RecyclerView.ViewHolder {
        private ImageView mall_pic;
        private TextView mall_name;
        private TextView location;
        private TextView description;
        private TextView phone;
        private TextView email;

        public holder(@NonNull View itemView) {
            super(itemView);
            mall_name = itemView.findViewById(R.id.mall_name);
            mall_pic = itemView.findViewById(R.id.mall_pic);
            location = itemView.findViewById(R.id.location);
            description = itemView.findViewById(R.id.mall_description);
            phone = itemView.findViewById(R.id.phone);
            email=itemView.findViewById(R.id.email);

        }
    }
}